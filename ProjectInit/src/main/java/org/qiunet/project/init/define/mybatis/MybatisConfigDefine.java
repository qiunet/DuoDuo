package org.qiunet.project.init.define.mybatis;

import org.qiunet.project.init.define.ITemplateObjectDefine;
import org.qiunet.project.init.util.InitProjectUtil;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/***
 *
 *
 * qiunet
 * 2019-08-19 15:51
 ***/
public class MybatisConfigDefine implements ITemplateObjectDefine {
	/**
	 * 自动匹配configDir下面的xml文件
	 */
	private boolean autoMatchConfigFile = true;
	private String fileName = "mybatis-config.xml";
	private String baseDir = "src/main/resources";
	private String configDir = "mybatis";

	private final Set<String> files = new HashSet<>();
	private final List<String> aliasPackages = new ArrayList<>();

	public void addExtraFile(String extraFileName) {
		this.files.add(extraFileName);
	}

	public void addExtraFile(MybatisExtraDefine define) {
		this.addExtraFile(define.getFile());
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public boolean isAutoMatchConfigFile() {
		return autoMatchConfigFile;
	}

	public void setAutoMatchConfigFile(boolean autoMatchConfigFile) {
		this.autoMatchConfigFile = autoMatchConfigFile;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getConfigDir() {
		return configDir;
	}

	public void setConfigDir(String configDir) {
		this.configDir = configDir;
	}

	public List<String> getFiles() {
		if (isAutoMatchConfigFile()) {
			URL resource = Thread.currentThread().getContextClassLoader().getResource(this.configDir);
			if (resource != null) {
				File file = new File(resource.getFile());
				for (File f : Objects.requireNonNull(file.listFiles())) {
					this.addExtraFile(f.getName());
				}
				this.files.remove(this.fileName);
			}
		}
		return files.stream().sorted().collect(Collectors.toList());
	}

	public void addAliasPackage(String packageName) {
		this.aliasPackages.add(packageName);
	}

	public List<String> getAliasPackages() {
		return aliasPackages;
	}

	@Override
	public Path outputPath() {
		return Paths.get(InitProjectUtil.getRealUserDir().getAbsolutePath(), baseDir, configDir);
	}
}
