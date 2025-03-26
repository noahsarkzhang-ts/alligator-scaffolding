package org.noahsark.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.noahsark.online.pojo.po.SubjectLoginEvent;


/**
 * <p>
 * 用户登陆事件 Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2024-05-27
 */
@Mapper
@CacheNamespace
public interface SubjectLoginEventMapper extends BaseMapper<SubjectLoginEvent> {

}