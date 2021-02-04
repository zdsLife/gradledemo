package com.zds.treedemo.mapper;

import com.zds.treedemo.domain.SysLogDO;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Mapper
public interface SysLogMapper extends tk.mybatis.mapper.common.Mapper<SysLogDO>, MySqlMapper<SysLogDO> {

}
