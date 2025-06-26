package com.xiaomi.cug.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VehicleInfo {
        private Long id;

        private String vid;

        private Integer batteryType;

        private BigDecimal totalMileage;

        private Integer batteryHealth;

        private Integer status;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdTime;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedTime;
}
