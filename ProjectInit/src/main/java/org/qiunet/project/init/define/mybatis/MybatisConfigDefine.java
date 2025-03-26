package org.qiunet.project.init.define.mybatis;

import org.qiunet.project.init.define.ITemplateObjectDefine;
import org.qiunet.project.init.util.InitProjectUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/***
 *
 *
 * qiunet
 * 2019-08-19 15:51
 ***/
public class MybatisConfigDefine implements ITemplateObjectDefine {
	private String fileName = "mybatis-config.xml";
	private String baseDir = "src/main/resources";
	private String configDir = "mybatis";

	private Set<String> files = new HashSet<>();
	private List<String> aliasPackages = new ArrayList<>();

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
		return files.stream().sorted().collect(Collectors.toList());
	}

	public void addAliasPackage(String packageName) {
		this.aliasPackages.add(packageName);
	}

	public void addAliasPackage(MybatisExtraPkgDefine define) {
		this.aliasPackages.add(define.getPkg());
	}

	public List<String> getAliasPackages() {
		return aliasPackages;
	}

	@Override
	public Path outputPath() {
		return Paths.get(InitProjectUtil.getRealUserDir().getAbsolutePath(), baseDir, configDir);
	}
}
