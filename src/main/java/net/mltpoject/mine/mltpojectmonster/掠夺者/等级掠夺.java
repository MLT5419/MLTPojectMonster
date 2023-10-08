package net.mltpoject.mine.mltpojectmonster.掠夺者;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mltpoject.mine.mltpojectmonster.NBT工具;
import net.mltpoject.mine.mltpojectmonster.能力基类;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class 等级掠夺 extends 能力基类 {
    public static ForgeConfigSpec.BooleanValue 等级掠夺启用;

    public static ForgeConfigSpec.DoubleValue 等级掠夺概率;

    public 等级掠夺(){
        super("掠夺者", "等级掠夺");
    }

    @Override
    protected void 当获取配置(ForgeConfigSpec.Builder 构建) {
        等级掠夺启用 = 构建
                .comment("如果启用，掠夺者在命中玩家后会降低玩家的等级")
                .define("等级掠夺启用", true);

        等级掠夺概率 = 构建
                .comment("等级掠夺能力出现的概率")
                .defineInRange("等级掠夺概率", 0.25, 0, 1);
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        Entity 攻击者 = event.getSource().getEntity();
        LivingEntity 被攻击者 = event.getEntityLiving();

        if (攻击者 instanceof AbstractArrow){
            攻击者 = ((AbstractArrow) 攻击者).getOwner();
        }

        if (被攻击者 instanceof Player && 攻击者 instanceof Pillager) {
            Pillager 掠夺者 = (Pillager) 攻击者;

            if (!等级掠夺启用.get()){
                return;
            }

            if (!NBT工具.获取NBTBool("等级掠夺", 掠夺者)){
                return;
            }

            ((Player) 被攻击者).giveExperienceLevels(-1);

            TextComponent message = new TextComponent("掠夺者夺取了你的等级");
            发送能力提示(message, (Player) 被攻击者);
        }
    }

    @Override
    protected void 当生成生物(LivingEntity 生物) {
        if (生物.getType() == EntityType.PILLAGER) {
            if (Math.random() < 等级掠夺概率.get()) {
                NBT工具.添加NBT("等级掠夺", true, 生物);
            }
        }
    }
}
