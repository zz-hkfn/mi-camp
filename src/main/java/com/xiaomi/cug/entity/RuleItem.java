package com.xiaomi.cug.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleItem {
        private String expression;
        private Integer warnLevel;
}
