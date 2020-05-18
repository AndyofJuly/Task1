# Task1
## 2020.5.18日下午代码更新说明

本次主要对原代码进行了一些优化

1.优化了场景之间的关系，将原来由数组表示场景间的关系，改为使用Hashmap结构来表示，其中key存储当前场景的名字，节点存储一个数组，该数组的元素为从当前场景出发可以到达的场景的id。使用move命令时，通过循环数组来查找目标场景是否可以到达。

2.优化了代码读取常量的方式，前面都是使用Const类来存储场景名字、场景关系、NPC名字和怪物名字，现已将这些数据统一使用配置文件存储，这样一来，新增数据时对代码没有侵入，也无需到代码中修改，只需要在对应的配置文件中修改或新增内容即可。由于使用了配置文件存储数据，数据库中的场景表、NPC表和怪物表也删除掉了。

3.在优化的同时，对涉及到的部分代码也进行了修改，进行了适当调整。


## 2020.5.17日上传的更新内容说明

本次更新主要对上一版本的一些问题和不足之处进行了完善

主要的改动有：

1.增加了Const类来保存一些常量信息，例如场景名字，NPC名字和怪物名字等

2.增加了自定义注解，用于对客户端输入的命令进行解析，然后根据命令自动调用FunctionService中的相应方法进行处理；先比于最初的条件判断具有更好的可读性和可扩展性

3.根据新增的常量类和自定义的注解类，对原代码中有相关调用的地方进行了修改，主要修改了调用数据库的类ConnectSql中的方法，避免了角色每次场景切换和查看场景信息等都需要查询数据库的操作，减少了对数据库的不必要的访问

4.增加了日志记录，存储在resources文件中，同时在该文件中增加了用到的数据库表，包括表的结构和数据


## 2020.5.14日初步实现功能：
用户注册与登录、角色注册与登录、角色move功能、aoi查看场景实体功能、checkPlace查看其它场景实体功能

#### 输出效果展示（idea的客户端控制台输入与输出）：

请输入命令：
client channel init!

login m m
登陆成功

loginR andy
登陆成功，您进入到了游戏世界

aoi
当前场景为：城堡；角色：andy 。 NPC：公主 商人 。 怪物：逃兵 叛军 

move 原始之地
不可以从这里去这个地方

checkPlace 森林
要查看的场景为：森林；角色：。 NPC：樵夫 猎户 。 怪物：猛虎 毒蛇 

move 村子
移动成功

aoi
当前场景为：村子；角色：andy 。 NPC：村长 铁匠 。 怪物：小偷 恶人 

checkPlace 城堡
要查看的场景为：城堡；角色：。 NPC：公主 商人 。 怪物：逃兵 叛军 

