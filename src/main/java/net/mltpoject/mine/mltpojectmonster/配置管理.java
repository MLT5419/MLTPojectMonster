package net.mltpoject.mine.mltpojectmonster;

import net.minecraftforge.common.ForgeConfigSpec;

public class 配置管理 {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    static {
        重载();
    }

    public static void 重载(){
        for (var item : 能力列表.列表){
            item.获取配置(BUILDER);
        }

        SPEC = BUILDER.build();
    }
}
