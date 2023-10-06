package net.mltpoject.mine.mltpojectmonster;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 指令管理 {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> cmd = Commands.literal("mltpm")
                .requires(cs -> cs.hasPermission(2)) // 设置权限等级为2，通常管理员的权限等级为2或更高
                .then(Commands.literal("reload")
                        .executes(context -> {
                            配置管理.重载();
                            context.getSource().sendSuccess(new TextComponent("銘洛天的怪物增强已重新加载。"), true);
                            return 1;
                        }));
        dispatcher.register(cmd);
    }
}
