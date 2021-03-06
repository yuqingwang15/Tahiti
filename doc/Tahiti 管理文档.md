# Tahiti 管理文档

## 计划

| 内容 | 时间（天）| 
| :---: |:---:| 
| 选择技术/框架 | 2 | 
| 搭建程序基本架构 | 4 | 
| 完善模块与重新组织代码 | 5 |
| 测试与文档 | 3 | 
  
## 总结

项目历时两周，团队成员协同开发，基于开源网络应用框架 Netty 实现了一个 C/S 聊天程序。

经过这个项目，我们发现在项目前期划分好模块，做好架构的模块化，可以在后期大大降低协同开发时候产生的代码冲突。

我们在本次项目中先按照需求写了很多代码，又将模块功能泛化删了很多代码，最后留下来了具有很高可复用性的模块。这些经验为我们将来直接开发高可复用性的模块提供了很好的借鉴。

## 分工 

### 施闻轩 1352978 [@SummerWish](https://github.com/SummerWish/)

- 基础架构搭建: 已完成，已测试
- 程序文档

### 李平山 1352840 [@nicktogo](https://github.com/nicktogo)

- 消息过滤模块: 已完成，已测试
- 消息转发模块: 已完成，已测试
- 程序文档
- 管理文档

### 张航 1352960 [@HermanZzz](https://github.com/HermanZzz)

- 日志记录模块: 已完成，已测试
- 程序文档

### 刘林青 1354361 [@likicode](https://github.com/likicode)

- 消息回复模块: 已完成，已测试
- 消息转发模块: 已完成，已测试
- 复用文档
- 程序文档

### 王雨晴 1354202 [@yuqingwang15](https://github.com/yuqingwang15)

- 数据库模块: 部分完成
- 数据库操作工具模块: 部分完成
- 程序文档

## 完成情况

程序整体架构在开发过程中不断地演变，解耦了各个模块。  

各模块都实现了预期需求，同时也进行了针对性的测试，确保程序的功能正确。
  
各模块能够配合起来正常工作，保障程序整体能够正确地运行。

