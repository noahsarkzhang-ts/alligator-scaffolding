package org.noashark.app.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.noashark.app.user.pojo.po.User;


@Mapper
@CacheNamespace
public interface UserMapper extends BaseMapper<User> {

}