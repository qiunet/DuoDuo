package org.qiunet.utils.args;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by qiunet.
 * 参数解析工具类
 * 参数格式必须为（--key=value）这种形式
 *
 * 17/10/10
 */
public class ArgsMapping {

	private static final Logger logger = LoggerType.DUODUO.getLogger();
	/**
	 * 命令描述
	 */
	private final List<ArgsDesc> argsDescs = Lists.newLinkedList();
	/**
	 * 命令参数
	 */
	private final Map<String, String> argsInfos = Maps.newHashMap();

	public ArgsMapping(String [] args) {
		if(args != null && args.length>0){
			for(String s : args){
				if (s == null ||
				(!s.startsWith("-") && !s.startsWith("--"))
				|| !s.contains("=")) {
					continue;
				}

				try {
					String[] tt = StringUtil.split(s, "=");
					if (s.startsWith("--")) {
						argsInfos.put(tt[0].substring(2).trim(), tt[1].trim());
					}else {
						argsInfos.put(tt[0].substring(1).trim(), tt[1].trim());
					}
				} catch (Exception e) {
					logger.warn("［"+s+"］参数异常", e);
				}
			}
		}
		if(logger.isInfoEnabled()){
			logger.info("Jvm 启动参数: " + JsonUtil.toJsonString(argsInfos));
		}
	}

	/**
	 * 添加命令描述
	 * @param shortArgsName 短名 前面一个 -
	 * @param fullArgsName 全名 前面两个 -
	 * @param desc 描述
	 * @return
	 */
	public ArgsMapping addArgsDesc(String shortArgsName, String fullArgsName, String desc) {
		Preconditions.checkState(!StringUtil.isEmpty(shortArgsName) && !StringUtil.isEmpty(fullArgsName));
		Preconditions.checkState(getArgsDesc(shortArgsName) == null && getArgsDesc(fullArgsName) == null);
		this.argsDescs.add(new ArgsDesc(shortArgsName, fullArgsName, desc));
		return this;
	}

	/**
	 * 获得参数值
	 * @param argsName
	 * @return
	 */
	public int getIntValue(String argsName) {
		return getIntValue(argsName, 0);
	}

	public int getIntValue(String argsName, int defaultVal) {
		String value = getValue(argsName);
		if (value == null) {
			return defaultVal;
		}
		return Integer.parseInt(value);
	}

	public String getValue(String argsName, String defaultVal) {
		String value = getValue(argsName);
		if (value == null) {
			return defaultVal;
		}
		return value;
	}

	public String getValue(String argsName) {
		if (argsName.startsWith("--")) {
			argsName = argsName.substring(2);
		}else if (argsName.startsWith("-")) {
			argsName = argsName.substring(1);
		}

		if ("h".equals(argsName) || "help".equals(argsName)) {
			this.usage();
			return null;
		}
		ArgsDesc commandDesc = getArgsDesc(argsName);
		if (commandDesc == null) {
			return argsInfos.get(argsName);
		}

		String result = argsInfos.get(commandDesc.shortArgsName);
		if (result != null) {
			return result;
		}

		return argsInfos.get(commandDesc.fullArgsName);
	}

	/**
	 * 打印usage
	 */
	private void usage() {
		System.out.println("USAGE:");
		if (argsDescs.isEmpty()) {
			System.out.println("\t\t没有定义说明!");
		}
		for (ArgsDesc desc : argsDescs) {
			System.out.printf("\t\t-%s --%s\t\t%s\n", desc.shortArgsName, desc.fullArgsName, desc.desc);
		}
	}

	/**
	 * 获得命令描述
 	 * @param argsName
	 * @return
	 */
	public ArgsDesc getArgsDesc(String argsName) {
		for (ArgsDesc desc : argsDescs) {
			if (desc.fullArgsName.equals(argsName) || desc.shortArgsName.equals(argsName)) {
				return desc;
			}
		}
		return null;
	}

	private static class ArgsDesc {

		private final String shortArgsName;

		private final String fullArgsName;

		private final String desc;

		public ArgsDesc(String shortArgsName, String fullArgsName, String desc) {
			this.shortArgsName = shortArgsName;
			this.fullArgsName = fullArgsName;
			this.desc = desc;
		}
	}
}
