package net.mltpoject.mine.mltpojectmonster.末影人;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 呼唤 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 呼唤启用;

    public static ForgeConfigSpec.DoubleValue 呼唤概率;

    public static ForgeConfigSpec.IntValue 呼唤范围;
    
    public 呼唤(){
        super("末影人", "呼唤");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        呼唤启用 = 构建
                .comment("如果启用，末影人有一定概率会获得攻击不造成伤害，而是召唤一个范围内的怪物的能力")
                .define("呼唤启用", true);

        呼唤概率 = 构建
                .comment("末影人可以呼唤怪物的概率")
                .defineInRange("呼唤概率", 0.3, 0, 1);

        呼唤范围 = 构建
                .comment("呼唤的范围")
                .defineInRange("呼唤范围", 32, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        Entity 攻击者 = event.getSource().getEntity();
        LivingEntity 被攻击者 = event.getEntityLiving();

        if (攻击者 instanceof EnderMan && 被攻击者 instanceof Player) {
            if (!呼唤启用.get()){
                return;
            }

            CompoundTag nbtTag = 攻击者.getPersistentData().getCompound("mltpojectmonster");
            boolean 是呼唤者 = nbtTag.contains("呼唤者") && nbtTag.getBoolean("呼唤者");

            if (!是呼唤者){
                return;
            }

            for (Entity entity : 攻击者.level.getEntities(攻击者, 攻击者.getBoundingBox().inflate(呼唤范围.get()))) {
                if (entity instanceof LivingEntity) {
                    LivingEntity 目标 = (LivingEntity) entity;
                    if (目标 instanceof Monster) {
                        目标.setPos(
                                被攻击者.position().x,
                                被攻击者.position().y,
                                被攻击者.position().z
                        );

                        攻击者.getCommandSenderWorld().playSound(
                                null,
                                攻击者.position().x,
                                攻击者.position().y,
                                攻击者.position().z,
                                SoundEvents.ENDERMAN_TELEPORT,
                                SoundSource.NEUTRAL,
                                1.0F,
                                1.0F);

                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }

    // 在生成生物的事件中订阅
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().getType() == EntityType.ENDERMAN) {
            EnderMan 末影人 = (EnderMan) event.getEntity();

            // 随机决定是否添加 NBT 标签
            if (Math.random() < 呼唤概率.get()) {
                // 添加 NBT 标签
                CompoundTag nbt = new CompoundTag();
                nbt.putBoolean("呼唤者", true);
                末影人.getPersistentData().put("mltpojectmonster", nbt);
            }
        }
    }
}
