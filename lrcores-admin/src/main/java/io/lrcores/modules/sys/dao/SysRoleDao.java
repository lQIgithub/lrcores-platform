package io.lrcores.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lrcores.modules.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色管理
 *
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {


}
