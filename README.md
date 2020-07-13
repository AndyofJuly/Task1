# Task1
## 2020.7.13日21:22代码更新说明
对代码结构进行了一些调整，对成就系统进行了一些优化

> 1.调整了代码结构，将包结构按不同系统进行设置，并进行分类

> 2.成就系统使用观察者模式，将多项成就分别拆分成观察者类，在事件源所在的类中预先注册好这些观察者，当触发这些事件时，会自动调用观察者判断是否满足成就。

---

## 2020.7.10日10:45代码更新说明
新增部分功能并进行了一些优化

> 1.怪物可以进行自由移动，并在角色对其进行攻击后，反过来攻击角色，直到角色视野中没有该怪物时，怪物又回到随机漫步状态

> 2.对客户端与服务端的传输协议进行了修改，服务端不再只返回字符串给客户端，而是连带对象一起传给了客户端，使用的是protobuf进行编解码传输

> 3.在Controller层和Service层之间增加了Service接口，便于统一管理方法，每个类具体做什么工作也比较清晰了

> 4.对代码结构进行了适当调整。

---

## 2020.7.7日10:15代码更新说明
完善了部分需求和功能

> 1.改进成就系统的实现方式，在excel表中增加具体成就对应的目标和数量字段，从而提高可扩展性

> 2.完善面对面交易功能，玩家之间可以交易和交换多件物品，补差价。

> 3.交易行系统倒计时使用定时器，角色竞价后先扣除对应的金钱，时间截止后自动完成交易。

> 4.小功能：每隔1分钟自动将数据持久化到数据库；击杀怪物获得随机装备和金钱；

> 5.原来的背包只是设计为一个map集合。现在，将背包容量划分为多个格子，每个格子有id、物品id和数量等属性，并新增整理背包功能。

---

## 2020.7.2日21:50代码更新说明
完善了数据库操作的相关代码，对代码进行了优化和调整

> 1.增加了进入游戏时，获取数据库中角色所有属性、身上装备、背包、成就情况等信息

> 2.增加了保存游戏功能，能够将角色所有属性、身上装备、背包、成就情况、公会列表、公会仓库的物品等信息持久化到数据库中

> 3.对成就系统进行了优化，借助excel表，使用集合来存储角色成就的完成情况，减少了大量代码

后续工作计划：

> 1.较多功能比较简单，需要再进行补充和完善

> 2.代码结构比较混乱，没有清晰的架构支撑，业务与其他框架或工具耦合过高，需要修改

---
## 2020.6.30日17:00代码更新说明
对代码进行了优化，同时实现了部分功能

> 1.输入调整为id而不是字符串，虽然自己测试时输入不友好，但代码显得更加简洁，更符合规范

> 2.初步实现任务成就系统，当玩家每完成一个成就时，会将该成就的布尔值设为true

> 3.初步实现公会系统，玩家可以创建公会、加入公会、根据职务拥有对应的权限来进行解散公会、申请批准、任职、开除等操作，主要通过设置等级并比较来实现。

> 4.优化链式调用，对常用的调用方法进行了抽象，其余的进行了拆分，过长的链式调用对异常处理非常不友好。

> 5.将传参roleId统一修改为role，去掉了大量根据roleid查找role的代码片段。

---
## 2020.6.28日11:00代码更新说明
初步增加了交易方法和交易行进行交易的方法

> 1.增加了玩家面对面交易的功能，角色在视野范围内与其他玩家进行交易，出售物品并给出价格，另外一位玩家可以接受或者拒绝此次交易

> 2.增加了交易行的一口价模式和拍卖模式方法。一口价由卖方提供，其他玩家可以直接购买；拍卖模式由卖方给出拍卖起始价后，其他玩家可以自由竞价，时间结束后进行交易处理。

> 3.接下来将编写任务、成就系统和工会系统的相关功能和方法，在实现后对该阶段的代码进行优化。

---
## 2020.6.20日21:00代码更新说明
通过九宫格算法，实现了玩家视野功能

> 将每个场景划分成多个小矩形（网格），每个角色以自己所在网格为中心，周围八个格子为自己的视野范围。每次玩家查看自己视野范围时，只需遍历九个格子中的所有实体即可，这样服务器性能相较于遍历全场景会更好。跨格子移动时，通过计算移动距离来判断角色所在网格的id是否变化，然后再进行相应的增删实体操作。

