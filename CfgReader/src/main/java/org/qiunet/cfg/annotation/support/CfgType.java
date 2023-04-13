package org.qiunet.cfg.annotation.support;

import org.apache.commons.lang3.ClassUtils;
import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.base.ICfgWrapper;
import org.qiunet.utils.exceptions.EnumParseException;

import java.util.List;

/***
 * Cfg 的类型.
 *
 * @author qiunet
 * 2020-04-23 17:10
 ***/
enum  CfgType {
	SIMPLE_MAP(ISimpleMapCfg.class) {
		@Override
        ICfgWrapper getCfgWrapper(CfgFileType cfgFileType, Class<? extends ICfg> clazz) {
			return cfgFileType.createSimpleMapCfgWrapper((Class<ISimpleMapCfg>)clazz);
		}
	},
	NEST_LIST(INestListCfg.class) {
		@Override
		ICfgWrapper getCfgWrapper(CfgFileType cfgFileType, Class<? extends ICfg> clazz) {
			return cfgFileType.createNestListCfgWrapper((Class<INestListCfg>)clazz);
		}
	},
	NEST_MAP(INestMapCfg.class) {
		@Override
		ICfgWrapper getCfgWrapper(CfgFileType cfgFileType, Class<? extends ICfg> clazz) {
			return cfgFileType.createNestMapCfgWrapper((Class<INestMapCfg>)clazz);
		}
	},
	;
	private final Class<? extends ICfg> clazz;

	CfgType(Class<? extends ICfg> clazz) {
		this.clazz = clazz;
	}

	abstract ICfgWrapper getCfgWrapper(CfgFileType cfgFileType, Class<? extends ICfg> clazz);


	static ICfgWrapper createCfgWrapper(Class<? extends ICfg> clazz) {
		Cfg cfg = clazz.getAnnotation(Cfg.class);
		CfgFileType fileType = CfgFileType.parse(cfg.value());
		List<Class<?>> is = ClassUtils.getAllInterfaces(clazz);
		for (CfgType type : values()) {
			for (Class<?> i : is) {
				if (i == type.clazz){
					return type.getCfgWrapper(fileType, clazz);
				}
			}
		}
		throw new EnumParseException(clazz.getName());
	}
}
