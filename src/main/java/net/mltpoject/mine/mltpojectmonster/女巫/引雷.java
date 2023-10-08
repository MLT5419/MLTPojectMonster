package net.mltpoject.mine.mltpojectmonster.女巫;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 引雷 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 引雷启用;

    public static ForgeConfigSpec.DoubleValue 引雷概率;

    public 引雷(){
        super("女巫", "引雷");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        引雷启用 = 构建
                .comment("如果启用，女巫有一定概率会在死亡后在凶手的位置召唤闪电")
                .define("引雷启用", true);

        引雷概率 = 构建
                .comment("女巫可以召唤闪电的概率")
                .defineInRange("引雷概率", 0.3, 0, 1);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Witch) {
            if (!引雷启用.get()){
                return;
            }

            Witch 女巫 = (Witch) event.getEntityLiving();

            if (!NBT工具.获取NBTBool("引雷", 女巫)){
                return;
            }

            // 获取击杀者
            Entity 击杀者 = event.getSource().getEntity();

            if (击杀者 == null){
                return;
            }

            if (击杀者 instanceof Player) {
                // 在击杀者位置生成闪电
                LightningBolt 闪电 = new LightningBolt(EntityType.LIGHTNING_BOLT, 击杀者.getCommandSenderWorld());

                闪电.setPos(击杀者.position());

                女巫.getCommandSenderWorld().addFreshEntity(闪电);
            }
        }
    }

    @Override
    protected void 当生成生物(LivingEntity 生物) {
        if (生物.getType() == EntityType.WITCH) {
            if (Math.random() < 引雷概率.get()) {
                NBT工具.添加NBT("引雷", true, 生物);
            }
        }
    }
}
