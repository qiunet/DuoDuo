package org.qiunet.cfg.wrapper;

import com.google.common.collect.Maps;
import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.utils.exceptions.EnumParseException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 17:10
 ***/
public enum  CfgType {
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
	private Class<? extends ICfg> clazz;

	CfgType(Class<? extends ICfg> clazz) {
		this.clazz = clazz;
	}

	abstract ICfgWrapper getCfgWrapper(CfgFileType cfgFileType, Class<? extends ICfg> clazz);


	private static final Map<Class, ICfgWrapper> cfgWrappers = Maps.newHashMap();
	public static void createCfgWrapper(Class<? extends ICfg> clazz) {
		Cfg cfg = clazz.getAnnotation(Cfg.class);
		CfgFileType fileType = CfgFileType.parse(cfg.value());
		for (CfgType type : values()) {
			for (Type genericInterface : clazz.getGenericInterfaces()) {
				if (((ParameterizedType) genericInterface).getRawType() == type.clazz){
					cfgWrappers.put(clazz, type.getCfgWrapper(fileType, clazz));
					return;
				}
			}
		}
		throw new EnumParseException(clazz.getName());
	}

	public static <T extends ICfgWrapper> T getCfgWrapper(Class clazz) {
		return (T) cfgWrappers.get(clazz);
	}
}
