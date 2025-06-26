package com.xiaomi.cug.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

//电池信号实体
@Data
@NoArgsConstructor
public class BatterySignal {

        private Long id;

        private String vid;

        private Long chassisNumber;

        private String signalData;

        private Integer status;

        @JsonIgnore
        private Map<String, Object> signalDataMap;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reportTime;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdTime;
}