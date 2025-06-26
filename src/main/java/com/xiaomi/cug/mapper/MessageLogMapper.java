package com.xiaomi.cug.mapper;

import com.xiaomi.cug.entity.MessageLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageLogMapper {
        @Select("SELECT COUNT(1) FROM message_log WHERE transaction_id = #{transactionId}")
        boolean existsByTransactionId(MessageLog messageLog);

        @Insert("INSERT INTO message_log (chassis_number,transaction_id, status, created_time) VALUES (#{chassisNumber}, #{transactionId}, #{status}, #{createdTime})")
        void insert(MessageLog messageLog);
}
