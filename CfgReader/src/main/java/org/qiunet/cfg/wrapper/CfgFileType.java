package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.json.NestListJsonCfgManager;
import org.qiunet.cfg.manager.json.NestMapJsonCfgManager;
import org.qiunet.cfg.manager.json.SimpleMapJsonCfgManager;
import org.qiunet.cfg.manager.xd.NestListXdCfgManager;
import org.qiunet.cfg.manager.xd.NestMapXdCfgManager;
import org.qiunet.cfg.manager.xd.SimpleMapXdCfgManager;
import org.qiunet.cfg.manager.xml.NestListXmlCfgManager;
import org.qiunet.cfg.manager.xml.NestMapXmlCfgManager;
import org.qiunet.cfg.manager.xml.SimpleMapXmlCfgManager;
import org.qiunet.utils.exceptions.EnumParseException;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 16:29
 ***/
public enum  CfgFileType {
	XML {
		@Override
		public <ID, Cfg extends ISimpleMapCfg<ID>> ISimpleMapCfgWrapper<ID, Cfg> createSimpleMapCfgWrapper(Class<Cfg> clazz) {
			SimpleMapXmlCfgManager<ID, Cfg> cfgManager = new SimpleMapXmlCfgManager<>(clazz);
			return new SimpleMapCfgWrapper<>(cfgManager);
		}

		@Override
		public <ID, SubId, Cfg extends INestMapCfg<ID, SubId>> INestMapCfgWrapper<ID, SubId, Cfg> createNestMapCfgWrapper(Class<Cfg> clazz) {
			NestMapXmlCfgManager<ID, SubId, Cfg> cfgManager = new NestMapXmlCfgManager<>(clazz);
			return new NestMapCfgWrapper<>(cfgManager);
		}

		@Override
		public <ID, Cfg extends INestListCfg<ID>> INestListCfgWrapper<ID, Cfg> createNestListCfgWrapper(Class<Cfg> clazz) {
			NestListXmlCfgManager<ID, Cfg> cfgManager = new NestListXmlCfgManager<>(clazz);
			return new NestListCfgWrapper<>(cfgManager);
		}
	},

	XD {
		@Override
		public <ID, Cfg extends ISimpleMapCfg<ID>> ISimpleMapCfgWrapper<ID, Cfg> createSimpleMapCfgWrapper(Class<Cfg> clazz) {
			SimpleMapXdCfgManager<ID, Cfg> cfgManager = new SimpleMapXdCfgManager<>(clazz);
			return new SimpleMapCfgWrapper<>(cfgManager);
		}

		@Override
		public <ID, SubId, Cfg extends INestMapCfg<ID, SubId>> INestMapCfgWrapper<ID, SubId, Cfg> createNestMapCfgWrapper(Class<Cfg> clazz) {
			NestMapXdCfgManager<ID, SubId, Cfg> cfgManager = new NestMapXdCfgManager<>(clazz);
			return new NestMapCfgWrapper<>(cfgManager);
		}

		@Override
		public <ID, Cfg extends INestListCfg<ID>> INestListCfgWrapper<ID, Cfg> createNestListCfgWrapper(Class<Cfg> clazz) {
			NestListXdCfgManager<ID, Cfg> cfgManager = new NestListXdCfgManager<>(clazz);
			return new NestListCfgWrapper<>(cfgManager);
		}
	},

	JSON {
		@Override
		public <ID, Cfg extends ISimpleMapCfg<ID>> ISimpleMapCfgWrapper<ID, Cfg> createSimpleMapCfgWrapper(Class<Cfg> clazz) {
			SimpleMapJsonCfgManager<ID, Cfg> cfgManager = new SimpleMapJsonCfgManager<>(clazz);
			return new SimpleMapCfgWrapper<>(cfgManager);
		}

		@Override
		public <ID, SubId, Cfg extends INestMapCfg<ID, SubId>> INestMapCfgWrapper<ID, SubId, Cfg> createNestMapCfgWrapper(Class<Cfg> clazz) {
			NestMapJsonCfgManager<ID, SubId, Cfg> cfgManager = new NestMapJsonCfgManager<>(clazz);
			return new NestMapCfgWrapper<>(cfgManager);
		}

		@Override
		public <ID, Cfg extends INestListCfg<ID>> INestListCfgWrapper<ID, Cfg> createNestListCfgWrapper(Class<Cfg> clazz) {
			NestListJsonCfgManager<ID, Cfg> cfgManager = new NestListJsonCfgManager<>(clazz);
			return new NestListCfgWrapper<>(cfgManager);
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
