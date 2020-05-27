package io.lrcores.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lrcores.common.utils.Query;
import io.lrcores.modules.oss.dao.SysOssDao;
import io.lrcores.modules.oss.entity.SysOssEntity;
import io.lrcores.modules.oss.service.SysOssService;
import io.lrcores.common.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SysOssEntity> page = this.page(
			new Query<SysOssEntity>().getPage(params)
		);

		return new PageUtils(page);
	}

}
