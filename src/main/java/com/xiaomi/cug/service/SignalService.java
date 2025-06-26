package com.xiaomi.cug.service;

import com.xiaomi.cug.config.RedisCacheDelayDelete;
import com.xiaomi.cug.dto.BatterySignalDTO;
import com.xiaomi.cug.entity.BatterySignal;
import com.xiaomi.cug.entity.MessageLog;
import com.xiaomi.cug.mapper.MessageLogMapper;
import com.xiaomi.cug.mapper.SignalDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SignalService {

        private final String SIGNAL_REDIS_KEY_PREFIX = "signal:";

        @Autowired
        private SignalDataMapper signalDataMapper;

        @Autowired
        private RocketMQTemplate rocketMQTemplate;

        @Autowired
        private MessageLogMapper messageLogMapper;

        @Autowired
        private RedisTemplate<String, Object> redisTemplate;

        @Autowired
        private RedisCacheDelayDelete redisCacheDelayDelete;

        public void upload(BatterySignalDTO batterySignal) {
                String txId = UUID.randomUUID().toString();
                batterySignal.setTransactionId(txId);
                rocketMQTemplate.convertAndSend("battery-signal-topic", batterySignal);
        }

        @Transactional
        public void save(BatterySignalDTO batterySignal) {
                // 先检查幂等日志
                String transactionId = batterySignal.getTransactionId();
                MessageLog messageLog = new MessageLog();
                messageLog.setChassisNumber(batterySignal.getChassisNumber());
                messageLog.setTransactionId(transactionId);
                if (messageLogMapper.existsByTransactionId(messageLog)) {
                        log.info("消息已处理，忽略:{}", transactionId);
                        return;
                }
                // 写入数据库
                BatterySignal bSignal = new BatterySignal();
                BeanUtils.copyProperties(batterySignal, bSignal);
                signalDataMapper.insertBatterySignal(bSignal);

                // 写入redis
                redisTemplate.opsForValue().set(SIGNAL_REDIS_KEY_PREFIX + batterySignal.getVid(), batterySignal, 10, TimeUnit.MINUTES);

                // 写入幂等日志
                messageLog.setStatus("SUCCESS");
                try {
                        messageLog.setCreatedTime(LocalDateTime.now());
                        messageLogMapper.insert(messageLog);
                } catch (Exception e) {
                        log.error("保存幂等日志失败", e);
                }
        }

        public BatterySignal queryByVidAndDate(BatterySignalDTO batterySignal) {
                Object object = redisTemplate.opsForValue().get(SIGNAL_REDIS_KEY_PREFIX + batterySignal.getVid());
                if (object instanceof BatterySignal) {
                        BatterySignal bSignal = (BatterySignal) object;
                        log.info("从redis获取数据:{}", bSignal);
                        return bSignal;
                }
                BatterySignal bSignal = new BatterySignal();
                BeanUtils.copyProperties(batterySignal, bSignal);
                BatterySignal batterySignals = signalDataMapper.queryByVidAndDate(bSignal);
                if(null != batterySignals) {
                        redisTemplate.opsForValue().set(SIGNAL_REDIS_KEY_PREFIX + batterySignal.getVid(), batterySignals, 10, TimeUnit.MINUTES);
                }
                return batterySignals;
        }

        public void deleteById(BatterySignalDTO dto) {
                BatterySignal bSignal = new BatterySignal();
                BeanUtils.copyProperties(dto, bSignal);
                String key = SIGNAL_REDIS_KEY_PREFIX + bSignal.getVid();
                delayDeleteSignalIfNeeded(key, bSignal);
                signalDataMapper.deleteById(bSignal);
        }

        public void updateSignalData(BatterySignalDTO batterySignal) {
                BatterySignal bSignal = new BatterySignal();
                BeanUtils.copyProperties(batterySignal, bSignal);
                String key = SIGNAL_REDIS_KEY_PREFIX + batterySignal.getVid();
                // 延迟删除 + 更新
                delayDeleteSignalIfNeeded(key, bSignal);
                signalDataMapper.updateSignalData(bSignal);

        }

        private void delayDeleteSignalIfNeeded(String key, BatterySignal bSignal) {
                // 查询redis中是否是最新的电池信号状态
                // 如果是最新的，修改后需要更新，则延迟双删
                BatterySignal inRedisSignal = (BatterySignal)redisTemplate.opsForValue().get(key);
                if(null != inRedisSignal && inRedisSignal.getReportTime().isBefore(bSignal.getReportTime())) {
                        redisCacheDelayDelete.delayDelete(key, 100);
                }
        }

}
