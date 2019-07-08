package org.qiunet.utils.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/***
 *
 * 不要重复 new 该类. 会出现严重问题. 最好保证是持有该对象 每次调用方法.
 *
 * @Author qiunet
 * @Date Create in 2018/6/25 15:16
 **/
public class GroovyRun<T> implements IGroovyRun<T> {
    /***
     * 类加载器
     */
    private static final GroovyClassLoader classLoader = new GroovyClassLoader();

    private Class<GroovyObject> scriptClass;

    public GroovyRun(String scriptText) {
        this.scriptClass = classLoader.parseClass(scriptText);
    }

    public GroovyRun(File file) {
        try {
            this.scriptClass = classLoader.parseClass(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GroovyRun(URL url) {
    	this(new File(url.getFile()));
	}

	public GroovyRun(URI uri) {
		this(new File(uri));
	}

    /***
     * 返回调用的 object
     * @return
     */
    private GroovyObject returnObject(){
        GroovyObject groovyObject = null;
        try {
            groovyObject = scriptClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
		return groovyObject;
    }

    @Override
    public T run(Map<String, Object> params) {
        GroovyObject groovyObject = returnObject();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                groovyObject.setProperty(entry.getKey(), entry.getValue());
            }
        }
        return (T) groovyObject.invokeMethod("run", null);
    }

    @Override
    public T invokeMethod(String methodName,Object... args) {
        GroovyObject groovyObject = returnObject();
        return (T) groovyObject.invokeMethod(methodName, args);
    }
}
