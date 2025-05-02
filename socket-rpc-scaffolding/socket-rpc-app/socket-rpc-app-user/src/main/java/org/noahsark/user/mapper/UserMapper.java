package org.noahsark.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.noahsark.user.pojo.po.User;


@Mapper
@CacheNamespace
public interface UserMapper extends BaseMapper<User> {

}