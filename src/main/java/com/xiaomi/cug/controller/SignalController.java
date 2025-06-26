package com.xiaomi.cug.controller;

import com.xiaomi.cug.dto.BatterySignalDTO;
import com.xiaomi.cug.dto.ResponseResult;
import com.xiaomi.cug.entity.BatterySignal;
import com.xiaomi.cug.service.SignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/signal")
public class SignalController {

        @Autowired
        private SignalService signalService;

        @PostMapping("/upload")
        public ResponseResult<String> uploadSignal(@RequestBody BatterySignalDTO batterySignal) {
                signalService.upload(batterySignal);
                return ResponseResult.success(null);
        }

        @PostMapping("/query")
        public ResponseResult<BatterySignal> querySignal(@RequestBody BatterySignalDTO batterySignal) {
                BatterySignal batterySignals = signalService.queryByVidAndDate(batterySignal);
                return ResponseResult.success(batterySignals);
        }

        @PostMapping("/delete")
        public ResponseResult<String> deleteSignal(@RequestBody BatterySignalDTO batterySignal) {
                signalService.deleteById(batterySignal);
                return ResponseResult.success(null);
        }

        @PostMapping("/update")
        public ResponseResult<String> updateSignal(@RequestBody BatterySignalDTO batterySignal) {
                signalService.updateSignalData(batterySignal);
                return ResponseResult.success(null);
        }
}
