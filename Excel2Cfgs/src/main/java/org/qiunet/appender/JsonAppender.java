package org.qiunet.appender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.qiunet.frame.enums.RoleType;
import org.qiunet.utils.file.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by qiunet.
 * 17/10/30
 */
public class JsonAppender implements IAppender {

	private String filePrefix;

	/**
	 * 相对根目录的目录相对路径
	 */
	private String relativeDirPath;
	public JsonAppender(String relativeDirPath, String filePrefix) {
		this.relativeDirPath = relativeDirPath;
		this.filePrefix = filePrefix;

	}

	@Override
	public void createCfgFile(String sheetName, RoleType roleType, String outPath, AppenderAttachable attachable) {
		List<List<AppenderData>> appenderDatas = attachable.getAppenderDatas();
		if (appenderDatas.isEmpty()) {
			return;
		}

		Path path = Paths.get(outPath, relativeDirPath, filePrefix + "_" + sheetName + ".json");
		if (! path.toFile().getParentFile().exists()) {
			path.toFile().getParentFile().mkdirs();
		}

		JSONArray jsonArray = new JSONArray();
		for (List<AppenderData> rowDatas : appenderDatas) {
			JSONObject jsonObject = new JSONObject();
			for (AppenderData rowData : rowDatas) {
				if (rowData.getOutPutType().canWrite(roleType)) {
					jsonObject.put(rowData.getName(), rowData.getDataType().convert(rowData.getVal()));
				}
			}
			jsonArray.add(jsonObject);
		}

		String content = JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat);
		FileUtil.createFileWithContent(path.toFile(), content);
	}

	@Override
	public void fileOver() {

	}

	@Override
	public String name() {
		return "json";
	}
}
