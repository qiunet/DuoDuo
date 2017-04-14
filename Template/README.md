
## 一个模板生成文件的工具
#### VmElement
    是xml解析的基类. 
    这个工具接受这个格式:
    <base baseDir="" vmfilePath="" filePostfix="">
        <other></other>
        <sub name="">
        </sub>
    </base>
    
    base 是一个VmElement. 必须指定  baseDir="" vmfilePath="" filePostfix="" 这三个属性
    分别为: 基础路径, vmfile在classpath的路径. 和生成文件后缀
    
    在subVmElement 里面, 可以得到 (base) 对象, 并进行一些操作. 
    也可以得到初始化时候指定的Map. 使用getParam(String key)
     
    如果需要<other></other> , 则需要自己继承VmElement 实现自己的 VmElement.
    例如: BeanVmElement
    
#### 测试
    模拟生成 QiunetDatas 需要的po 和 entityInfo
    测试总类: TemplateTest , 然后会按照游戏大部分场景适用的4种情况 来生成po 以及entityINfo.
    