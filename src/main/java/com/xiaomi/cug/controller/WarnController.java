package com.xiaomi.cug.controller;

import com.xiaomi.cug.dto.ResponseResult;
import com.xiaomi.cug.dto.WarnDTO;
import com.xiaomi.cug.entity.WarningRecord;
import com.xiaomi.cug.entity.WarningRule;
import com.xiaomi.cug.service.WarnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WarnController {

        @Autowired
        private WarnService warnService;

        @PostMapping("/warn")
        public ResponseResult<List<WarnDTO>> warn(@RequestBody List<WarnDTO> warnDTOS) {
                if (CollectionUtils.isEmpty(warnDTOS)) return ResponseResult.error("warnDTOS is empty");
                List<WarnDTO> warn = warnService.warn(warnDTOS);
                return ResponseResult.success(warn);
        }

        @PostMapping("/getWarnRules")
        public ResponseResult<List<WarningRule>> getWarnRules(@RequestBody List<Long> warnIds) {
                return ResponseResult.success(warnService.getWarnRules(warnIds));
        }

        @PostMapping("/saveRules")
        public ResponseResult<String> saveWarnRules(@RequestBody List<WarnDTO> warnDTOS) {
                if (CollectionUtils.isEmpty(warnDTOS)) return ResponseResult.error("warnDTOS is empty");
                warnService.saveWarnRules(warnDTOS);
                return ResponseResult.success(null);
        }

        @PostMapping("/updateRules")
        public ResponseResult<String> updateRules(@RequestBody WarnDTO warnDTO) {
                if (warnDTO == null) return ResponseResult.error("warnDTO is null");
                if (warnDTO.getWarnId() == null) return ResponseResult.error("warnId is null");
                warnService.updateWarnRules(warnDTO);
                return ResponseResult.success(null);
        }

        @PostMapping("/deleteRules")
        public ResponseResult<List<WarningRule>> deleteRules(@RequestBody List<Long> warnIds) {
                if (CollectionUtils.isEmpty(warnIds)) return ResponseResult.error("warnIds is empty");
                warnService.deleteWarnRules(warnIds);
                return ResponseResult.success(null);
        }
}
