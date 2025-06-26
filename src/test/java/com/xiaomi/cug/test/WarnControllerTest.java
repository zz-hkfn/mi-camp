package com.xiaomi.cug.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.cug.dto.ResponseResult;
import com.xiaomi.cug.dto.WarnDTO;
import com.xiaomi.cug.entity.RuleItem;
import com.xiaomi.cug.entity.SignalData;
import com.xiaomi.cug.entity.WarningRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j @SpringBootTest
@AutoConfigureMockMvc
public class WarnControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void testInsert() throws Exception {
                List<WarnDTO> rules = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setRuleNumber(1);
                warnDTO.setRuleName("电压差报警");
                warnDTO.setBatteryType(1);
                warnDTO.setSignalType("VOLTAGE");
                warnDTO.setRuleConfig("[\n" + "  {\n" + "    \"expression\": \"(Mx - Mi) >= 5\",\n" + "    \"warnLevel\": 0\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 3 && (Mx - Mi) < 5\",\n" + "    \"warnLevel\": 1\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 1 && (Mx - Mi) < 3\",\n" + "    \"warnLevel\": 2\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 0.6 && (Mx - Mi) < 1\",\n" + "    \"warnLevel\": 3\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 0.2 && (Mx - Mi) < 0.6\",\n" + "    \"warnLevel\": 4\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) < 0.2\",\n" + "    \"warnLevel\": -1\n" + "  }\n" + "]\n");
                rules.add(warnDTO);

                warnDTO = new WarnDTO();
                warnDTO.setRuleNumber(1);
                warnDTO.setRuleName("电压差报警");
                warnDTO.setBatteryType(2);
                warnDTO.setSignalType("VOLTAGE");
                warnDTO.setRuleConfig("[\n" + "  {\n" + "    \"expression\": \"(Mx - Mi) >= 2\",\n" + "    \"warnLevel\": 0\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 1 && (Mx - Mi) < 2\",\n" + "    \"warnLevel\": 1\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 0.7 && (Mx - Mi) < 1\",\n" + "    \"warnLevel\": 2\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 0.4 && (Mx - Mi) < 0.7\",\n" + "    \"warnLevel\": 3\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) >= 0.2 && (Mx - Mi) < 0.4\",\n" + "    \"warnLevel\": 4\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Mx - Mi) < 0.2\",\n" + "    \"warnLevel\": -1\n" + "  }\n" + "]\n");
                rules.add(warnDTO);

                warnDTO = new WarnDTO();
                warnDTO.setRuleNumber(2);
                warnDTO.setRuleName("电流差报警");
                warnDTO.setBatteryType(1);
                warnDTO.setSignalType("CURRENT");
                warnDTO.setRuleConfig("[\n" + "  {\n" + "    \"expression\": \"(Ix - Ii) >= 3\",\n" + "    \"warnLevel\": 0\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) >= 1 && (Ix - Ii) < 3\",\n" + "    \"warnLevel\": 1\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) >= 0.2 && (Ix - Ii) < 1\",\n" + "    \"warnLevel\": 2\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) < 0.2\",\n" + "    \"warnLevel\": -1\n" + "  }\n" + "]\n");
                rules.add(warnDTO);
                warnDTO = new WarnDTO();
                warnDTO.setRuleNumber(2);
                warnDTO.setRuleName("电流差报警");
                warnDTO.setBatteryType(2);
                warnDTO.setSignalType("CURRENT");
                warnDTO.setRuleConfig("[\n" + "  {\n" + "    \"expression\": \"(Ix - Ii) >= 1\",\n" + "    \"warnLevel\": 0\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) >= 0.5 && (Ix - Ii) < 1\",\n" + "    \"warnLevel\": 1\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) >= 0.2 && (Ix - Ii) < 0.5\",\n" + "    \"warnLevel\": 2\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) < 0.2\",\n" + "    \"warnLevel\": -1\n" + "  }\n" + "]\n");
                rules.add(warnDTO);

