package org.qiunet.utils.groovy;

import java.util.Map;

/***
 * groovy 是通过加载class 来执行groovy的. 所以尽量不要重复加载.
 * 就是不要执行一次 new 一次这个 IGroovyRun 对象.
 * 出现结果是 class处理占用cpu过多. 1.7以下还会占满 perm空间
 *
 * @Author qiunet
 * @Date Create in 2018/6/25 15:11
 **/
public interface IGroovyRun<T> {
    /***
     * 运行groovy得到返回.
     * @return
     */
    T run(Map<String, Object> params);
    /***
     * 直接调用指定方法
     * @param methodName
     * @param args
     * @return
     */
    T invokeMethod(String methodName, Object... args);
}
