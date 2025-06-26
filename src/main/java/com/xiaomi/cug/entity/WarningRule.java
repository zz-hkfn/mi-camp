package com.xiaomi.cug.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//预警规则实体
@Data
@NoArgsConstructor
public class WarningRule {
        private Long id;

        private Integer ruleNumber;

        private String ruleName;

        private Integer batteryType;

        private String signalType;

        private String ruleConfig;

        private List<RuleItem> ruleItemList;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss") private LocalDateTime createdTime;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss") private LocalDateTime updatedTime;
}

