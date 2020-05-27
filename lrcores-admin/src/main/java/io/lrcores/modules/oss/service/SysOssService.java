package io.lrcores.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.lrcores.modules.oss.entity.SysOssEntity;
import io.lrcores.common.utils.PageUtils;

import java.util.Map;

/**
 * 文件上传
 *
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
