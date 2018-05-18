package org.qiunet.project.init;

/***
 *
 * 模板生成文件的配置类. 指出配置文件在哪.
 * @Author qiunet
 * @Date Create in 2018/5/18 16:50
 **/
public interface IProjectInitConfig {
	/***
	 * 得到基础路径在哪. src上一级目录
	 * @return
	 */
	String getBasePath();

	/***
	 * 得到entity配置xml的路径. 基于classpath
	 * @return
	 */
	String getEntityXmlPath();

	/***
	 * 得到entity info配置xml的路径. 基于classpath
	 * @return
	 */
	String getEntityInfoXmlPath();

	/***
	 * 得到Mybatis Config 配置xml的路径. 基于classpath
	 * @return
	 */
	String getMybatisConfigXmlPath();

	/***
	 * 得到Mybatis Mapping配置xml的路径. 基于classpath
	 * @return
	 */
	String getMabatisMappingXmlPath();
}
