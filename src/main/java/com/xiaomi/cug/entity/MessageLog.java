package com.xiaomi.cug.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageLog {
        private Long id;
        private Long chassisNumber;
        private String status;
        private String transactionId;
        private LocalDateTime createdTime;
}
