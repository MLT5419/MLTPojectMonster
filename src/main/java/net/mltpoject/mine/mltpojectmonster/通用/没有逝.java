package net.mltpoject.mine.mltpojectmonster.通用;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 没有逝 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 没有逝启用;

    public static ForgeConfigSpec.BooleanValue 没有逝玩家生效;

    public static ForgeConfigSpec.BooleanValue 没有逝恢复效果;

    public static ForgeConfigSpec.DoubleValue 没有逝直接恢复比例;

    public static ForgeConfigSpec.IntValue 没有逝起始恢复等级;

    public static ForgeConfigSpec.IntValue 没有逝等级递增;

    public static ForgeConfigSpec.IntValue 没有逝最大恢复等级;

    public static ForgeConfigSpec.IntValue 没有逝恢复持续时间;

    public 没有逝(){
        super("通用", "没有逝");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        没有逝启用 = 构建
                .comment("如果启用，当一个生物击杀玩家后会获得生命恢复")
                .define("没有逝启用", true);

        没有逝玩家生效 = 构建
                .comment("如果启用，玩家击杀玩家也会获得恢复效果")
                .define("没有逝玩家生效", true);

        没有逝恢复效果 = 构建
                .comment("如果启用，在基础的恢复效果之上还会提供递增的生命恢复效果")
                .define("没有逝恢复效果", true);

        没有逝直接恢复比例 = 构建
                .comment("击杀玩家后直接恢复给予血量上限的生命值")
                .defineInRange("没有逝直接恢复比例", 0.35, 0, 1);

        没有逝起始恢复等级 = 构建
                .comment("第一次击杀玩家后获得的生命恢复效果等级")
                .defineInRange("没有逝起始恢复等级", 0, 0, Integer.MAX_VALUE);

        没有逝等级递增 = 构建
                .comment("已经持有恢复效果的生物再次击杀玩家会增加的等级")
                .defineInRange("没有逝等级递增", 1, 0, Integer.MAX_VALUE);

        没有逝最大恢复等级 = 构建
                .comment("通过此能力最高叠加的恢复等级，如果超出等级继续触发则只会刷新持续时间(在持续时间低于设定值的情况)")
                .defineInRange("没有逝最大恢复等级", 9, 0, Integer.MAX_VALUE);

        没有逝恢复持续时间 = 构建
                .comment("通过此能力施加的生命恢复效果的持续时间")
                .defineInRange("没有逝恢复持续时间", 300, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event){
        if (event.getEntityLiving() instanceof Player){
            var 凶手 = event.getSource().getEntity();

            if (凶手 instanceof AbstractArrow){
                凶手 = ((AbstractArrow) 凶手).getOwner();
            }

            if ((!(凶手 instanceof Player) || 没有逝玩家生效.get()) && 凶手 instanceof LivingEntity){
                var 活着的凶手 = (LivingEntity) 凶手;

                活着的凶手.heal(活着的凶手.getMaxHealth() * (float) 没有逝直接恢复比例.get().doubleValue());

                if (!没有逝恢复效果.get()){
                    return;
                }

                MobEffectInstance 当前效果 = 活着的凶手.getEffect(MobEffects.REGENERATION);

                if (当前效果 != null) {
                    int 剩余时间 = 当前效果.getDuration();
                    int 当前等级 = 当前效果.getAmplifier();

                    if (剩余时间 >= 没有逝恢复持续时间.get() && 当前等级 >= 没有逝最大恢复等级.get()){
                        return;
                    }

                    int 目标等级 = 没有逝起始恢复等级.get();
                    int 目标时间 = 没有逝恢复持续时间.get();


                    if (当前等级 >= 没有逝最大恢复等级.get()){
                        目标等级 = 当前等级;
                    }
                    else if (当前等级 >= 没有逝起始恢复等级.get()){
                        目标等级 = 当前等级 + 没有逝等级递增.get();
                    }

                    if (剩余时间 >= 没有逝恢复持续时间.get()){
                        目标时间 = 剩余时间;
                    }

                    活着的凶手.removeEffect(MobEffects.REGENERATION);
                    MobEffectInstance newEffect = new MobEffectInstance(MobEffects.REGENERATION, 目标时间, 目标等级, false, false);
                    活着的凶手.addEffect(newEffect);
                }
                else {
                    MobEffectInstance newEffect = new MobEffectInstance(MobEffects.REGENERATION, 没有逝恢复持续时间.get(), 没有逝起始恢复等级.get(), false, false);
                    活着的凶手.addEffect(newEffect);
                }
            }
        }
    }
}
