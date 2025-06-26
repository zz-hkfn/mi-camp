package com.xiaomi.cug.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BatterySignalDTO {
        private Long id;

        private String vid;

        private Long chassisNumber;

        private String signalData;

        private String transactionId;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reportTime;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdTime;

}
