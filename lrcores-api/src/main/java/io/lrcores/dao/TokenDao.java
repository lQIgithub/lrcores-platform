package io.lrcores.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lrcores.entity.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Token
 *
 */
@Mapper
public interface TokenDao extends BaseMapper<TokenEntity> {

}
