package io.lrcores.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.lrcores.modules.sys.entity.SysLogEntity;
import io.lrcores.common.utils.PageUtils;

import java.util.Map;


/**
 * 系统日志
 *
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
