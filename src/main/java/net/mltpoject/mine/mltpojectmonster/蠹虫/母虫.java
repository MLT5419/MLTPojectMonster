package net.mltpoject.mine.mltpojectmonster.蠹虫;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
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
public class 母虫 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 母虫启用;

    public static ForgeConfigSpec.DoubleValue 母虫概率;

    public static ForgeConfigSpec.IntValue 母虫寄生效果间隔;

    public static ForgeConfigSpec.DoubleValue 母虫寄生伤害比例;

    public 母虫(){
        super("蠹虫", "母虫");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        母虫启用 = 构建
                .comment("如果启用，蠹虫有一定概率会主动攻击并寄生其它怪物")
                .define("母虫启用", true);

        母虫概率 = 构建
                .comment("母虫能力出现的概率")
                .defineInRange("母虫概率", 0.25, 0, 1);

        母虫寄生效果间隔 = 构建
                .comment("被寄生的生物间隔多久触发一次寄生效果")
                .defineInRange("母虫寄生效果间隔", 100, 0, Integer.MAX_VALUE);

        母虫寄生伤害比例 = 构建
                .comment("被寄生的生物每次受到寄生伤害的比例值")
                .defineInRange("母虫寄生伤害比例", 0.1, 0, 1);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if(living instanceof Monster){
            var 寄生数量 = NBT工具.获取NBTInt("被母虫寄生", living);
            if (寄生数量 > 0){
                var 计时 = NBT工具.获取NBTInt("被母虫寄生效果计时", living);
                if (计时 > 0){
                    计时 -= 1;
                }
                else{
                    for (int i = 0; i < 寄生数量; i++){
                        Silverfish 新蠹虫 = new Silverfish(EntityType.SILVERFISH, living.getCommandSenderWorld());
                        新蠹虫.setPos(living.position());

                        // 添加新蠹虫到世界
                        living.getCommandSenderWorld().addFreshEntity(新蠹虫);

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
                    var 伤害量 = 寄生数量 * (living.getMaxHealth() * (float) 母虫寄生伤害比例.get().doubleValue());
                    living.hurt(DamageSource.GENERIC, 伤害量);
                    计时 = 母虫寄生效果间隔.get();
                }
                NBT工具.添加NBT("被母虫寄生效果计时", 计时, living);
            }
        }

        if (living instanceof Silverfish) {
            if (!母虫启用.get()){
                return;
            }

            Silverfish 蠹虫 = (Silverfish) living;

            if (!NBT工具.获取NBTBool("母虫", 蠹虫)){
                return;
            }

            if (蠹虫.getTarget() instanceof Monster){
                return;
            }

            for (Entity entity : 蠹虫.level.getEntities(蠹虫, 蠹虫.getBoundingBox().inflate(32))) {
                if (entity instanceof LivingEntity) {
                    LivingEntity 新目标 = (LivingEntity) entity;
                    if (新目标 instanceof Monster) {
                        蠹虫.setTarget(新目标);
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        Entity 攻击者 = event.getSource().getEntity();
        LivingEntity 被攻击者 = event.getEntityLiving();

        if (被攻击者 instanceof Monster && 攻击者 instanceof Silverfish) {
            Silverfish 蠹虫 = (Silverfish) 攻击者;

            if (!母虫启用.get()){
                return;
            }

            if (!NBT工具.获取NBTBool("母虫", 蠹虫)){
                return;
            }

            var 寄生数量 = NBT工具.获取NBTInt("被母虫寄生", 被攻击者);
            NBT工具.添加NBT("被母虫寄生", 寄生数量 + 1, 被攻击者);
            NBT工具.添加NBT("被母虫寄生效果计时", 母虫寄生效果间隔.get(), 被攻击者);
            蠹虫.remove(Entity.RemovalReason.KILLED);
        }
    }

    // 在生成生物的事件中订阅
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().getType() == EntityType.SILVERFISH) {
            if (Math.random() < 母虫概率.get()) {
                NBT工具.添加NBT("母虫", true, event.getEntity());
            }
        }
    }
}
