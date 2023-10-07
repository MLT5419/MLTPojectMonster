package net.mltpoject.mine.mltpojectmonster;

import net.minecraftforge.common.ForgeConfigSpec;

public class 配置管理 {
    public static ForgeConfigSpec.Builder BUILDER;
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue 启用聊天栏状态输出;

    static {
        重载();
    }

    public static void 重载(){
        BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("全局");

        启用聊天栏状态输出 = BUILDER
                .comment("如果启用，部分特殊能力在对玩家造成影响时会对玩家发送特殊提示")
                .define("启用聊天栏状态输出", true);

        BUILDER.pop();

        for (var item : 能力列表.列表){
            item.获取配置(BUILDER);
        }

        SPEC = BUILDER.build();
    }
}
