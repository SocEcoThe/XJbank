# XJCraft 多货币插件使用说明书

改自 https://github.com/XJcraft/XJBank

和原版相比，修改了缺失的xjcraft依赖

## 功能

### 当前支持

1. 无成本货币发行
2. 用户多币种账户
3. 电子账户之间转账
4. 开出和兑现成书支票
5. 交易记录查询
6. 傻瓜式 ATM 机服务
7. 货币兑换

## 指令

### 货币相关 

（不发行货币的人可以略过本节）

1. 新建货币  `/bank currency new [货币代码] [货币名称]`

   货币代码固定为3位大写英文字母（类似ISO 4217中定义的现实货币代码）

   货币名称任意，最长50字符

2. 增发货币 `/bank currency incr [货币代码] [增发货币数量]`

3. 减少货币 `/bank currency decr [货币代码] [减少货币数量]`

4. 重命名货币 `/bank currency rename [货币代码] [新货币名称]`

5. 准备金提取 `/bank currency get [货币代码] [货币数量]`

   备注：准备金归还可通过向准备金账户转账实现，准备金账户为`$(货币代码)`
   
6. 查看准备金账户余额 `/bank currency balance [货币代码]`

7. 准备金付款 `/bank currency pay [收款人] [准备金的货币代码] [待支付的货币代码] [货币数量]` 

### 转账

当我们需要直接向对方转账时，输入`/bank pay [对方玩家名] [货币代码] [货币数量]`，即可完成转账。

需要注意的是，转账不会有二次确认也不会有其他任何提示，请自行通知对方收款。如果填错收款账号请自行和收款方联系退款事宜。

玩家无需提前创建对应的货币账号，在收到新币种汇款时会自动创建对应币种的账号。

### 支票

#### 支票常识

支票是电子现金实体化的表现，属于不记名票据，当你开出支票后，对应的钱款就被固化在支票中了。此时如果你对支票进行了破坏，那这笔钱就消失了。所以请务必谨慎对待支票书。

支票面值不限，整数最高支持16位，小数点后支持4位。请根据需要自行开出对应面值的支票。

#### 支票样式

一本真实有效的支票通常有以下防伪点：

1. 著作人为 ：“XJCraft金融管理局”
2. 成色为：破烂不堪
3. 书名为：（货币代码） （空格） （金额）
4. 翻开内页，有“XJCraft金融管理局监制”和滚动的字符

支票书一般为两页，人类可读页为第一页，机读页为第二页，机读页有特定的数字签名。

***在插件配置文件中有 secert 需要换成服务器唯一的密钥，防止数字签名伪造。***

#### 支票指令

*批量指令适用于店主批量处理货箱，玩家可以略过批量相关内容*

1. 开出单张支票 `/bank check [货币代码] [货币数量]`

  如果指令成功执行，你的背包里会多出一本由“XJCraft金融管理局”著的支票书。

  注意：现在的支票要求手中有4张纸才能开出，如果纸不够则开票失败。

2. 兑现单张支票 `/bank cash`

  兑现支票要求手中持有待兑现的支票，支票成功兑现后会收走支票。

3. 批量开出支票 `/bank bluk check [货币代码] [面值] [数量]`

 批量开出支票要求玩家背包中有足够的纸（每本支票消耗4张纸），账户中有足够的余额，当背包无法容纳时会把支票喷在地上。

4. 批量兑换背包中的支票 `/bank bluk cash`

    该指令会把玩家背包中所有有效支票兑现并返还相应的纸张。

### 信息查看

玩家可通过 `/bank info` 查看自己的账户内多种货币的余额。

玩家也可通过`/bank info [货币代码]`查看某种货币的详细信息。
币种详细内容如下：

1. 货币名称
2. 发行人
3. 货币代码
4. 发行总量
5. 储备金数量
6. 玩家电子账户该币种的总余额

### 交易日志
在交易过程中我们尽可能的保留了日志，可以通过`/bank log`查看交易日志，每页5条。
标准的交易日志包含：时间，谁和你怎么交互，币种，金额和备注。
时间显示为xxx小时前，精确到2位小数。

### 货币兑换

货币采取单向汇率模式，汇率定义为1外币兑换本币数量

1. 查看货币之间的汇率 `/bank exchange get [卖出货币代码] [买入货币代码]`
2. 修改汇率 `/bank exchange set [卖出货币代码] [买入货币代码] [货币汇率]`
3. 兑换货币 `/bank exchange fx [卖出货币代码] [买入货币代码] [买入货币数量]`

## ATM
ATM是为了方便真实萌新使用的，无需记忆指令。

要创建一个ATM机，只需要一个木牌，任意材料均可，第一行写`[ATM]`。贴墙和直接站立都可以有响应。

要和ATM机互动，右键敲击ATM木牌，会询问你要办理的业务。随时可以通过输入`exit`退出ATM机。

当前ATM机支持5项功能，分别对应相应的指令。

| 功能     | 对应指令    |
| -------- | ----------- |
| 直接转账 | /bank pay   |
| 开出支票 | /bank check |
| 兑现支票 | /bank cash  |
| 查询余额 | /bank info  |
| 账单查询 | /bank log   |

ATM 机接到指令后会询问必要的参数，并执行你的指示。
