package com.xiaomi.cug.mapper;

import com.xiaomi.cug.entity.WarningRule;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarnRuleMapper {
        @Insert("INSERT INTO warning_rule(rule_number, rule_name, battery_type, signal_type, rule_config, created_time, updated_time) " +
                        "VALUES(#{ruleNumber}, #{ruleName}, #{batteryType}, #{signalType}, #{ruleConfig},  NOW(), NOW())")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        void insert(WarningRule rule);

        @Insert({"<script>",
                        "INSERT INTO warning_rule(rule_number, rule_name, battery_type, signal_type, rule_config,  created_time, updated_time) VALUES ",
                        "<foreach collection='rules' item='rule' separator=','>",
                        "(#{rule.ruleNumber}, #{rule.ruleName}, #{rule.batteryType}, #{rule.signalType}, #{rule.ruleConfig}, NOW(), NOW())",
                        "</foreach>",
                        "</script>"})
        void insertBatch(@Param("rules") List<WarningRule> rules);

        @Update("UPDATE warning_rule SET rule_number=#{ruleNumber}, rule_name=#{ruleName}, battery_type=#{batteryType}, signal_type=#{signalType}, " +
                        "rule_config=#{ruleConfig}, updated_time=NOW() WHERE id=#{id}")
        void updateById(WarningRule rule);

        @Select("SELECT id, rule_number, rule_name, battery_type, signal_type, rule_config, created_time, updated_time FROM warning_rule LIMIT 20")
        List<WarningRule> selectList();

        @Select({"<script>",
                        "SELECT id, rule_number, rule_name, battery_type, signal_type, rule_config, created_time, updated_time FROM warning_rule WHERE id IN ",
                        "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                        "#{id}",
                        "</foreach>",
                        "</script>"}) List<WarningRule> selectBatchIds(@Param("ids") List<Long> ids);

        @Delete("DELETE FROM warning_rule WHERE id=#{id}")
        void deleteById(Long id);

        @Delete({"<script>",
                        "DELETE FROM warning_rule WHERE id IN ",
                        "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                        "#{id}",
                        "</foreach>",
                        "</script>"})
        void deleteBatchIds(@Param("ids") List<Long> ids);
}
