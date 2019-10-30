package com.zren.platform.common.util.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 54张棋牌卡片
 *
 * @author k.y
 * @version Id: ChessUtil.java, v 0.1 2018年11月17日 下午17:56 k.y Exp $
 */
public class ChessCard {

    /**牌最小编号*/
    public final static Integer MIN_INDEX = 2;

    /**牌最大编号*/
    public final static Integer MAX_INDEX = 55;

    /**牌隔符号*/
    public final static String MARK_UNLINE = "_";

    /**所有牌型*/
    public static final List<Integer> CHESS_CARD = Arrays.asList(
            2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
            28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53,
            54, 55);

    /**黑桃*/
    public static final List<Integer> SPADES_CARD = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);

    /**红桃*/
    public static final List<Integer> RED_PEACH_CARD = Arrays.asList(15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);

    /**梅花*/
    public static final List<Integer> PLUM_CARD = Arrays.asList(28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40);

    /**方块*/
    public static final List<Integer> BLOCK_CARD = Arrays.asList(41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);

    /**小大王*/
    public static final List<Integer> KING_CARD = Arrays.asList(54, 55);

}
