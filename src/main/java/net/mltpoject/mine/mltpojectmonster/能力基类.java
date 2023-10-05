package net.mltpoject.mine.mltpojectmonster;

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
}
