package net.mltpoject.mine.mltpojectmonster;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.mltpoject.mine.mltpojectmonster.僵尸.飞雷神;
import net.mltpoject.mine.mltpojectmonster.女巫.引雷;
import net.mltpoject.mine.mltpojectmonster.末影人.呼唤;
import net.mltpoject.mine.mltpojectmonster.玩家.防守尸;
import net.mltpoject.mine.mltpojectmonster.蜘蛛.刺客;
import net.mltpoject.mine.mltpojectmonster.蠹虫.虫群;
import net.mltpoject.mine.mltpojectmonster.骷髅.医疗兵;
import org.slf4j.Logger;

import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("mltpojectmonster")
public class MLTPojectMonster {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MLTPojectMonster() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, 配置管理.SPEC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        for (var item : 能力列表.列表){
            MinecraftForge.EVENT_BUS.register(item);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("我有一个阴谋.....");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
