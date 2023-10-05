package net.mltpoject.mine.mltpojectmonster;

import net.minecraftforge.common.ForgeConfigSpec;

public class 配置管理 {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        for (var item : 能力列表.列表){
            item.获取配置(BUILDER);
        }

        SPEC = BUILDER.build();
    }
}
