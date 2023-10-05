package net.mltpoject.mine.mltpojectmonster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NBT工具 {
    public static void 添加NBT(String ID, boolean 值, Entity 实体){
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean(ID, 值);
        实体.getPersistentData().put("mltpojectmonster", nbt);
    }

    public static void 添加NBT(String ID, int 值, Entity 实体){
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(ID, 值);
        实体.getPersistentData().put("mltpojectmonster", nbt);
    }

    public static void 添加NBT(String ID, double 值, Entity 实体){
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble(ID, 值);
        实体.getPersistentData().put("mltpojectmonster", nbt);
    }

    public static boolean 获取NBTBool(String ID, Entity 实体){
        CompoundTag nbtTag = 实体.getPersistentData().getCompound("mltpojectmonster");
        return nbtTag.contains(ID) && nbtTag.getBoolean(ID);
    }

    public static int 获取NBTInt(String ID, Entity 实体){
        CompoundTag nbtTag = 实体.getPersistentData().getCompound("mltpojectmonster");
        if (!nbtTag.contains(ID)){
            return 0;
        }
        return nbtTag.getInt(ID);
    }

    public static double 获取NBTDouble(String ID, Entity 实体){
        CompoundTag nbtTag = 实体.getPersistentData().getCompound("mltpojectmonster");
        if (!nbtTag.contains(ID)){
            return 0;
        }
        return nbtTag.getDouble(ID);
    }
}
