package net.mltpoject.mine.mltpojectmonster.僵尸;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 感染者 extends 能力基类 {
    public 感染者(){
        super("僵尸", "感染者");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        super.当获取配置(构建);
    }
}
