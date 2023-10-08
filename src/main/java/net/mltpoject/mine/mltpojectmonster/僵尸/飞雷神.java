package net.mltpoject.mine.mltpojectmonster.僵尸;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 飞雷神 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 启用飞雷神;

    public static ForgeConfigSpec.DoubleValue 飞雷神概率;

    public static ForgeConfigSpec.IntValue 飞雷神距离;

    public static ForgeConfigSpec.IntValue 飞雷神返回时间;

    public 飞雷神(){
        super("僵尸", "飞雷神");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        启用飞雷神 = 构建
                .comment("如果启用，僵尸会瞬移到一定范围内的玩家位置，并在一段时间后返回之前的位置")
                .define("启用飞雷神", true);

        飞雷神概率 = 构建
                .comment("僵尸可以进行飞雷神的概率")
                .defineInRange("飞雷神概率", 0.35, 0, 1);

        飞雷神距离 = 构建
                .comment("飞雷神最远的距离")
                .defineInRange("飞雷神距离", 16, 0, Integer.MAX_VALUE);

        飞雷神返回时间 = 构建
                .comment("飞雷神生效后多久僵尸会返回原位")
                .defineInRange("飞雷神返回时间", 100, 0, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Zombie) {
            if (!启用飞雷神.get()) {
                return;
            }

            Zombie 僵尸 = (Zombie) living;
            CompoundTag nbtTag = 僵尸.getPersistentData().getCompound("mltpojectmonster");
            boolean 飞雷神返回 = nbtTag.contains("飞雷神返回") && nbtTag.getBoolean("飞雷神返回");

            if (飞雷神返回) {
                if (nbtTag.contains("飞雷神返回计时")) {
                    int 返回计时 = nbtTag.getInt("飞雷神返回计时");
                    if (返回计时 > 0) {
                        返回计时 -= 1;

                        nbtTag.putInt("飞雷神返回计时", 返回计时);
                        僵尸.getPersistentData().put("mltpojectmonster", nbtTag);
                        return;
                    }
                }

                if (nbtTag.contains("飞雷神返回位置X") && nbtTag.contains("飞雷神返回位置Y") && nbtTag.contains("飞雷神返回位置Z")) {
                    double PosX = nbtTag.getDouble("飞雷神返回位置X");
                    double PosY = nbtTag.getDouble("飞雷神返回位置Y");
                    double PosZ = nbtTag.getDouble("飞雷神返回位置Z");

                    僵尸.getCommandSenderWorld().playSound(
                            null,
                            僵尸.position().x,
                            僵尸.position().y,
                            僵尸.position().z,
                            SoundEvents.ENDERMAN_TELEPORT,
                            SoundSource.NEUTRAL,
                            1.0F,
                            1.0F);
                    僵尸.setPos(PosX, PosY, PosZ);
                }

                nbtTag.putBoolean("飞雷神返回", false);
                僵尸.getPersistentData().put("mltpojectmonster", nbtTag);

                return;
            }

            if (僵尸.getTarget() instanceof Player) {
                if (!NBT工具.获取NBTBool("飞雷神", 僵尸)){
                    return;
                }

                Player 目标玩家 = (Player) 僵尸.getTarget();
                double 距离 = 获取距离(僵尸, 目标玩家);

                if (距离 <= 飞雷神距离.get()) {
                    僵尸.getCommandSenderWorld().playSound(
                            null,
                            僵尸.position().x,
                            僵尸.position().y,
                            僵尸.position().z,
                            SoundEvents.ENDERMAN_TELEPORT,
                            SoundSource.NEUTRAL,
                            1.0F,
                            1.0F);

                    // 添加 NBT 标签
                    nbtTag.putDouble("飞雷神返回位置X", 僵尸.position().x);
                    nbtTag.putDouble("飞雷神返回位置Y", 僵尸.position().y);
                    nbtTag.putDouble("飞雷神返回位置Z", 僵尸.position().z);

                    僵尸.setPos(目标玩家.position());

                    TextComponent message = new TextComponent("一个僵尸对你使用了飞雷神");
                    发送能力提示(message, 目标玩家);

                    nbtTag.putBoolean("飞雷神", false);
                    nbtTag.putBoolean("飞雷神返回", true);
                    nbtTag.putInt("飞雷神返回计时", 飞雷神返回时间.get());

                    僵尸.getPersistentData().put("mltpojectmonster", nbtTag);
                }
            }
        }
    }

    @Override
    protected void 当生成生物(LivingEntity 生物) {
        if (生物.getType() == EntityType.ZOMBIE) {
            if (Math.random() < 飞雷神概率.get()) {
                NBT工具.添加NBT("飞雷神", true, 生物);
            }
        }
    }

    public int 获取距离(Entity entity1, Entity entity2) {
        var pos1 = entity1.position();
        var pos2 = entity2.position();

        double distance = Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2) + Math.pow(pos1.z - pos2.z, 2));

        return (int) Math.round(distance);
    }
}
