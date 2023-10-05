package net.mltpoject.mine.mltpojectmonster;

import net.mltpoject.mine.mltpojectmonster.僵尸.飞雷神;
import net.mltpoject.mine.mltpojectmonster.女巫.引雷;
import net.mltpoject.mine.mltpojectmonster.末影人.呼唤;
import net.mltpoject.mine.mltpojectmonster.玩家.防守尸;
import net.mltpoject.mine.mltpojectmonster.蜘蛛.刺客;
import net.mltpoject.mine.mltpojectmonster.蠹虫.虫群;
import net.mltpoject.mine.mltpojectmonster.骷髅.医疗兵;

import java.util.ArrayList;

public class 能力列表 {
    public static ArrayList<能力基类> 列表 = new ArrayList<>();

    static {
        列表.add(new 防守尸());
        列表.add(new 医疗兵());
        列表.add(new 飞雷神());
        列表.add(new 呼唤());
        列表.add(new 虫群());
        列表.add(new 引雷());
        列表.add(new 刺客());
    }
}