> 参考了CSDN博客上的九宫格算法思路，然后结合自己的工作进行了修改，链接：https://blog.csdn.net/weixin_38278878/article/details/105757545

## 2020.6.19日10:00代码更新说明
对代码进行了一些优化
> 1.对public static修饰的属性进行封装，向外部提供方法获取

> 2.对BOSS攻击和购买商品的长方法进行拆分和提取

> 3.对限购等数据进行持久化，对数据库操作方法进行了封装

> 4.对函数返回的输出进行了修改，对获取控制台输入进行了优化

---
## 2020.6.17日12:00代码更新说明
对代码进行了一些优化

> 1.修改了临时场景的生成方式，通过获取配置类数据的数据来构建临时场景

> 2.修改了部分方法的返回，不再以String进行返回，对所有方法中不恰当的地方进行了优化

> 3.去掉了一些不必要的循环，可以通过获得key的方式直接获取数据，而不需要循环查找

> 4.新增了获取视野的方法demo和场景内移动的方法demo，给玩家、怪物、npc新增了坐标属性，玩家与怪物超过一定距离，玩家无法攻击到怪物

> 5.新增了生成不重复id的方法，用于生成临时场景id和副本队伍id

> 6.新增限购的方法

后续可以优化的方向：

> 1.优先实现获取视野的相关方法

> 2.对public static修饰的属性进行封装，向外部提供方法获取

> 3.对BOSS攻击和购买商品的长方法进行拆分和提取

> 4.限购等数据应进行持久化、业务逻辑中不应当出现数据库操作方法，应该进行封装，或间隔时间才进行数据库操作

> 5.可以对BOSS状态进行扩展，考虑用状态机和行为树等实现

> 6.一些返回的提示消息可以利用事件驱动的相关方法进行封装

---
## 2020.6.15日11:00代码更新说明
对三阶段剩余功能进行完善

1.每个职业有对应的技能，可以通过命令skillList查看自己职业所能使用的技能

2.角色注册时选择职业，注册完后，需登录游戏方可进入游戏

3.在队伍中，玩家突然掉线，对队伍和副本的状态进行了处理，将去掉该玩家的相关信息，并对其他玩家进行通知

4.对聊天和邮件系统进行了优化，输入与输出更简单，容易理解

5.临时副本结束后，将删除对其的引用，从而使得其被JVM回收

---
## 2020.6.12日16:50代码更新说明

BossAttack类编写了boss按优先级攻击角色的方法

BaByAttack类编写了宝宝攻击怪物的方法

RoleService类中对不同技能进行了实现，调用相应的方法即可

TempSceneCreate:构建临时场景类，开始副本时会生成临时场景，打完后回收该场景，通过消除对该场景的引用的方式

Baby实体类编写了宝宝的相关属性，baby.xls为配置表，BabyStatic为与配置表对应的实体类，BabyResource为资源类，即已读取到内存中的配置数据

这次更新主要增加的内容有：

> 1.完善了各类技能

> 2.完善了boss和宝宝的简单AI、宝宝可以跟随角色移动

> 3.构建了临时场景用于组队打副本

目前未完成和待完善的地方：

> 1.还没有将对应职业与技能相匹配

> 2.可以新增功能：在角色注册时可以选择职业

> 3.组队离线时队伍和副本的状态管理

> 4.在游戏内部对聊天和邮件系统进行封装

导师意见，可新增：

> 1.商品限购功能，例如每个角色每天最多只能买10份某商品

> 2.视野设置，角色在场景中只能看到某个范围内的玩家、怪物，因此需要构建坐标系统

---
## 2020.6.9日17:00代码更新说明
这段时间对第三阶段的部分系统和需求进行了代码编写

> 1.根据第三阶段需求，新增了相应的excel表存放静态数据，增加了副本类和职业类，修改了相关代码。

> 2.初步完成了商城系统，玩家可以获取商品列表，然后选取对应的商品和数量，完成购买。

> 3.初步完成了组队系统，由其中一个玩家作为房主，创建队伍，其他角色根据队伍id加入队伍，房主可以执行开始副本操作。

> 4.初步完成了玩家互相PK，选择的设计方式为同一场景下玩家随意PK。

待完善的地方：

