package net.mltpoject.mine.mltpojectmonster.蜘蛛;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 刺客 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 刺客启用;

    public static ForgeConfigSpec.DoubleValue 刺客概率;

    public static ForgeConfigSpec.IntValue 刺客范围;
    
    public 刺客(){
        super("蜘蛛", "刺客");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        刺客启用 = 构建
                .comment("如果启用，蜘蛛有一定概率会在远离玩家时隐身并恢复生命值")
                .define("刺客启用", true);

        刺客概率 = 构建
                .comment("蜘蛛成为刺客的概率")
                .defineInRange("刺客概率", 0.3, 0, 1);

        刺客范围 = 构建
                .comment("刺客距离玩家多远才会触发隐身与恢复")
                .defineInRange("刺客范围", 8, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Spider) {
            if (!刺客启用.get()){
                return;
            }

            Spider 蜘蛛 = (Spider) living;

            CompoundTag nbtTag = 蜘蛛.getPersistentData().getCompound("mltpojectmonster");
            boolean 是刺客 = nbtTag.contains("刺客") && nbtTag.getBoolean("刺客");

            if (!是刺客){
                return;
            }

            for (Entity entity : 蜘蛛.level.getEntities(蜘蛛, 蜘蛛.getBoundingBox().inflate(刺客范围.get()))) {
                if (entity instanceof LivingEntity) {
                    LivingEntity 单位 = (LivingEntity) entity;
                    if (单位 instanceof Player) {
                        return;
                    }
                }
            }

            MobEffectInstance invisibility = new MobEffectInstance(MobEffects.INVISIBILITY, 10, 1, false, false);
            MobEffectInstance regeneration = new MobEffectInstance(MobEffects.REGENERATION, 10, 1, false, false);

            蜘蛛.addEffect(invisibility);
            蜘蛛.addEffect(regeneration);
        }
    }

    // 在生成生物的事件中订阅
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().getType() == EntityType.SPIDER) {
            Spider 蜘蛛 = (Spider) event.getEntity();

            // 随机决定是否添加 NBT 标签
            if (Math.random() < 刺客概率.get()) {
                // 添加 NBT 标签
                CompoundTag nbt = new CompoundTag();
                nbt.putBoolean("刺客", true);
                蜘蛛.getPersistentData().put("mltpojectmonster", nbt);
            }
        }
    }
}
