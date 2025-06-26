package com.xiaomi.cug.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.xiaomi.cug.dto.BatterySignalDTO;
import com.xiaomi.cug.dto.ResponseResult;
import com.xiaomi.cug.dto.WarnDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j @SpringBootTest
@AutoConfigureMockMvc
public class SignalControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void testUploadSignal() throws Exception {
                BatterySignalDTO signal = new BatterySignalDTO();
                signal.setVid("VH234K9L8M1N5P7Q");
                signal.setChassisNumber(1L);
                signal.setSignalData("{\"mi\":4.2,\"mx\": 4.2}");
                signal.setReportTime(LocalDateTime.now());
                signal.setCreatedTime(LocalDateTime.now());
                String requestJSON = objectMapper.writeValueAsString(signal);
                MvcResult result =  mockMvc.perform(post("/signal/upload")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestJSON))
                                .andExpect(status().isOk()).andReturn();
                String contentAsString = result.getResponse().getContentAsString();
                log.info("Request: " + requestJSON);
                ResponseResult<String> responseList = objectMapper.readValue(contentAsString, new TypeReference<ResponseResult<String>>() {});
                Assertions.assertEquals(200, responseList.getStatus(), "写入成功");
                log.info("Response: " + contentAsString);
        }


        @Test
        public void testInsertToDb1() throws Exception {
                BatterySignalDTO signal = new BatterySignalDTO();
                signal.setVid("VH567R2S3T4U8W9X");
                signal.setChassisNumber(2L);
                signal.setSignalData("{\"mi\": 4.1,\"mx\": 4.2}");
                signal.setReportTime(LocalDateTime.now());
                signal.setCreatedTime(LocalDateTime.now());
                MvcResult result =  mockMvc.perform(post("/signal/upload")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(signal)))
                                .andExpect(status().isOk()).andReturn();
                String contentAsString = result.getResponse().getContentAsString();
                ResponseResult<String> responseList = objectMapper.readValue(contentAsString, new TypeReference<ResponseResult<String>>() {});
                Assertions.assertEquals(200, responseList.getStatus(), "写入成功");
        }

        @Test
        public void testQuerySignal() throws Exception {
                BatterySignalDTO queryDTO = new BatterySignalDTO();
                queryDTO.setChassisNumber(1L);
                queryDTO.setVid("VH234K9L8M1N5P7Q");
                queryDTO.setReportTime(LocalDateTime.now().plusDays(-5));
                mockMvc.perform(post("/signal/query")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(queryDTO)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        public void testQuerySignalWithResponse() throws Exception {
                BatterySignalDTO queryDTO = new BatterySignalDTO();
                queryDTO.setChassisNumber(1L);
                queryDTO.setReportTime(LocalDateTime.now().plusDays(-5));
                String requestJSON = objectMapper.writeValueAsString(queryDTO);
                MvcResult result = mockMvc.perform(post("/signal/query")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestJSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();
                String content = result.getResponse().getContentAsString();
                SignalControllerTest.log.info("Request: " + requestJSON);
                SignalControllerTest.log.info("Response 1: " + content);

                // 从redis中获取
                result = mockMvc.perform(post("/signal/query")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(queryDTO)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();
                content = result.getResponse().getContentAsString();
                SignalControllerTest.log.info("Response 2: " + content);
        }

        @Test
        public void testDeleteSignal() throws Exception {
                String dateTimeStr = "2025-06-25 12:09:44";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, formatter);
                BatterySignalDTO dto = new BatterySignalDTO();
                dto.setChassisNumber(1L);
                dto.setVid("VH234K9L8M1N5P7Q");
                dto.setReportTime(localDateTime);
                mockMvc.perform(post("/signal/delete")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("success"));
        }

        @Test
        public void testUpdateSignal() throws Exception {
                String dateTimeStr = "2025-06-25 17:10:52";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, formatter);
                BatterySignalDTO dto = new BatterySignalDTO();
                dto.setChassisNumber(1L);
                dto.setVid("VH234K9L8M1N5P7Q");
                dto.setReportTime(localDateTime);
                dto.setSignalData("{\"mi\": 4.1,\"mx\": 4.2}");

                mockMvc.perform(post("/signal/update")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("success"));
        }


}
