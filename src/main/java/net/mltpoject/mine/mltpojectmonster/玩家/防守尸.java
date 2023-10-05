package net.mltpoject.mine.mltpojectmonster.玩家;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 防守尸 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 防守尸启用;

    public static ForgeConfigSpec.IntValue 防守尸无敌时间;

    public 防守尸(){
        super("玩家", "防守尸");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        防守尸启用 = 构建
                .comment("如果启用，玩家复活后会获得一段时间的无敌效果。(抗性提升10与生命恢复10)")
                .define("防守尸启用", true);
        防守尸无敌时间 = 构建
                .comment("玩家复活后的无敌时间，以刻为计时，默认为300")
                .defineInRange("防守尸无敌时间", 300, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            if (!防守尸启用.get()){
                return;
            }

            event.getPlayer().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 防守尸无敌时间.get(), 9));
            event.getPlayer().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 防守尸无敌时间.get(), 9));
        }
    }
}
