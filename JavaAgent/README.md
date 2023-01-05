# JavaAgent

--------------------------------

 - 热更替换运行的Class, 可以线上不停服修复bug.
	
## 注意点

* 热更Class时候, 也需要替换jar包. 以免重启后, 更新被还原.
* > 热更新部分class. 更的代码需要符合热更的[规范](https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/Instrumentation.html#redefineClasses-java.lang.instrument.ClassDefinition...-).
	> > The redefinition must not add, remove or rename fields or methods, change the signatures of methods, or change inheritance



