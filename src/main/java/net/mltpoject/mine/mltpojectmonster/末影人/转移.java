package net.mltpoject.mine.mltpojectmonster.末影人;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 转移 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 转移启用;

    public static ForgeConfigSpec.DoubleValue 转移概率;

    public 转移(){
        super("末影人", "转移");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        转移启用 = 构建
                .comment("如果启用，末影人有概率在死亡后将凶手传送至自身的位置")
                .define("转移启用", true);

        转移概率 = 构建
                .comment("末影人死亡后传送玩家的概率")
                .defineInRange("转移概率", 0.3, 0, 1);
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        if (event.getEntity() instanceof EnderMan) {
            if (!转移启用.get()){
                return;
            }

            EnderMan 末影人 = (EnderMan) event.getEntity();

            if (!NBT工具.获取NBTBool("转移", 末影人)){
                return;
            }

            // 计算末影人受到伤害后的生命值
            float healthAfterDamage = 末影人.getHealth() - event.getAmount();
            if (healthAfterDamage > 0){
                return;
            }

            // 获取击杀者
            Entity 击杀者 = event.getSource().getEntity();

            if (击杀者 == null){
                for (Entity entity : 末影人.level.getEntities(末影人, 末影人.getBoundingBox().inflate(8))) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity 单位 = (LivingEntity) entity;
                        if (单位 instanceof Player) {
                            击杀者 = 单位;
                            break;
                        }
                    }
                }
            }

            if (击杀者 instanceof Player) {
                击杀者.setPos(末影人.position());
                击杀者.getCommandSenderWorld().playSound(
                        null,
                        击杀者.position().x,
                        击杀者.position().y,
                        击杀者.position().z,
                        SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F);
            }
        }
    }

    // 在生成生物的事件中订阅
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().getType() == EntityType.ENDERMAN) {
            if (Math.random() < 转移概率.get()) {
                NBT工具.添加NBT("转移", true, event.getEntity());
            }
        }
    }
}
