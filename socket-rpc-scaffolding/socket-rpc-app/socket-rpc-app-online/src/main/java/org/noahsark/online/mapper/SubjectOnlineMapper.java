package org.noahsark.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.noahsark.online.pojo.po.SubjectOnline;


/**
 * <p>
 * 用户在线 Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
@Mapper
@CacheNamespace
public interface SubjectOnlineMapper extends BaseMapper<SubjectOnline> {

}