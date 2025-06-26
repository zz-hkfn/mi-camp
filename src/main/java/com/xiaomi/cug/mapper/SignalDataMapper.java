package com.xiaomi.cug.mapper;

import com.xiaomi.cug.entity.BatterySignal;
import org.apache.ibatis.annotations.*;


import java.util.List;


@Mapper
public interface SignalDataMapper {

        @Insert("INSERT INTO battery_signal (vid, chassis_number, report_time, signal_data, created_time) " +
                "VALUES (#{vid}, #{chassisNumber}, #{reportTime}, #{signalData}, #{createdTime})")
        void insertBatterySignal(BatterySignal batterySignal);

        @Select("SELECT id, vid, chassis_number, signal_data, report_time, created_time " +
                "FROM battery_signal WHERE chassis_number = #{chassisNumber} AND report_time > #{reportTime} " +
                "ORDER BY  report_time DESC LIMIT 1 ")
        BatterySignal queryByVidAndDate(BatterySignal batterySignal);

        @Select("SELECT id, vid, chassis_number, signal_data, report_time, created_time, status " +
                        "FROM battery_signal WHERE status = ${status} " +
                        "ORDER BY  report_time DESC LIMIT 20 ")
        List<BatterySignal> queryByVidAndDateMulti(BatterySignal batterySignal);

        @Delete("DELETE FROM battery_signal WHERE chassis_number = #{chassisNumber} AND report_time = #{reportTime}")
        void deleteById(BatterySignal dto);

        @Update("UPDATE battery_signal SET signal_data = #{signalData} WHERE chassis_number = #{chassisNumber} AND report_time = #{reportTime}")
        void updateSignalData(BatterySignal batterySignal);

        @Update("UPDATE battery_signal SET status = #{status} WHERE id = #{id}")
        void updateSignalStatus(BatterySignal batterySignal);

}
