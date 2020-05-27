package io.lrcores.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.lrcores.modules.sys.entity.SysDictEntity;
import io.lrcores.common.utils.PageUtils;

import java.util.Map;

/**
 * 数据字典
 *
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

