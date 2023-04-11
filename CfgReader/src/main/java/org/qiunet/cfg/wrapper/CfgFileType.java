package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.base.INestListCfgWrapper;
import org.qiunet.cfg.manager.base.INestMapCfgWrapper;
import org.qiunet.cfg.manager.base.ISimpleMapCfgWrapper;
import org.qiunet.cfg.manager.json.NestListJsonCfgManager;
import org.qiunet.cfg.manager.json.NestMapJsonCfgManager;
import org.qiunet.cfg.manager.json.SimpleMapJsonCfgManager;
import org.qiunet.utils.exceptions.EnumParseException;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 16:29
 ***/
public enum  CfgFileType {

	JSON {
		@Override
		public <ID, Cfg extends ISimpleMapCfg<ID>> ISimpleMapCfgWrapper<ID, Cfg> createSimpleMapCfgWrapper(Class<Cfg> clazz) {
			return new SimpleMapJsonCfgManager<>(clazz);
		}

		@Override
		public <ID, SubId, Cfg extends INestMapCfg<ID, SubId>> INestMapCfgWrapper<ID, SubId, Cfg> createNestMapCfgWrapper(Class<Cfg> clazz) {
			return new NestMapJsonCfgManager<>(clazz);
		}

		@Override
		public <ID, Cfg extends INestListCfg<ID>> INestListCfgWrapper<ID, Cfg> createNestListCfgWrapper(Class<Cfg> clazz) {
			return new NestListJsonCfgManager<>(clazz);
		}
	},
	;

	 abstract <ID, Cfg extends ISimpleMapCfg<ID>> ISimpleMapCfgWrapper<ID, Cfg> createSimpleMapCfgWrapper(Class<Cfg> clazz);

	 abstract <ID, SubId, Cfg extends INestMapCfg<ID, SubId>> INestMapCfgWrapper<ID, SubId, Cfg> createNestMapCfgWrapper(Class<Cfg> clazz);

	 abstract <ID, Cfg extends INestListCfg<ID>> INestListCfgWrapper<ID, Cfg> createNestListCfgWrapper(Class<Cfg> clazz);

	 static CfgFileType parse(String fileName) {
		 int indexOf = fileName.lastIndexOf(".");
		 if (indexOf > 0) {
			String postfix = fileName.substring(indexOf + 1);
			for (CfgFileType type : values()) {
				if (type.name().equalsIgnoreCase(postfix)) {
					return type;
				}
			}
		 }
		throw new EnumParseException(fileName);
	}
}
