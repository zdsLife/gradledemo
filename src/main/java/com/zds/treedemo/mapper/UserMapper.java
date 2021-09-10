package com.zds.treedemo.mapper;

import com.zds.treedemo.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<UserDO>, MySqlMapper<UserDO> {

}
