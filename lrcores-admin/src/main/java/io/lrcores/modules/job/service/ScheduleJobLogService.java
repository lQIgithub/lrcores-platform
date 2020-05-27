package io.lrcores.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.lrcores.modules.job.entity.ScheduleJobLogEntity;
import io.lrcores.common.utils.PageUtils;

import java.util.Map;

/**
 * 定时任务日志
 *
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
