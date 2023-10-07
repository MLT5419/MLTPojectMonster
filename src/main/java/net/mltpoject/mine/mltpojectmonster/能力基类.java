package net.mltpoject.mine.mltpojectmonster;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

public class 能力基类 {
    public String 总类名;
    public String 能力名;

    public 能力基类(String 总类名, String 能力名){
        this.总类名 = 总类名;
        this.能力名 = 能力名;
    }

    public void 获取配置(ForgeConfigSpec.Builder 构建){
        构建.push(总类名);
        构建.push(能力名);

        当获取配置(构建);

        构建.pop();
        构建.pop();
    }

    protected void 当获取配置(ForgeConfigSpec.Builder 构建){

    }

    public void 主动生成生物(LivingEntity 生物, LivingEntity 生成源){
        生成源.getCommandSenderWorld().addFreshEntity(生物);
        for (var item : 能力列表.列表){
            item.当主动生成生物(生物, 生成源);
        }
    }

    protected void 当主动生成生物(LivingEntity 生物, LivingEntity 生成源){

    }

    public void 发送能力提示(TextComponent 内容, Player 玩家){
        if (!配置管理.启用聊天栏状态输出.get()) {
            return;
        }

        玩家.sendMessage(内容, 玩家.getUUID());
    }
}
