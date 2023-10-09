package net.mltpoject.mine.mltpojectmonster;

import net.mltpoject.mine.mltpojectmonster.僵尸.感染者;
import net.mltpoject.mine.mltpojectmonster.僵尸.飞雷神;
import net.mltpoject.mine.mltpojectmonster.卫道士.午夜凶铃;
import net.mltpoject.mine.mltpojectmonster.女巫.引雷;
import net.mltpoject.mine.mltpojectmonster.掠夺者.等级掠夺;
import net.mltpoject.mine.mltpojectmonster.末影人.呼唤;
import net.mltpoject.mine.mltpojectmonster.末影人.转移;
import net.mltpoject.mine.mltpojectmonster.玩家.防守尸;
import net.mltpoject.mine.mltpojectmonster.蜘蛛.刺客;
import net.mltpoject.mine.mltpojectmonster.蠹虫.寄生;
import net.mltpoject.mine.mltpojectmonster.蠹虫.母虫;
import net.mltpoject.mine.mltpojectmonster.蠹虫.虫群;
import net.mltpoject.mine.mltpojectmonster.通用.没有逝;
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
        列表.add(new 转移());
        列表.add(new 寄生());
        列表.add(new 等级掠夺());
        列表.add(new 感染者());
        列表.add(new 母虫());
        列表.add(new 午夜凶铃());
        列表.add(new 没有逝());
    }
}
