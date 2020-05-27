package io.lrcores.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lrcores.modules.sys.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典
 *
 */
@Mapper
public interface SysDictDao extends BaseMapper<SysDictEntity> {

}
