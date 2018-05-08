package org.qiunet.utils.args;

import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiunet.
 * 参数解析工具类
 * 参数格式必须为（--key=value）这种形式
 *
 * 17/10/10
 */
public class ArgsMapping extends KeyValueData<String,String> {

	private Logger logger = LoggerFactory.getLogger(LoggerType.QIUNET_UTILS);

	public ArgsMapping(String [] args) {
		super(new HashMap<>());

		Map<String, String> temp = new HashMap<>();

		if(args!=null&&args.length>0){
			for(String s:args){
				if (!s.startsWith("--") || s.indexOf("=") == -1) continue;

				try {
					String tt[] = StringUtil.split(s, "=");
					temp.put(tt[0].substring(2).trim(), tt[1].trim());
				} catch (Exception e) {
					logger.warn("［"+s+"］参数异常", e);
				}
			}
		}
		if(logger.isInfoEnabled()){
			logger.info(JsonUtil.toJsonString(temp));
		}

		this.load(temp);
	}
}
