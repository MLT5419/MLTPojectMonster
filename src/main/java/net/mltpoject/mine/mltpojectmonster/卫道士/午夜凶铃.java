package net.mltpoject.mine.mltpojectmonster.卫道士;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 午夜凶铃 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 午夜凶铃启用;

    public static ForgeConfigSpec.DoubleValue 午夜凶铃概率;

    public static ForgeConfigSpec.IntValue 午夜凶铃诅咒时间;

    public static ForgeConfigSpec.BooleanValue 午夜凶铃诅咒时间刷新;

    public static ForgeConfigSpec.IntValue 午夜凶铃诅咒施加量;

    public static ForgeConfigSpec.IntValue 午夜凶铃诅咒递增量;

    public static ForgeConfigSpec.IntValue 午夜凶铃诅咒递减量;

    public static ForgeConfigSpec.IntValue 午夜凶铃诅咒传递附加量;

    public static ForgeConfigSpec.IntValue 午夜凶铃诅咒致死数量;

    public 午夜凶铃(){
        super("卫道士", "午夜凶铃");
        生成订阅类型 = EntityType.VINDICATOR;
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        午夜凶铃启用 = 构建
                .comment("如果启用，卫道士有一定概率会诅咒命中的玩家")
                .define("午夜凶铃启用", true);

        午夜凶铃概率 = 构建
                .comment("卫道士可以诅咒玩家的概率")
                .defineInRange("午夜凶铃概率", 0.3, 0, 1);

        午夜凶铃诅咒时间 = 构建
                .comment("诅咒会持续多长时间")
                .defineInRange("午夜凶铃诅咒时间", 8400, 0, Integer.MAX_VALUE);

        午夜凶铃诅咒时间刷新 = 构建
                .comment("如果启用，当诅咒层数增加后会刷新持续时间")
                .define("午夜凶铃诅咒时间刷新", true);

        午夜凶铃诅咒施加量 = 构建
                .comment("午夜凶铃能力的卫道士每次攻击给玩家施加的诅咒层数")
                .defineInRange("午夜凶铃诅咒施加量", 1, 0, Integer.MAX_VALUE);

        午夜凶铃诅咒递增量 = 构建
                .comment("当被诅咒的玩家被怪物攻击后诅咒增加的数量")
                .defineInRange("午夜凶铃诅咒递增量", 1, 0, Integer.MAX_VALUE);

        午夜凶铃诅咒递减量 = 构建
                .comment("当被诅咒的玩家击杀怪物后诅咒减少的数量")
                .defineInRange("午夜凶铃诅咒递减量", 1, 0, Integer.MAX_VALUE);

        午夜凶铃诅咒传递附加量 = 构建
                .comment("当玩家通过攻击传递诅咒后诅咒会额外增加的数量")
                .defineInRange("午夜凶铃诅咒传递附加量", 1, 0, Integer.MAX_VALUE);

        午夜凶铃诅咒致死数量 = 构建
                .comment("多少层诅咒会让玩家立即去世")
                .defineInRange("午夜凶铃诅咒致死数量", 77, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        Entity 攻击者 = event.getSource().getEntity();
        LivingEntity 被攻击者 = event.getEntityLiving();

        if (!午夜凶铃启用.get()){
            return;
        }

        if (被攻击者 instanceof Player && 攻击者 instanceof Player){
            var 玩家 = (Player) 被攻击者;
            var 凶手玩家 = (Player) 攻击者;

            if (获取诅咒层数(凶手玩家) > 0){
                添加诅咒(获取诅咒层数(凶手玩家) + 午夜凶铃诅咒传递附加量.get(), 玩家);

                TextComponent 对凶手说 = new TextComponent("你将");
                对凶手说.append(new TextComponent(Integer.toString(获取诅咒层数(玩家))).setStyle(Style.EMPTY.withColor(数字颜色(获取诅咒层数(玩家)))));
                对凶手说.append("层诅咒传递给了");
                对凶手说.append(玩家.getName());

                TextComponent 对目标说 = new TextComponent("你被");
                对目标说.append(玩家.getName());
                对目标说.append("传递了");
                对目标说.append(new TextComponent(Integer.toString(获取诅咒层数(玩家))).setStyle(Style.EMPTY.withColor(数字颜色(获取诅咒层数(玩家)))));
                对目标说.append("层诅咒(满层" + 午夜凶铃诅咒致死数量.get() +")");

                消除诅咒(凶手玩家);

                发送能力提示(对目标说, 玩家);
                发送能力提示(对凶手说, 凶手玩家);

                if (午夜凶铃诅咒时间刷新.get()){
                    持续时间重置(玩家);
                }
            }
        }

        if (被攻击者 instanceof Player && 攻击者 instanceof Monster){
            var 玩家 = (Player) 被攻击者;
            if (获取诅咒层数(玩家) > 0){
                添加诅咒(午夜凶铃诅咒递增量.get(), 玩家);
                if (!(攻击者 instanceof Vindicator)){
                    TextComponent message = new TextComponent("诅咒正在加剧，当前诅咒层数(满层" + 午夜凶铃诅咒致死数量.get() +")：");
                    message.append(new TextComponent(Integer.toString(获取诅咒层数(被攻击者))).setStyle(Style.EMPTY.withColor(数字颜色(获取诅咒层数(被攻击者)))));
                    发送能力提示(message, 玩家);
                    if (午夜凶铃诅咒时间刷新.get()){
                        持续时间重置(被攻击者);
                    }
                }
            }
        }

        if (被攻击者 instanceof Player && 攻击者 instanceof Vindicator) {
            Vindicator 卫道士 = (Vindicator) 攻击者;

            if (!NBT工具.获取NBTBool("午夜凶铃", 卫道士)){
                return;
            }

            TextComponent message;
            if (获取诅咒层数(被攻击者) <= 0){
                添加诅咒(午夜凶铃诅咒施加量.get(), 被攻击者);

                message = new TextComponent("你被诅咒了，当前诅咒层数(满层" + 午夜凶铃诅咒致死数量.get() +")：");
                message.append(new TextComponent(Integer.toString(获取诅咒层数(被攻击者))).setStyle(Style.EMPTY.withColor(数字颜色(获取诅咒层数(被攻击者)))));
                持续时间重置(被攻击者);
            }
            else{
                添加诅咒(午夜凶铃诅咒施加量.get(), 被攻击者);

                message = new TextComponent("诅咒正在加剧，当前诅咒层数(满层" + 午夜凶铃诅咒致死数量.get() +")：");
                message.append(new TextComponent(Integer.toString(获取诅咒层数(被攻击者))).setStyle(Style.EMPTY.withColor(数字颜色(获取诅咒层数(被攻击者)))));

                if (午夜凶铃诅咒时间刷新.get()){
                    持续时间重置(被攻击者);
                }
            }

            发送能力提示(message, (Player) 被攻击者);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player){
            var 玩家 = (Player) event.getEntityLiving();
            var 诅咒层数 = 获取诅咒层数(玩家);
            if (诅咒层数 >= 午夜凶铃诅咒致死数量.get()){
                玩家.die(DamageSource.MAGIC);
                return;
            }

            if (诅咒层数 > 0){
                持续时间步进(玩家);
                if (是否结束(玩家)){
                    消除诅咒(玩家);
                    TextComponent message = new TextComponent("诅咒已经随时间消散了");
                    发送能力提示(message, 玩家);
                }
            }
        }
    }
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Monster){
            // 获取击杀者
            Entity 击杀者 = event.getSource().getEntity();

            if (击杀者 instanceof AbstractArrow){
                击杀者 = ((AbstractArrow) 击杀者).getOwner();
            }

            if (击杀者 == null){
                return;
            }

            if (击杀者 instanceof Player){
                var 诅咒数量 = 获取诅咒层数((Player) 击杀者);

                if (诅咒数量 > 0){
                    添加诅咒(0 - 午夜凶铃诅咒递减量.get(), (Player) 击杀者);

                    TextComponent message = new TextComponent("诅咒正在衰退，当前诅咒层数(满层" + 午夜凶铃诅咒致死数量.get() +")：");
                    message.append(new TextComponent(Integer.toString(获取诅咒层数((Player) 击杀者))).setStyle(Style.EMPTY.withColor(数字颜色(获取诅咒层数((Player) 击杀者)))));
                    发送能力提示(message, (Player) 击杀者);
                }
            }
        }
    }

    @Override
    protected void 当订阅生物生成(LivingEntity 生物) {
        if (Math.random() < 午夜凶铃概率.get()) {
            NBT工具.添加NBT("午夜凶铃", true, 生物);
        }
    }

    private static int 获取诅咒层数(LivingEntity 实体){
        return NBT工具.获取NBTInt("午夜凶铃诅咒", 实体);
    }

    private static void 添加诅咒(int 数量, LivingEntity 实体){
        var 当前层数 = 获取诅咒层数(实体);
        NBT工具.添加NBT("午夜凶铃诅咒", 当前层数 + 数量, 实体);
    }

    private static void 消除诅咒(LivingEntity 实体){
        NBT工具.添加NBT("午夜凶铃诅咒", 0, 实体);
    }


    private static boolean 是否结束(LivingEntity 实体){
        return 获取持续时间计时(实体) <= 0;
    }

    private static int 获取持续时间计时(LivingEntity 实体){
        return NBT工具.获取NBTInt("午夜凶铃计时器", 实体);
    }

    private static void 持续时间步进(LivingEntity 实体){
        var 当前时间 = 获取持续时间计时(实体);
        NBT工具.添加NBT("午夜凶铃计时器", 当前时间 - 1, 实体);
    }

    private static void 持续时间重置(LivingEntity 实体){
        NBT工具.添加NBT("午夜凶铃计时器", 午夜凶铃诅咒时间.get(), 实体);
    }

    private static ChatFormatting 数字颜色(int 诅咒层数){
        var 致死量 = 午夜凶铃诅咒致死数量.get();

        if (诅咒层数 < 致死量 * 0.1){
            return ChatFormatting.GREEN;
        }

        if (诅咒层数 < 致死量 * 0.3){
            return ChatFormatting.YELLOW;
        }

        if (诅咒层数 < 致死量 * 0.6){
            return ChatFormatting.GOLD;
        }

        if (诅咒层数 < 致死量 * 0.9){
            return ChatFormatting.DARK_PURPLE;
        }

        return ChatFormatting.DARK_RED;
    }
}
