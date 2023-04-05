# DuoDuo | [Wiki](https://github.com/qiunet/DuoDuo/wiki)
[![Java CI with Maven](https://github.com/qiunet/DuoDuo/actions/workflows/maven.yml/badge.svg)](https://github.com/qiunet/DuoDuo/actions/workflows/maven.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.qiunet/duoduo-all?logo=apache-maven&logoColor=white)](https://search.maven.org/artifact/io.github.qiunet/duoduo-all)
[![Java support](https://img.shields.io/badge/Java-17+-green?logo=java&logoColor=white)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/license-apache--2.0-green)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub release](https://img.shields.io/github/release/qiunet/DuoDuo)](https://github.com/qiunet/DuoDuo/releases)
[![GitHub Contributors](https://img.shields.io/github/contributors/qiunet/DuoDuo)](https://github.com/qiunet/DuoDuo/graphs/contributors)
	
## Wiki
[https://github.com/qiunet/DuoDuo/wiki](https://github.com/qiunet/DuoDuo/wiki)

## 协议支持
| 是否支持     | Tcp | Kcp | WebSocket | Http |
|----------|-----|-----|-----------|------|
| Protobuf | √   | √   | √         | √    |
| Json     | x   | x   | x         | √    |

## Git配置
 - 不对换行符做自动转换<br />
`git config --global core.autocrlf input`

## 模块简介
| 模块名                                 | 简介                                                     |
| -------------------------------------- | -------------------------------------------------------- |
| [Quartz](Quartz/README.md)             | 定时调度相关的模块                                       |
| [CfgReader](CfgReader/README.md)       | 配置文件读取的模块                                       |
| [QiunetUtils](QiunetUtils/README.md)   | 各种基本工具类                                           |
| [QiunetDatas](QiunetDatas/README.md)   | Mysql和Redis以及本地Cache使用模块, 实现了异步更新等功能. |
| [Entity2Table](Entity2Table/README.md) | 根据Do对象自动生成和更新数据库结构的模块                 |
| [ProjectInit](ProjectInit/README.md)   | 通过xml配置.自动生成Do Bo xml Service和调用的类          |
| [FlashHandler](FlashHandler/README.md) | 能启动Tcp Http WebSocket作为服务的模块                   |
| [GameTest](GameTest/README.md)         | 模拟机器人测试的模块                                     |
| [JavaAgent](JavaAgent/README.md)       | 可以通过javaAgent 最后热加载指定的class                  |
| [LogRecord](LogRecord/README.md)         | 日志记录模块                                             |
| [all](all/README.md)                   | 打包成一个duoduo-all 方便调用的模块.                     |


## 服务器结构

​	![服务器项目结构](all/img/ServerConstructor.png)

## 安装环境

* jdk 17
* maven 3.5
* Mysql(MariaDB) Redis 两个必须都有.
* 推荐使用 IntelliJ IDEA(Eclipse 对module的使用感觉不是很好).

## 引用
	<dependency>
		<groupId>io.github.qiunet</groupId>
		<artifactId>duoduo-all</artifactId>
		<version>7.1.1</version>
	</dependency>

 
## 示例

自己写了服务器端模板工程. 可以参考[模板项目](https://github.com/qiunet/DuoDuoExample)

## 交流
QQ群: `669409114` , 入群请附带申请留言: "Github"
