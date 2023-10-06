package net.mltpoject.mine.mltpojectmonster.僵尸;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 感染者 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 感染者启用;

    public static ForgeConfigSpec.DoubleValue 感染者概率;

    public static ForgeConfigSpec.IntValue 感染者蠹虫生成最小值;

    public static ForgeConfigSpec.IntValue 感染者蠹虫生成最大值;

    public 感染者(){
        super("僵尸", "感染者");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        感染者启用 = 构建
                .comment("如果启用，僵尸有一定概率成为死亡后会生成蠹虫的僵尸")
                .define("感染者启用", true);

        感染者概率 = 构建
                .comment("僵尸成为感染者的概率")
                .defineInRange("感染者概率", 0.3, 0, 1);

        感染者蠹虫生成最小值 = 构建
                .comment("感染者死亡后生成蠹虫数量的最小值")
                .defineInRange("感染者蠹虫生成最小值", 1, 0, Integer.MAX_VALUE);

        感染者蠹虫生成最大值 = 构建
                .comment("感染者死亡后生成蠹虫数量的最大值")
                .defineInRange("感染者蠹虫生成最大值", 3, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Zombie) {
            if (!感染者启用.get()){
                return;
            }

            Zombie 僵尸 = (Zombie) event.getEntityLiving();

            if (!NBT工具.获取NBTBool("感染者", 僵尸)){
                return;
            }

            var 生成数量 = new Random().nextInt((感染者蠹虫生成最大值.get() - 感染者蠹虫生成最小值.get()) + 1) + 感染者蠹虫生成最小值.get();

            for (int i = 0; i < 生成数量; i++){
                Silverfish 新蠹虫 = new Silverfish(EntityType.SILVERFISH, 僵尸.getCommandSenderWorld());
                新蠹虫.setPos(僵尸.position());

                // 添加新蠹虫到世界
                僵尸.getCommandSenderWorld().addFreshEntity(新蠹虫);

                新蠹虫.getCommandSenderWorld().playSound(
                        null,
                        新蠹虫.position().x,
                        新蠹虫.position().y,
                        新蠹虫.position().z,
                        SoundEvents.SLIME_JUMP,
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F);
            }
        }
    }

    // 在生成生物的事件中订阅
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().getType() == EntityType.ZOMBIE) {
            if (Math.random() < 感染者概率.get()) {
                NBT工具.添加NBT("感染者", true, event.getEntity());
            }
        }
    }
}
