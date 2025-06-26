package com.xiaomi.cug.scheduler;

import com.xiaomi.cug.config.DynamicDataSourceContextHolder;
import com.xiaomi.cug.entity.BatterySignal;
import com.xiaomi.cug.mapper.SignalDataMapper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SignalScanJob {

        @Autowired
        private SignalDataMapper signalMapper;

        @Autowired
        private RocketMQTemplate rocketMQTemplate;

        @Scheduled(fixedRate = 60000) // 每1分钟执行
        public void scanAndSendDB0() {
                try {
                        // TODO: 以下参数仅为了分库分表逻辑，实际业务中需优化
                        BatterySignal batterySignal = new BatterySignal();
                        batterySignal.setStatus(0);
                        batterySignal.setChassisNumber(0L);
                        batterySignal.setReportTime(LocalDateTime.now());
                        List<BatterySignal> signals = signalMapper.queryByVidAndDateMulti(batterySignal); // 自定义查询

                        for (BatterySignal signal : signals) {
                                rocketMQTemplate.convertAndSend("signal-warning-topic", signal);
                        }
                } finally {
                        DynamicDataSourceContextHolder.clear();
                }
        }

//        @Scheduled(fixedRate = 60000) // 每1分钟执行
//        public void scanAndSendDB1() {
//                try {
//                        // TODO: 以下参数仅为了分库分表逻辑，实际业务中需优化
//                        BatterySignal batterySignal = new BatterySignal();
//                        batterySignal.setStatus(0);
//                        batterySignal.setChassisNumber(1L);
//                        DynamicDataSourceContextHolder.set("bmsDb1");
//                        batterySignal.setReportTime(LocalDateTime.now());
//                        List<BatterySignal> signals = signalMapper.queryByVidAndDateMulti(batterySignal); // 自定义查询
//                        for (BatterySignal signal : signals) {
//                                rocketMQTemplate.convertAndSend("signal-warning-topic", signal);
//                        }
//                } finally {
//                        DynamicDataSourceContextHolder.clear();
//                }
//        }
}