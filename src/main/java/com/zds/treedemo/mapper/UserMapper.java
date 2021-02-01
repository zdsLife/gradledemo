package com.zds.treedemo.mapper;

import com.zds.treedemo.domain.UserDO;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UserMapper extends tk.mybatis.mapper.common.Mapper<UserDO>, MySqlMapper<UserDO> {

}