> 1.初步完成的系统有待完善和改进的地方，打算在完成所有功能后进行一次统一的迭代更新。

> 2.BOSS副本系统和开发各具特色的职业这两个任务偏复杂，目前只有简单的demo，需要花时间进行完善。

> 3.代码有些类的内容过于庞大，可以拆分，比较多的地方可以进行优化。

---
## 2020.6.7日11:00代码更新说明
第二阶段任务的一些代码优化

=======

> 1.有些静态资源的类中放了一些动态的对象，这样不好维护，且容易让人误解，因此对其进行了拆分

> 2.每个动态实体的id应该是唯一的。例如场景中生成的怪物，其id是唯一的，与配置表中的怪物id是不同的，因此使用了UUID来生成怪物唯一id

> 3.前面区分客户端时测试时是自己人为区分的，这里进行了一点点改进，即角色登录游戏时

> 4.对大量重复的代码片段进行了抽取，这样有利于程序运行效率，而且代码更简洁

> 5.准备开始第三阶段的任务，完成对应的需求开发

---
## 2020.6.5日代码更新说明
对代码部分功能进行了调整和优化

1.完成了对资源类按实体类进行了拆分，方便后续在不同类上进行扩展

2.对aoi查看场景信息这一功能进行了调整，在击杀怪物后，怪物在场景上消失

3.对实体类的属性增加了注释

4.完成了物品叠加功能的实现，根据key判断背包中是否有该物品，如果是药品则在原数量的基础上叠加，如果是装备则不叠加

5.对代码中的一些性能问题正在进一步优化中

---
## 2020.6.3日代码更新说明
#### 本次主要对代码中的问题部分进行了调整，实现了多个客户端登录，同时优化了一些不恰当的循环* 

1.对每个实体类的属性进行了修改，不再是持有该类的所有静态属性，而是只需持有其id即可。
这样优化了内存，通过key去访问hashmap的value这种方式更好

2.role不再声明为静态变量，现在登录多个客户端可以创建多个role，每个role之间的资源不共享

3.对RoleService类中的方法进行了调整，去掉了大量循环，保持代码整洁

4.对InitStaticResource类进行了拆分，目前正在修改中，计划将其按不同实体类进行拆分，每个实体类对应一个静态资源的配置类，这样方便后续进行扩展

---
## 2020.6.2日代码更新说明
本次更新主要增加了毒素和护盾技能demo，以及蓝药缓慢恢复功能，对其他地方进行了一些调整，增加了常量类优化代码中的常量配置  
*目前代码还存在的问题，下一步将集中解决：*    

1.实体类中包含key属性即可，无需包含整个静态类  
2.role对象应考虑多个客户端的，aoi功能需要修改为动态的  
3.静态资源读取类需要修改，数据库及资源类与业务类可以再拆分开来，目前耦合有些高。配置文件可以通过spring的注解进行扫描  
4.实体类的属性需要注释，保持注释充足和整洁  

---
## 2020.6.1日代码更新说明
本次更新主要增加了攻击怪物的方法
角色可以使用技能对怪物进行攻击，同时会消耗相应的mp，武器耐久也会下降，技能冷却时间没到时不能再释放

---
系统输入输出测试，客户端结果：

*aoi*  
要查看的场景为：未知世界；角色：andy 。 NPC：守将 。 怪物：恶龙 巨兽 。  
  
*getInfo*  
当前角色的hp：50。 mp：50。 武器耐久：50。 攻击力：12  
  
*skill 突刺攻击 恶龙*  
  
*skill 横扫千军 恶龙*  
  
*skill 横扫千军 恶龙*  
该技能冷却中  
  
*skill 突刺攻击 恶龙*  
  
*getInfo*  
当前角色的hp：50。 mp：30。 武器耐久：47。 攻击力：12  
  
*use 清泉酒*  
使用药品成功，你的当前血量为：50， 当前的蓝量为：41  
  
...  
  
*use 清泉酒*  
此药品已经没有了！  
  
*skill 横扫千军 恶龙*  
怪物血量为0，你已经打败该怪物！恭喜获得5000银和一个高级宝箱!  
  
*getInfo*  
当前角色的hp：50。 mp：12。 武器耐久：42。 攻击力：12  
  
*repair 钢剑*  
修理成功！当前武器耐久为：50  
  
