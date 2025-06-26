package com.xiaomi.cug.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

// 预警记录实体
@Data
@NoArgsConstructor
public class WarningRecord {
        private Long id;

        private Long signalId;

        private String vid;

        private Long chassisNumber;

        private Integer batteryType;

        private String ruleNumber;

        private String ruleName;

        private Integer warningLevel;

        private String signalData;

        private Map<String, Object> signalDataMap;

        private String warningDesc;

        private Integer status;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdTime;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedTime;
}