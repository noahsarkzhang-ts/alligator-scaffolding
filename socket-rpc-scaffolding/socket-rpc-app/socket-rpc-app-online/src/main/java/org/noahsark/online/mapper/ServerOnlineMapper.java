package org.noahsark.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.noahsark.online.pojo.po.ServerOnline;


/**
 * <p>
 * 网关在线 Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
@Mapper
@CacheNamespace
public interface ServerOnlineMapper extends BaseMapper<ServerOnline> {

}