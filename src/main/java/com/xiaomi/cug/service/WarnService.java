package com.xiaomi.cug.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import com.xiaomi.cug.config.RedisCacheDelayDelete;
import com.xiaomi.cug.dto.ResponseResult;
import com.xiaomi.cug.dto.WarnDTO;
import com.xiaomi.cug.entity.*;
import com.xiaomi.cug.dto.WarnRuleDTOMapper;
import com.xiaomi.cug.mapper.SignalDataMapper;
import com.xiaomi.cug.mapper.VehicleInfoMapper;
import com.xiaomi.cug.mapper.WarnRuleMapper;
import com.xiaomi.cug.mapper.WarningRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarnService {

        private static final String WARN_RULE_HASH_KEY = "warn_rules";

        @Autowired
        private WarnRuleMapper warnRuleMapper;

        @Autowired
        private RedisTemplate<String, Object> redisTemplate;

        @Autowired
        private WarnRuleDTOMapper warnRuleDTOMapper;
        @Autowired private RedisCacheDelayDelete redisCacheDelayDelete;

        @Autowired
        private VehicleInfoMapper vehicleInfoMapper;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private WarningRecordMapper warningRecordMapper;

        @Autowired
        private SignalDataMapper signalDataMapper;

        public List<WarnDTO>  warn(List<WarnDTO> warnDTOS) {
                List<WarningRule> allRules = this.getWarnRules(null);
                HashMap<Long, WarningRule> rules = new HashMap<>();
                // 将缓存中的数据转换成 Map，方便后续按照规则ID获取
                for (WarningRule allRule : allRules) {
                        List<RuleItem> ruleItemList = null;
                        try {
                                ruleItemList = objectMapper.readValue(allRule.getRuleConfig(), new TypeReference<List<RuleItem> >() {});
                        } catch (Exception e) {
                                log.error("解析规则配置失败:{}", allRule.getRuleConfig());
                        }
                        allRule.setRuleItemList(ruleItemList);
                        rules.put(allRule.getId(), allRule);
                }
                List<WarnDTO> resultList = new ArrayList<>();
                for (WarnDTO warnDTO : warnDTOS) {
                        VehicleInfo vehicleInfo = vehicleInfoMapper.selectById(warnDTO.getCarId());
                        if (null != warnDTO.getWarnId()) {
                                WarningRule rule = rules.get(warnDTO.getWarnId());
                                // 车的电池类型和规则的电池类型一致，则继续处理
                                // 指定规则ID时，一个上报的信号对应一个规则匹配的结果
                                if (null != rule && null != rule.getBatteryType() && rule.getBatteryType().equals(vehicleInfo.getBatteryType())) {
                                        WarnDTO warnLevel = this.getWarnLevel(rule, warnDTO, warnDTO.getSignal());
                                        resultList.add(warnLevel);
                                }
                        } else {
                                // 电池类型对应时，不指定规则ID时，只要规则中电池类型和车的电池类型匹配就匹配规则, 一个上报的信号对应多个规则匹配的结果
                                for (WarningRule theRule : allRules) {
                                        if (null != theRule && null != theRule.getBatteryType() && theRule.getBatteryType().equals(vehicleInfo.getBatteryType())) {
                                                WarnDTO warnLevel = this.getWarnLevel(theRule, warnDTO, warnDTO.getSignal());
                                                resultList.add(warnLevel);
                                        }
                                }
                        }
                }

                return resultList;
        }

        private WarnDTO getWarnLevel(WarningRule rule, WarnDTO dto, SignalData signal) {
                WarnDTO theReturnDTO = new WarnDTO();
                BeanUtils.copyProperties(dto, theReturnDTO);
                theReturnDTO.setWarnId(rule.getId());
                theReturnDTO.setSignalType(rule.getSignalType());
                theReturnDTO.setRuleName(rule.getRuleName());
                theReturnDTO.setRuleNumber(rule.getRuleNumber());
                List<RuleItem> ruleConfig = rule.getRuleItemList();
                Integer level = null;
                if (CollectionUtils.isEmpty(ruleConfig)) {
                        return theReturnDTO;
                }
                for (RuleItem ruleItem : ruleConfig) {
                        String expression = ruleItem.getExpression();
                        Integer warnLevel = ruleItem.getWarnLevel();
                        if (null == expression || null == warnLevel) {
                                continue;
                        }
                        try {
                                Boolean result = (Boolean) AviatorEvaluator.execute(expression, signal.getSignalDataMap());
                                if (result) {
                                        level = warnLevel;
                                }
                        } catch (Exception e) {
                                log.warn("表达式执行失败，可能存在未定义变量: {}", e.getMessage());
                        }
                }
                theReturnDTO.setWarnLevel(level);
                return theReturnDTO;
        }

        @Transactional
        public void processSignal(BatterySignal signal) throws Exception{
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setCarId(signal.getChassisNumber());
                String signalData = signal.getSignalData();
                if(!StringUtils.isEmpty(signalData)) {
                        SignalData sd = objectMapper.readValue(signalData, SignalData.class);
                        warnDTO.setSignal(sd);
                }
                warnDTO.setCarId(signal.getChassisNumber());
                warnDTOS.add(warnDTO);

                List<WarnDTO> warn = this.warn(warnDTOS);
                List<WarningRecord> records = new ArrayList<>();
                for (WarnDTO warnDTO1 : warn) {
                        WarningRecord record = new WarningRecord();
                        BeanUtils.copyProperties(warnDTO1, record);
                        records.add(record);
                }
                warningRecordMapper.insertBatch( records);
                signal.setStatus(1);
                signalDataMapper.updateSignalData(signal);
        }


        public void saveWarnRules(List<WarnDTO> warnDTOS) {
                List<WarningRule> list = warnRuleDTOMapper.toList(warnDTOS);
                warnRuleMapper.insertBatch(list);
        }

        public List<WarningRule> getWarnRules(List<Long> warnIds) {
                // 查询所有规则：从数据库取全量 + 回填 Redis
                if (CollectionUtils.isEmpty(warnIds)) {
                        List<WarningRule> allRules = warnRuleMapper.selectList();
                        Map<String, WarningRule> map = allRules.stream()
                                        .collect(Collectors.toMap(rule -> String.valueOf(rule.getId()), rule -> rule));
                        redisTemplate.opsForHash().putAll(WARN_RULE_HASH_KEY, map);
                        redisTemplate.expire(WARN_RULE_HASH_KEY, 1, TimeUnit.DAYS);
                        return allRules;
                }
                // 按字段批量获取 Hash 中的 WarningRule


                List<Object> idStrList = warnIds.stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList());

                List<Object> ruleObjs = redisTemplate.opsForHash().multiGet(WARN_RULE_HASH_KEY, idStrList);
                List<WarningRule> result = new ArrayList<>();
                List<Long> missingIds = new ArrayList<>();
                if (!CollectionUtils.isEmpty(ruleObjs)) {
                        for (int i = 0; i < warnIds.size(); i++) {
                                WarningRule rule = (WarningRule) ruleObjs.get(i);
                                if (null != rule) {
                                        result.add(rule);
                                } else {
                                        missingIds.add(warnIds.get(i));
                                }
                        }
                }
                // 查询缺失部分
                if (!missingIds.isEmpty()) {
                        List<WarningRule> fromDb = warnRuleMapper.selectBatchIds(missingIds);
                        result.addAll(fromDb);
                        // 回填 Redis Hash
                        Map<String, WarningRule> fillMap = fromDb.stream()
                                        .collect(Collectors.toMap(rule -> String.valueOf(rule.getId()), rule -> rule));
                        redisTemplate.opsForHash().putAll(WARN_RULE_HASH_KEY, fillMap);
                }

                return result;
        }

        public void updateWarnRules(WarnDTO warnDTO) {
                WarningRule warnRules = warnRuleDTOMapper.toEntity(warnDTO);
                List<String> redisKeys = new ArrayList<>();
                redisKeys.add(warnDTO.getWarnId().toString());
                redisCacheDelayDelete.delayDeleteHashFields(WARN_RULE_HASH_KEY, redisKeys, 100);
                warnRuleMapper.updateById(warnRules);
        }

        public void deleteWarnRules(List<Long> warnIds) {
                List<String> collect = warnIds.stream().map(Object::toString).collect(Collectors.toList());
                redisCacheDelayDelete.delayDeleteHashFields(WARN_RULE_HASH_KEY, collect, 100);
                warnRuleMapper.deleteBatchIds(warnIds);
        }

}
