package net.mltpoject.mine.mltpojectmonster.骷髅;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 医疗兵 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 骷髅仅限弓箭;

    public static ForgeConfigSpec.BooleanValue 骷髅医疗兵启用;

    public static ForgeConfigSpec.BooleanValue 骷髅非医疗兵豁免;

    public static ForgeConfigSpec.DoubleValue 骷髅医疗兵概率;

    public static ForgeConfigSpec.IntValue 骷髅医疗兵范围;

    public static ForgeConfigSpec.DoubleValue 骷髅医疗兵触发比例;

    public static ForgeConfigSpec.DoubleValue 骷髅医疗兵恢复比例;

    public static ForgeConfigSpec.IntValue 骷髅医疗兵固定恢复;

    public static ForgeConfigSpec.DoubleValue 骷髅医疗兵停止比例;
    
    public 医疗兵(){
        super("骷髅", "医疗兵");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        骷髅仅限弓箭 = 构建
                .comment("骷髅只有在持有弓箭时才会触发能力效果")
                .define("骷髅仅限弓箭", true);

        骷髅医疗兵启用 = 构建
                .comment("骷髅有一定概率会成为医疗兵骷髅，医疗兵骷髅会主动瞄准生命值低于一定比例的队友并发射治疗箭")
                .define("骷髅医疗兵启用", true);

        骷髅非医疗兵豁免 = 构建
                .comment("骷髅就算不是医疗兵也不会对友军造成伤害")
                .define("骷髅非医疗兵豁免", true);

        骷髅医疗兵概率 = 构建
                .comment("骷髅成为一个医疗兵的概率")
                .defineInRange("骷髅医疗兵概率", 0.5, 0, 1);

        骷髅医疗兵范围 = 构建
                .comment("骷髅医疗兵选择队友的范围")
                .defineInRange("骷髅医疗兵范围", 24, 0, Integer.MAX_VALUE);

        骷髅医疗兵触发比例 = 构建
                .comment("骷髅医疗兵在发现一名生命值低于多少的队友后会将其选作目标")
                .defineInRange("骷髅医疗兵触发比例", 0.7, 0, 1);

        骷髅医疗兵恢复比例 = 构建
                .comment("骷髅医疗兵的每次治疗会恢复目标多少比例的生命值")
                .defineInRange("骷髅医疗兵恢复比例", 0.1, 0, 1);

        骷髅医疗兵固定恢复 = 构建
                .comment("骷髅医疗兵的每次治疗会恢复目标多少的固定生命值")
                .defineInRange("骷髅医疗兵固定恢复", 5, 0, Integer.MAX_VALUE);

        骷髅医疗兵停止比例 = 构建
                .comment("骷髅医疗兵在目标的生命值高于多少以后主动停止恢复")
                .defineInRange("骷髅医疗兵停止比例", 0.8, 0, 1);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Skeleton) {
            if (!骷髅医疗兵启用.get()){
                return;
            }

            Skeleton skeleton = (Skeleton) living;

            if (!NBT工具.获取NBTBool("医疗兵", skeleton)){
                return;
            }

            if (骷髅仅限弓箭.get()){
                if (!skeleton.getMainHandItem().getItem().equals(Items.BOW)){
                    return;
                }
            }

            目标检查(skeleton);
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        Entity 攻击者 = event.getSource().getEntity();
        LivingEntity 被攻击者 = event.getEntityLiving();

        if (攻击者 instanceof AbstractArrow){
            攻击者 = ((AbstractArrow) 攻击者).getOwner();
        }

        if (攻击者 instanceof Skeleton && 被攻击者 instanceof Monster) {
            CompoundTag nbtTag = 攻击者.getPersistentData().getCompound("mltpojectmonster");
            boolean 是医疗兵 = nbtTag.contains("医疗兵") && nbtTag.getBoolean("医疗兵");

            if (!骷髅非医疗兵豁免.get()){
                if (!是医疗兵){
                    return;
                }
            }

            event.setCanceled(true);

            if (是医疗兵){
                被攻击者.heal((float) (被攻击者.getMaxHealth() * 骷髅医疗兵恢复比例.get().doubleValue()));
                被攻击者.heal(骷髅医疗兵固定恢复.get());
            }
        }
    }

    private void 目标检查(Skeleton skeleton){
        for (Entity entity : skeleton.level.getEntities(skeleton, skeleton.getBoundingBox().inflate(骷髅医疗兵范围.get()))) {
            if (entity instanceof LivingEntity) {
                LivingEntity 新目标 = (LivingEntity) entity;
                if (新目标 instanceof Monster &&
                        新目标.getHealth() < 新目标.getMaxHealth() * 骷髅医疗兵触发比例.get() &&
                        !新目标.isDeadOrDying()) {
                    skeleton.setTarget(新目标);
                    return;
                }
            }
        }

        if (skeleton.getTarget() == null || skeleton.getTarget() instanceof Player){
            return;
        }

        if (skeleton.getTarget().getHealth() >= skeleton.getTarget().getMaxHealth() * 骷髅医疗兵停止比例.get() ||
                skeleton.getTarget().isDeadOrDying()){
            skeleton.setTarget(null);
        }
    }

    @Override
    protected void 当生成生物(LivingEntity 生物) {
        if (生物.getType() == EntityType.SKELETON) {
            if (Math.random() < 骷髅医疗兵概率.get()) {
                NBT工具.添加NBT("医疗兵", true, 生物);
            }
        }
    }
}
