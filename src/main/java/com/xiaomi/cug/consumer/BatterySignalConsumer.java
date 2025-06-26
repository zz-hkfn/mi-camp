package com.xiaomi.cug.consumer;

import com.xiaomi.cug.dto.BatterySignalDTO;
import com.xiaomi.cug.service.SignalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
                topic = "battery-signal-topic",
                consumerGroup = "signal-consumer-group"
)
public class BatterySignalConsumer implements RocketMQListener<BatterySignalDTO> {

        @Autowired
        private SignalService signalService;

        @Override
        public void onMessage(BatterySignalDTO batterySignalDTO) {
                try {
                        // 入库写缓存操作
                        signalService.save(batterySignalDTO);
                } catch (Exception e) {
                        log.error("保存数据失败", e);
                        throw e;
                }
        }
}
