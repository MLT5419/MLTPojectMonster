# 銘洛天的怪物增强
###### *MLTPoject Monster* [MLTPM]

## 这是什么
一个给部分怪物增加了特殊能力的Mod
还给玩家提供了一些友善的加成

能力全部使用NBT标签来进行管理，但是能力的触发是分生物类型的。
可能未来会出现通用能力。

## 能力列表
### 僵尸
- **飞雷神**
  - 有一定概率(默认为35%)使僵尸获得能力，使其在选择玩家作为目标时如果与玩家在一定距离(默认为16格)内则会瞬移到玩家的位置。
  - 僵尸在瞬移后会保持一段时间(默认为5秒)，随后会瞬移回瞬移前的位置。
  - 这个能力是一次性的。
- **感染者**
  - 有一定概率(默认为30%)使僵尸获得能力，使其死亡后在原地生成随机数量的蠹虫。
  - 默认情况下，最低生成1个，最高生成3个。

### 骷髅
- **医疗兵**
  - 有一定概率(默认为50%)使骷髅获得能力，使其可以主动瞄准其它血量低于一定比例的怪物，并且在弓箭击中怪物后使其恢复生命。
  - 在默认情况下，就算没有医疗兵能力，所有持有弓箭的骷髅都不会对其它怪物造成伤害。
  - 哪怕没有主动瞄准，医疗兵骷髅在击中怪物的时候也会为其恢复生命。
  - 可以通过配置使不拿着弓箭的骷髅也可以治疗队友并且避免误伤。

### 末影人
- **呼唤**
  - 有一定概率(默认为30%)使末影人获得能力，使其在攻击后不会对玩家造成伤害，而是从一定范围内(默认为32格)召唤一个怪物到自身的位置。
  - 如果末影人找不到可以召唤的目标，那么还是会正常的对玩家造成伤害。
- **转移**
  - 有一定概率(默认为30%)使末影人获得能力，使其在死亡后将凶手传送到自己的位置。

### 蠹虫
- **虫群**
  - 有一定概率(默认为50%)使蠹虫获得能力，使其在受到伤害后有一定概率(默认为25%)在攻击者的位置召唤一个新的蠹虫。
  - 在生命值低于一定比例(默认为50%)后，这个概率提升至100%。
  - 如果没有攻击者，那么蠹虫会选择32格内的随机玩家生成新的蠹虫。
  - 新召唤的蠹虫的攻击力为召唤者蠹虫的125%
  - 被召唤的蠹虫依然有可能拥有虫群能力，但是最大套娃数量有限制(默认为3)。
- **寄生**
  - 有一定概率(默认为25%)使蠹虫获得能力，使其在攻击玩家时有一定概率(默认为10%)寄生在玩家体内。
  - 被寄生的玩家会每隔一段时间(默认为5秒)失去饥饿值。
  - 寄生的效果可以叠加。
  - 寄生结束后，所有的蠹虫都会重新生成，并且拥有更高的生命值(默认倍数为2.0)。
  - 默认情况下，新生成的蠹虫不会使用寄生能力(禁止套娃)，在这种情况下拥有寄生能力的蠹虫每次攻击会恢复生命值(默认为1)。
- **母虫**
  - 有一定概率(默认为25%)使蠹虫获得能力，使其如果附近(默认距离为8)有怪物则会寄生在其身上。
  - 被寄生的怪物每隔一段时间(默认为5秒)都会受到一次基于最大生命值的伤害(默认为10%)，并生成一个蠹虫。
  - 寄生可以叠加。
  - 可以通过配置使母虫饥不择食。(可以选择任意除了玩家之外的生物作为寄生目标)

### 女巫
- **引雷**
  - 有一定概率(默认为30%)使女巫获得能力，使其在死亡后在凶手的位置召唤一道闪电。

### 蜘蛛
- **刺客**
  - 有一定概率(默认为30%)使蜘蛛获得能力，使其在附近一定距离(默认为8格)内没有玩家时获得隐身效果。

### 掠夺者
- **等级掠夺**
  - 有一定概率(默认为25%)使掠夺者获得能力，使其在击中玩家后使玩家的等级-1。

### 卫道士
- **午夜凶铃**
  - 有一定概率(默认为25%)使卫道士获得能力，使其在对玩家造成伤害后诅咒受害者很长一段时间(默认7分钟)。
  - 诅咒可以叠加。
  - 被诅咒的玩家受到来自怪物的伤害会使诅咒层数+1。
  - 被诅咒的玩家击杀任意怪物后会使诅咒层数-1。
  - 玩家攻击其它玩家会将自身的诅咒额外增加一层并传递给被攻击的玩家，随后自身的诅咒解除。
  - 当一名玩家的诅咒层数大于一定层数后立即死亡(默认为77层)。

### 玩家
- **防守尸**
  - 玩家在重生后获得一段时间(默认为15秒)的生命恢复10与抗性提升10的效果。