                MvcResult result = mockMvc.perform(post("/api/saveRules")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(rules)))
                                .andExpect(status().isOk()).andReturn();
                log.info("result: {}", result.getResponse().getContentAsString());


        }

        @Test
        public void testQuery() throws Exception {
                List<Long> warnIds = new ArrayList<>();
                MvcResult result = mockMvc.perform(post("/api/getWarnRules")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnIds)))
                                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("result: {}", respnse);
                ResponseResult<List<WarningRule>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarningRule>>>() {
                });
                // 校验顺序与传入的 warnIds 是否一致
                Assertions.assertEquals(4, responseList.getData().size(), "Returned IDs do not match input IDs ");
        }


        @Test
        public void testQueryAll() throws Exception {
                List<Long> warnIds = new ArrayList<>();
                MvcResult result = mockMvc.perform(post("/api/getWarnRules")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnIds)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("result: {}", respnse);
                ResponseResult<List<WarningRule>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarningRule>>>() {
                });
                // 校验顺序与传入的 warnIds 是否一致
                Assertions.assertEquals(4, responseList.getData().size(), "Returned IDs do not match input IDs in order");
        }



        @Test
        public void testWarnRuleNotMatch() throws Exception {
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setWarnId(4L); // 规则编号4对应铁锂电池
                warnDTO.setCarId(1L);  // 车编号1对应三元电池
                warnDTO.setSignal(new SignalData(new BigDecimal("0.5"), new BigDecimal("0.5"), new BigDecimal("0.5"), new BigDecimal("0.5")));
                warnDTOS.add(warnDTO);
                MvcResult result = mockMvc.perform(post("/api/warn")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTOS)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                ResponseResult<List<WarnDTO>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarnDTO>>>() {});
                Assertions.assertEquals(0, responseList.getData().size(), "Returned IDs do not match input IDs in order");
        }

        @Test
        public void testWarnMatch() throws Exception {
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setWarnId(4L); // 规则编号4对应铁锂电池
                warnDTO.setCarId(2L);  // 车编号1对应铁锂电池
                warnDTO.setSignal(new SignalData(new BigDecimal("0.5"), new BigDecimal("0.5"), new BigDecimal("0.5"), new BigDecimal("0.5")));
                warnDTOS.add(warnDTO);
                MvcResult result = mockMvc.perform(post("/api/warn")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTOS)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("responseList: {}", respnse);
                ResponseResult<List<WarnDTO>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarnDTO>>>() {});
                Assertions.assertEquals(1, responseList.getData().size(), "至少有一条记录");
                Assertions.assertEquals(-1, responseList.getData().get(0).getWarnLevel(), "应当返回不报警状态");
        }

        @Test
        public void testWarnLevel0() throws Exception {
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setWarnId(2L); // 规则编号2对应铁锂电池,电压差报警
                warnDTO.setCarId(2L);  // 车编号1对应铁锂电池
                // 模拟电压差等于2，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("2.5"), new BigDecimal("0.5"), new BigDecimal("0.5"), new BigDecimal("0.5")));
                warnDTOS.add(warnDTO);
                MvcResult result = mockMvc.perform(post("/api/warn")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTOS)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("responseList: {}", respnse);
                ResponseResult<List<WarnDTO>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarnDTO>>>() {});
                Assertions.assertEquals(1, responseList.getData().size(), "至少有一条记录");
                Assertions.assertEquals(0, responseList.getData().get(0).getWarnLevel(), "应当返回不报警状态");
        }

        @Test
        public void testWarnWithNoWarnId() throws Exception {
                // 测试单个信号不指定规则，四条规则中只有两条规则能匹配上
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setCarId(2L);  // 车编号1对应铁锂电池
                // 模拟电压差等于2，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("2.5"), new BigDecimal("0.5"), new BigDecimal("0.5"), new BigDecimal("0.5")));
                warnDTOS.add(warnDTO);
                MvcResult result = mockMvc.perform(post("/api/warn")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTOS)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("responseList: {}", respnse);
                ResponseResult<List<WarnDTO>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarnDTO>>>() {});
                Assertions.assertEquals(2, responseList.getData().size(), "至少有两条记录");
                List<WarnDTO> currentCollect = responseList.getData().stream().filter(dto -> "CURRENT".equals(dto.getSignalType())).collect(Collectors.toList());
                List<WarnDTO> voltageCollect = responseList.getData().stream().filter(dto -> "VOLTAGE".equals(dto.getSignalType())).collect(Collectors.toList());
                Assertions.assertEquals(1, currentCollect.size(), "至少有有一条电流记录");
                Assertions.assertEquals(1, voltageCollect.size(), "至少有有一条电压记录");
                Assertions.assertEquals(-1, currentCollect.get(0).getWarnLevel(), "电流不报警");
                Assertions.assertEquals(0, voltageCollect.get(0).getWarnLevel(), "电压报警等级应为0");
        }


        @Test
        public void testWarnMulti() throws Exception {
                // 测试多个信号
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setCarId(1L);  // 车编号1对应三元电池
                warnDTO.setWarnId(1L); // 车编号1对应三元电池电压差报警
                // 模拟电压差等于5，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("5.5"), new BigDecimal("0.5"), "VOLTAGE"));
                warnDTOS.add(warnDTO);

                warnDTO = new WarnDTO();
                warnDTO.setCarId(2L);  // 车编号1对应铁锂电池
                warnDTO.setWarnId(4L); // 车编号1对应铁锂电池电流差报警
                // 模拟电流差等于5，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("5.5"), new BigDecimal("0.5"), "CURRENT"));
                warnDTOS.add(warnDTO);


                MvcResult result = mockMvc.perform(post("/api/warn")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTOS)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("responseList: {}", respnse);
                ResponseResult<List<WarnDTO>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarnDTO>>>() {});
                Assertions.assertEquals(2, responseList.getData().size(), "至少有两条记录");
                List<WarnDTO> currentCollect = responseList.getData().stream().filter(dto -> "CURRENT".equals(dto.getSignalType())).collect(Collectors.toList());
                List<WarnDTO> voltageCollect = responseList.getData().stream().filter(dto -> "VOLTAGE".equals(dto.getSignalType())).collect(Collectors.toList());
                Assertions.assertEquals(1, currentCollect.size(), "至少有有一条电流记录");
                Assertions.assertEquals(1, voltageCollect.size(), "至少有有一条电压记录");
                Assertions.assertEquals(0, currentCollect.get(0).getWarnLevel(), "电流不报警");
                Assertions.assertEquals(0, voltageCollect.get(0).getWarnLevel(), "电压报警等级应为0");
        }

        @Test
        public void testWarnMulti2() throws Exception {
                // 测试多个信号，两个信号都不指定规则编号
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setCarId(1L);  // 车编号1对应三元电池
                // 模拟电压差等于5，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("5.5"), new BigDecimal("0.5"), "VOLTAGE"));
                warnDTOS.add(warnDTO);

                warnDTO = new WarnDTO();
                warnDTO.setCarId(2L);  // 车编号2对应铁锂电池
                // 模拟电流差等于5，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("5.5"), new BigDecimal("0.5"), "CURRENT"));
                warnDTOS.add(warnDTO);


                MvcResult result = mockMvc.perform(post("/api/warn")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTOS)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("responseList: {}", respnse);
                ResponseResult<List<WarnDTO>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarnDTO>>>() {});
                Assertions.assertEquals(4, responseList.getData().size(), "至少有两条记录");
                List<WarnDTO> currentCollect = responseList.getData().stream().filter(dto -> "CURRENT".equals(dto.getSignalType())).collect(Collectors.toList());
                List<WarnDTO> voltageCollect = responseList.getData().stream().filter(dto -> "VOLTAGE".equals(dto.getSignalType())).collect(Collectors.toList());
                Assertions.assertEquals(2, currentCollect.size(), "至少有有一条电流记录");
                Assertions.assertEquals(2, voltageCollect.size(), "至少有有一条电压记录");

                List<WarnDTO> filteredCurrentCollect = currentCollect.stream().filter(dto -> null != dto.getWarnLevel()).collect(Collectors.toList());
                List<WarnDTO> filteredVoltageCollect = voltageCollect.stream().filter(dto -> null != dto.getWarnLevel()).collect(Collectors.toList());
                Assertions.assertEquals(1, filteredCurrentCollect.size(), "至少有有一条电流记录");
                Assertions.assertEquals(1, filteredVoltageCollect.size(), "至少有有一条电压记录");
                Assertions.assertEquals(0, filteredVoltageCollect.get(0).getWarnLevel(), "电流报警等级应为0");
                Assertions.assertEquals(0, filteredCurrentCollect.get(0).getWarnLevel(), "电压报警等级应为0");
        }


        @Test
        public void testWarnMulti3() throws Exception {
                // 测试多个信号，两个信号都不指定规则编号, 二个信号指定规则
                List<WarnDTO> warnDTOS = new ArrayList<>();
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setCarId(1L);  // 车编号1对应三元电池
                // 模拟电压差等于5，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("5.5"), new BigDecimal("0.5"), "VOLTAGE"));
                warnDTOS.add(warnDTO);

                warnDTO = new WarnDTO();
                warnDTO.setCarId(2L);  // 车编号2对应铁锂电池
                // 模拟电流差等于5，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("5.5"), new BigDecimal("0.5"), "CURRENT"));
                warnDTOS.add(warnDTO);

                warnDTO = new WarnDTO();
                warnDTO.setCarId(3L);  // 车编号3对应三元电池
                warnDTO.setWarnId(1L); // 指定规则编号1为三元电池电压差报警
                // 模拟电压差等于5，应该输出的报警等级为0
                warnDTO.setSignal(new SignalData(new BigDecimal("5.5"), new BigDecimal("0.5"), "VOLTAGE"));
                warnDTOS.add(warnDTO);

                warnDTO = new WarnDTO();
                warnDTO.setCarId(3L);  // 车编号3对应三元电池
                warnDTO.setWarnId(3L); // 指定规则编号1为三元电池电流差报警
                // 模拟电压差等于0.5，应该输出的报警等级为1
                warnDTO.setSignal(new SignalData(new BigDecimal("1.0"), new BigDecimal("0.5"), "CURRENT"));
                warnDTOS.add(warnDTO);


                MvcResult result = mockMvc.perform(post("/api/warn")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTOS)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("responseList: {}", respnse);
                ResponseResult<List<WarnDTO>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarnDTO>>>() {});
                Assertions.assertEquals(6, responseList.getData().size(), "至少有两条记录");
                List<WarnDTO> currentCollect = responseList.getData().stream().filter(dto -> "CURRENT".equals(dto.getSignalType())).collect(Collectors.toList());
                List<WarnDTO> voltageCollect = responseList.getData().stream().filter(dto -> "VOLTAGE".equals(dto.getSignalType())).collect(Collectors.toList());
                Assertions.assertEquals(3, currentCollect.size(), "至少有有3条电流记录");
                Assertions.assertEquals(3, voltageCollect.size(), "至少有有3条电压记录");

                List<WarnDTO> filteredCurrentCollect = currentCollect.stream().filter(dto -> null != dto.getWarnLevel()).collect(Collectors.toList());
                List<WarnDTO> filteredVoltageCollect = voltageCollect.stream().filter(dto -> null != dto.getWarnLevel()).collect(Collectors.toList());
                Assertions.assertEquals(2, filteredCurrentCollect.size(), "至少有有2条电流记录");
                Assertions.assertEquals(2, filteredVoltageCollect.size(), "至少有有1条电压记录");
                Assertions.assertEquals(0, filteredCurrentCollect.get(0).getWarnLevel(), "电流报警等级应为0");
                Assertions.assertEquals(2, filteredCurrentCollect.get(1).getWarnLevel(), "电流报警等级应为2");
                Assertions.assertEquals(0, filteredVoltageCollect.get(0).getWarnLevel(), "电压报警等级应为0");
                Assertions.assertEquals(0, filteredVoltageCollect.get(1).getWarnLevel(), "电压报警等级应为0");
        }

        @Test
        public void testDelete() throws Exception {
                // 删除第一条规则
                List<Long> warnIds = new ArrayList<>();
                warnIds.add(1L);
                MvcResult result = mockMvc.perform(post("/api/deleteRules")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnIds)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("result: {}", respnse);
                ResponseResult<Object> responseResult = objectMapper.readValue(respnse, new TypeReference<ResponseResult<Object>>() {
                });
                // 校验顺序与传入的 warnIds 是否一致
                Assertions.assertEquals(200, responseResult.getStatus(), "Returned IDs do not match input IDs in order");

        }

        @Test
        public void testUpdate() throws Exception {
                // 更新第二条规则
                WarnDTO warnDTO = new WarnDTO();
                warnDTO.setWarnId(4L);
                warnDTO.setRuleNumber(2);
                warnDTO.setRuleName("电流差报警");
                warnDTO.setBatteryType(2);
                warnDTO.setSignalType("CURRENT");
                String modifiedExpression = "(Ix - Ii) < 0.7";
                warnDTO.setRuleConfig("[\n" + "  {\n" + "    \"expression\": \"(Ix - Ii) >= 1\",\n" + "    \"warnLevel\": 0\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) >= 0.5 && (Ix - Ii) < 1\",\n" + "    \"warnLevel\": 1\n" + "  },\n" + "  {\n"
                                + "    \"expression\": \"(Ix - Ii) >= 0.2 && (Ix - Ii) < 0.5\",\n" + "    \"warnLevel\": 2\n" + "  },\n" + "  {\n"
                                + "    \"expression\":" + "\"" + modifiedExpression + "\",\n" + "    \"warnLevel\": -1\n" + "  }\n" + "]\n");
                MvcResult result = mockMvc.perform(post("/api/updateRules")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnDTO)))
                                .andExpect(status().isOk()).andReturn();
                String respnse = result.getResponse().getContentAsString();
                log.info("result: {}", respnse);

                List<Long> warnIds = new ArrayList<>();
                warnIds.add(4L);
                result = mockMvc.perform(post("/api/getWarnRules")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(warnIds)))
                                .andExpect(status().isOk()).andReturn();
                respnse = result.getResponse().getContentAsString();
                log.info("result: {}", respnse);
                ResponseResult<List<WarningRule>> responseList = objectMapper.readValue(respnse, new TypeReference<ResponseResult<List<WarningRule>>>() {
                });

                Assertions.assertEquals(1, responseList.getData().size(), "Returned IDs do not match input IDs in order");
                WarningRule rule = responseList.getData().get(0);

                List<RuleItem> ruleItems = objectMapper.readValue(rule.getRuleConfig(), new TypeReference<List<RuleItem>>() {
                });
                RuleItem ruleItem = ruleItems.get(ruleItems.size() - 1);
                Assertions.assertEquals(modifiedExpression, ruleItem.getExpression(), "Returned IDs do not match input IDs in order");
                log.info("rule: {}", rule);
        }
}