*getInfo*  
当前角色的hp：50。 mp：13。 武器耐久：50。 攻击力：12  



## 2020.5.28日代码更新说明
    本次更新主要增加了一些角色功能
    在RoleService类中增加一些角色功能：
    使用命令“repair+装备名称”修理装备
    使用命令 “putOn+装备名称”穿戴装备
    使用命令 “takeOff+装备名称”脱下装备
    使用命令 “use+药品名称”使用药品
    使用命令 “getInfo”获得当前角色状态如Hp/Mp/武器耐久/攻击力等  

    增加了角色的静态数据excel表及对应的类
    增加了对角色技能进行初始化的类InitRole
    增加了Skill类，目前正在编写角色调用技能攻击怪物的功能  
---
    系统输入输出测试，客户端结果：  

    login m m
    登陆成功
    loginR andy
    登陆成功，您进入到了游戏世界
    putOn 钢剑
    你已成功装备该武器，目前攻击力为：12
    getInfo
    当前角色的hp：50。 mp：50。 武器耐久：50。 攻击力：12
    use 麻沸散
    血已满
    takeOff 钢剑
    你已成功卸下该武器，目前攻击力为：2

## 2020.5.22日代码更新说明
本次继续对读取excel中数据的方式进行了优化，将读取的工具类进行了封装，与业务进行了分离。

其次，将实体类中的一些静态类的属性提取出来封装成了一个类，并使得该类的属性与excel表中的字段一一对应，每次新增表格时，只需要新建excel并添加元素，然后在项目中新建对应的类即可，这样一来，后续新增一些有着静态数据的实体类时会非常方便

此外，项目开始推进到第二阶段，在项目中新增了与NPC对话的方法。由于写了读取excel表的工具类，因此新增这个功能的时候非常方便：首先在npc的excel表中新增words字段和内容，然后再NpcStatic类中新增words属性，最后新增对话功能的业务逻辑。

#### 客户端控制台结果展示：
login m m

登陆成功

loginR andy

登陆成功，您进入到了游戏世界

aoi

要查看的场景为：未知世界；角色：andy 。 NPC：守将 。 怪物：恶龙 巨兽 。 

checkPlace 森林

要查看的场景为：森林；角色：。 NPC：樵夫 猎户 神秘人 。 怪物：猛虎 毒蛇 火龙 。 

talkToNpc 守将

请出示你的通行证！

talkToNpc 神秘人

该场景没有这个npc

move 城堡

移动成功

aoi

要查看的场景为：城堡；角色：andy 。 NPC：公主 商人 。 怪物：逃兵 叛军 。 

talkToNpc 商人

小伙子，需要买一些补给品吗？

move 村子

移动成功

aoi

要查看的场景为：村子；角色：andy 。 NPC：村长 铁匠 。 怪物：小偷 恶人 。 

talkToNpc 村长

欢迎来到我们村庄，这是一片宁静祥和之地..

move 森林

移动成功

aoi

要查看的场景为：森林；角色：andy 。 NPC：樵夫 猎户 神秘人 。 怪物：猛虎 毒蛇 火龙 。 

talkToNpc 神秘人

没想到你能找到我呀，我可以给你传授一些秘籍，需要吗？



## 2020.5.20日下午代码更新说明
本次对代码读取excel中数据的方式进行了优化

这一次主要修改了ExcelToJson.java读取excel并解析为json的方式

这次改动，使得我们通过工具类ExcelToJson.java读取excel时，能够根据excel表的字段和数据的多少来自动转化为对象。相比于上一版，在这一版中，我们每次在某个类中增加一个属性时，不需要再在这个工具类中的一些地方做出进行相应的修改。

这样一来，无论是在excel表中直接增加元素，还是增加字段，都可以自动将excel中的数据解析为json，再转化为对象，而无需修改这个工具类，具有更好的实用性。


## 2020.5.19日下午代码更新说明
本次主要对原代码中读取配置数据的方式进行了优化：

在之前，时通过配置文件来修改或增加场景、NPC和怪物的相关常量数据。

目前已经进行了修改，可以直接在excel表中修改或增加场景、NPC和怪物的相关常量数据。

新建了一个ExcelToJson类，用于将excel表的数据解析成json，然后转换为java对象。

这样做相比之前，数据的表现更直观，更容易进行数据的增删等操作。

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

