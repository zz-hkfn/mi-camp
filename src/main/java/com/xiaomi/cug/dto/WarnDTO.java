package com.xiaomi.cug.dto;

import com.xiaomi.cug.entity.RuleItem;
import com.xiaomi.cug.entity.SignalData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WarnDTO {
        private Long signalId;
        private Long carId;
        private Long warnId;
        private SignalData signal;

        private Integer ruleNumber;

        private String ruleName;

        private Integer batteryType;

        private String signalType;

        private String ruleConfig;

        private List<RuleItem> ruleItemList;

        private Integer warnLevel;
}
