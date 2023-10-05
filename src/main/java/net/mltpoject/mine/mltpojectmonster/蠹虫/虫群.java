package net.mltpoject.mine.mltpojectmonster.蠹虫;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 虫群 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 虫群启用;

    public static ForgeConfigSpec.BooleanValue 虫群防止无目标启用;

    public static ForgeConfigSpec.DoubleValue 虫群概率;

    public static ForgeConfigSpec.DoubleValue 虫群召唤概率;

    public static ForgeConfigSpec.DoubleValue 虫群必定召唤生命比例;

    public 虫群(){
        super("蠹虫", "虫群");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        虫群启用 = 构建
                .comment("如果启用，蠹虫有一定概率会获得受伤后一定概率在攻击者位置召唤新的蠹虫的能力")
                .define("虫群启用", true);

        虫群防止无目标启用 = 构建
                .comment("如果启用，如果没有伤害来源则会在32格内寻找一名玩家生成蠹虫")
                .define("虫群防止无目标启用", true);

        虫群概率 = 构建
                .comment("虫群能力出现的概率")
                .defineInRange("虫群概率", 0.5, 0, 1);

        虫群召唤概率 = 构建
                .comment("蠹虫在受到伤害后召唤新蠹虫的概率")
                .defineInRange("虫群召唤概率", 0.25, 0, 1);

        虫群必定召唤生命比例 = 构建
                .comment("蠹虫的生命值低于该比例后受到伤害必定会召唤新蠹虫")
                .defineInRange("虫群必定召唤生命比例", 0.5, 0, 1);
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        Entity 攻击者 = event.getSource().getEntity();
        LivingEntity 被攻击者 = event.getEntityLiving();

        if (攻击者 instanceof AbstractArrow){
            攻击者 = ((AbstractArrow) 攻击者).getOwner();
        }

        if (攻击者 == null){
            if (!虫群防止无目标启用.get()){
                return;
            }
            for (Entity entity : 被攻击者.level.getEntities(被攻击者, 被攻击者.getBoundingBox().inflate(32))) {
                if (entity instanceof LivingEntity) {
                    LivingEntity 单位 = (LivingEntity) entity;
                    if (单位 instanceof Player) {
                        攻击者 = 单位;
                        break;
                    }
                }
            }
        }

        if (被攻击者 instanceof Silverfish && 攻击者 instanceof Player) {
            if (!虫群启用.get()){
                return;
            }

            if (!NBT工具.获取NBTBool("虫群", 被攻击者)){
                return;
            }

            Silverfish 蠹虫 = (Silverfish) event.getEntityLiving();

            // 如果蠹虫的生命值低于最大生命值的一定比例则必定生成新蠹虫
            if (!(蠹虫.getHealth() < 蠹虫.getMaxHealth() * 虫群必定召唤生命比例.get())){
                // 否则随机生成新蠹虫
                if (!(Math.random() < 虫群召唤概率.get())) {
                    return;
                }
            }

            // 在攻击者位置生成新蠹虫
            Silverfish 新蠹虫 = new Silverfish(EntityType.SILVERFISH, 蠹虫.getCommandSenderWorld());
            新蠹虫.setPos(攻击者.position());
            新蠹虫.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(蠹虫.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 1.25);

            // 添加新蠹虫到世界
            蠹虫.getCommandSenderWorld().addFreshEntity(新蠹虫);

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
    }

    // 在生成生物的事件中订阅
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().getType() == EntityType.SILVERFISH) {
            if (Math.random() < 虫群概率.get()) {
                NBT工具.添加NBT("虫群", true, event.getEntity());
            }
        }
    }
}
