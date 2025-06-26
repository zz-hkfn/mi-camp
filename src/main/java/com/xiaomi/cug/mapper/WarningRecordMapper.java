package com.xiaomi.cug.mapper;

import com.xiaomi.cug.entity.WarningRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WarningRecordMapper {
        @Insert({"<script>",
                        "INSERT INTO warning_record(vid, chassis_number, battery_type, signal_type, rule_number, rule_name, warning_level, signal_data, warning_desc, status, created_time, updated_time) VALUES ",
                        "<foreach collection='rules' item='rule' separator=','>",
                        "(#{vid}, #{chassisNumber}, #{batteryType}, #{signalType}, #{ruleNumber}, #{ruleName}, #{warningLevel}, #{signalData}, #{warningDesc}, #{status}, NOW(), NOW())",
                        "</foreach>",
                        "</script>"})
        void insertBatch(@Param("records") List<WarningRecord> records);

}
