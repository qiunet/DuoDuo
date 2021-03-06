package org.qiunet.excel2cfgs.appender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.qiunet.excel2cfgs.common.enums.RoleType;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by qiunet.
 * 17/10/30
 */
public class JsonAppender extends BaseAppender {
	protected Logger logger = LoggerType.DUODUO_CFG_READER.getLogger();

	public JsonAppender(File sourceFile, String relativeDirPath, String filePrefix) {
		super(sourceFile, relativeDirPath, filePrefix);
	}

	@Override
	public void createCfgFile(String sheetName, RoleType roleType, String outPath, AppenderAttachable attachable) {
		List<List<AppenderData>> appenderDatas = attachable.getAppenderDatas();

		Path path = Paths.get(outPath, outputRelativePath, filePrefix + "_" + sheetName + ".json");
		if (! path.toFile().getParentFile().exists()) {
			path.toFile().getParentFile().mkdirs();
		}

		JSONArray jsonArray = new JSONArray();
		for (List<AppenderData> rowDatas : appenderDatas) {
			JSONObject jsonObject = new JSONObject();
			for (AppenderData rowData : rowDatas) {
				if (rowData.getOutPutType().canWrite(roleType)) {
					switch (rowData.getDataType()){
						case INT:
							if(StringUtil.isEmpty(rowData.getVal())){
								logger.error("Excel int 类型 列没有数值");
								jsonObject.put(rowData.getName(), 0);
							}else{
								jsonObject.put(rowData.getName(), Integer.parseInt(rowData.getVal()));
							}
							break;
						case LONG:
							if(StringUtil.isEmpty(rowData.getVal())){
								logger.error("Excel long 类型 列没有数值");
								jsonObject.put(rowData.getName(), 0l);
							}else{
								jsonObject.put(rowData.getName(), Long.parseLong(rowData.getVal()));
							}
							break;
						case STRING:
							jsonObject.put(rowData.getName(), rowData.getVal());
							break;
					}
				}
			}
			jsonArray.add(jsonObject);
		}

		String content = JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat);
		FileUtil.createFileWithContent(path.toFile(), content);

		this.copyToProject(path.toFile());
	}

	@Override
	public void fileOver() {

	}

	@Override
	public String name() {
		return "json";
	}
}
