//package com.xiaomi.cug.consumer;
//
//import com.xiaomi.cug.entity.BatterySignal;
//import com.xiaomi.cug.service.WarnService;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RocketMQMessageListener(
//                topic = "signal-warning-topic",
//                consumerGroup = "signal-warning-consumer-group"
//)
//public class SignalWarningConsumer implements RocketMQListener<BatterySignal> {
//
//        @Autowired
//        private WarnService warningService;
//
//        @SneakyThrows @Override
//        public void onMessage(BatterySignal dto) {
//                try {
//                        warningService.processSignal(dto);
//                } catch (Exception e) {
//                        log.error("保存数据失败", e);
//                        throw e;
//                }
//        }
//}
