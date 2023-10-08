package net.mltpoject.mine.mltpojectmonster.蠹虫;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 寄生 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 寄生启用;

    public static ForgeConfigSpec.BooleanValue 寄生套娃启用;

    public static ForgeConfigSpec.DoubleValue 寄生概率;

    public static ForgeConfigSpec.DoubleValue 寄生发生概率;

    public static ForgeConfigSpec.IntValue 饥饿降低间隔;

    public static ForgeConfigSpec.IntValue 饥饿降低率;

    public static ForgeConfigSpec.IntValue 寄生时间;

    public static ForgeConfigSpec.DoubleValue 寄生结束生命值倍率;


    public static ForgeConfigSpec.IntValue 寄生套娃攻击恢复;

    public 寄生(){
        super("蠹虫", "寄生");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        寄生启用 = 构建
                .comment("如果启用，蠹虫有一定概率会在击中玩家后寄生在玩家体内")
                .define("寄生启用", true);

        寄生套娃启用 = 构建
                .comment("如果启用，蠹虫在寄生出现后依然有可能成为寄生蠹虫")
                .define("寄生套娃启用", false);

        寄生概率 = 构建
                .comment("寄生能力出现的概率")
                .defineInRange("寄生概率", 0.25, 0, 1);

        寄生发生概率 = 构建
                .comment("每次击中玩家有多少概率进行寄生")
                .defineInRange("寄生发生概率", 0.1, 0, 1);

        饥饿降低间隔 = 构建
                .comment("被寄生的玩家间隔多久降低一次饥饿值")
                .defineInRange("饥饿降低间隔", 100, 0, Integer.MAX_VALUE);

        饥饿降低率 = 构建
                .comment("被寄生的玩家每次降低的饥饿值")
                .defineInRange("饥饿降低率", 1, 0, Integer.MAX_VALUE);

        寄生时间 = 构建
                .comment("寄生后重新生成一个蠹虫并结束寄生的时间")
                .defineInRange("寄生时间", 1200, 0, Integer.MAX_VALUE);

        寄生结束生命值倍率 = 构建
                .comment("寄生结束后生成的蠹虫的生命倍率")
                .defineInRange("寄生结束生命值倍率", 2.0, 0, 10);

        寄生套娃攻击恢复 = 构建
                .comment("在禁止套娃的情况下寄生蠹虫的攻击恢复量")
                .defineInRange("寄生套娃攻击恢复", 1, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        Entity 攻击者 = event.getSource().getEntity();
        LivingEntity 被攻击者 = event.getEntityLiving();

        if (被攻击者 instanceof Player && 攻击者 instanceof Silverfish) {
            Silverfish 蠹虫 = (Silverfish) 攻击者;

            if (!寄生启用.get()){
                return;
            }

            if (!NBT工具.获取NBTBool("寄生", 蠹虫)){
                return;
            }

            if (!寄生套娃启用.get()){
                if (NBT工具.获取NBTBool("寄生套娃", 蠹虫)){
                    蠹虫.heal(寄生套娃攻击恢复.get());
                    return;
                }
            }

            if (Math.random() < 寄生发生概率.get()) {
                var 寄生数量 = NBT工具.获取NBTInt("被寄生", 被攻击者);
                NBT工具.添加NBT("被寄生", 寄生数量 + 1, 被攻击者);
                NBT工具.添加NBT("被寄生计时", 寄生时间.get(), 被攻击者);

                TextComponent message = new TextComponent("你被");
                message.append(new TextComponent(Integer.toString(寄生数量 + 1)).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                message.append("个蠹虫寄生了！");

                发送能力提示(message, (Player) 被攻击者);

                蠹虫.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player){
            var 玩家 = (Player) event.getEntityLiving();
            var 寄生数量 = NBT工具.获取NBTInt("被寄生", 玩家);

            if (寄生数量 > 0){
                var 寄生时间 = NBT工具.获取NBTInt("被寄生计时", 玩家);
                var 寄生饥饿时间 = NBT工具.获取NBTInt("被寄生饥饿计时", 玩家);

                if (寄生饥饿时间 > 0){
                    寄生饥饿时间 -= 1;
                }
                else{
                    玩家.getFoodData().setFoodLevel(玩家.getFoodData().getFoodLevel() - 饥饿降低率.get());
                    寄生饥饿时间 = 饥饿降低间隔.get();
                }
                NBT工具.添加NBT("被寄生饥饿计时", 寄生饥饿时间, 玩家);

                if (寄生时间 > 0){
                    寄生时间 -= 1;
                }
                else{
                    for (int i = 0; i < 寄生数量; i++){
                        // 在受害者位置生成新蠹虫
                        Silverfish 新蠹虫 = new Silverfish(EntityType.SILVERFISH, 玩家.getCommandSenderWorld());
                        新蠹虫.setPos(玩家.position());

                        // 添加新蠹虫到世界
                        主动生成生物(新蠹虫, 玩家);

                        新蠹虫.getAttribute(Attributes.MAX_HEALTH).setBaseValue(新蠹虫.getAttribute(Attributes.MAX_HEALTH).getValue() * 寄生结束生命值倍率.get());
                        新蠹虫.heal(新蠹虫.getMaxHealth());

                        NBT工具.添加NBT("寄生套娃", true, 新蠹虫);

                        新蠹虫.getCommandSenderWorld().playSound(
                                null,
                                新蠹虫.position().x,
                                新蠹虫.position().y,
                                新蠹虫.position().z,
                                SoundEvents.SLIME_JUMP,
                                SoundSource.NEUTRAL,
                                1.0F,
                                1.0F);
                    }

                    TextComponent message = new TextComponent(寄生数量 + "个蠹虫结束了寄生，出现了。");
                    发送能力提示(message, 玩家);

                    NBT工具.添加NBT("被寄生", 0, 玩家);
                }
                NBT工具.添加NBT("被寄生计时", 寄生时间, 玩家);
            }
        }
    }

    // 在生成生物的事件中订阅
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().getType() == EntityType.SILVERFISH) {
            if (Math.random() < 寄生概率.get()) {
                NBT工具.添加NBT("寄生", true, event.getEntity());
            }
        }
    }

    @Override
    protected void 当主动生成生物(LivingEntity 生物, LivingEntity 生成源) {
        if (生物.getType() == EntityType.SILVERFISH) {
            if (Math.random() < 寄生概率.get()) {
                NBT工具.添加NBT("寄生", true, 生物);
            }
        }
    }
}
