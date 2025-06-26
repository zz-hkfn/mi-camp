package com.xiaomi.cug.mapper;

import com.xiaomi.cug.entity.VehicleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VehicleInfoMapper {
        @Select("SELECT id, battery_type FROM vehicle_info WHERE id = #{id}")
        VehicleInfo selectById(@Param("id") Long id);

}
