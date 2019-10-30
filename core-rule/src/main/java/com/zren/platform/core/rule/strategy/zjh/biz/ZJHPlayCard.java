package com.zren.platform.core.rule.strategy.zjh.biz;

import com.zren.platform.common.util.constants.ChessCard;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.core.rule.enums.CardColorEnum;
import com.zren.platform.core.rule.enums.CardTypeEnum;
import com.zren.platform.core.rule.strategy.BasePlayCard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 比牌大小逻辑
 *
 * @author k.y
 * @version Id: PlayCard.java, v 0.1 2018年11月20日 下午10:26 k.y Exp $
 */
public class ZJHPlayCard extends BasePlayCard{

    private static final List<Integer> KING_CARD = Arrays.asList(54, 55);
    private static final Integer ONE_ROUND = 13;
    private static final int ZJH_SINGLE_BIG = 14;
    private static final int[] ZJH_SMALLEST = new int[]{2, 3, 5};

    private static final List<Integer> WHOLE_CARD_WITHOUT_KING;

    static {
        WHOLE_CARD_WITHOUT_KING = ChessCard.CHESS_CARD.stream().filter(t -> !KING_CARD.contains(t)).collect(Collectors.toList());
    }

    public static final Integer CARD_SIZE = ChessCard.CHESS_CARD.size();

    public static List<Integer> getWholeCardWithoutKing() {
        return WHOLE_CARD_WITHOUT_KING;
    }

    public static List<Integer> getWholeCards() {
        return ChessCard.CHESS_CARD;
    }

    public static CardColorEnum getCardColor(int code) {
        if (code < 0 || code > 54) {
            throw new RobotBizException(ErrorCodeEnum.ILLEGAL_CARD);
        }
        if (ChessCard.SPADES_CARD.contains(code)) {
            return CardColorEnum.SPADES;
        }
        if (ChessCard.RED_PEACH_CARD.contains(code)) {
            return CardColorEnum.RED_PEACH;
        }
        if (ChessCard.BLOCK_CARD.contains(code)) {
            return CardColorEnum.BLOCK;
        }
        if (ChessCard.PLUM_CARD.contains(code)) {
            return CardColorEnum.PLUM;
        }
        if (KING_CARD.contains(code)) {
            Boolean result = KING_CARD.get(0).intValue() == code;
            return result ? CardColorEnum.SMALL_KING : CardColorEnum.BIG_KING;
        }
        throw new RobotBizException(ErrorCodeEnum.ILLEGAL_CARD);
    }

    public static CardTypeEnum getCardType(int a, int b, int c) {

        CardColorEnum a_colorEnum = getCardColor(a);
        CardColorEnum b_colorEnum = getCardColor(b);
        CardColorEnum c_colorEnum = getCardColor(c);

        int[] ints = getOriginal(new int[]{a, b, c});
        Arrays.sort(ints);
        //花色相同
        if (a_colorEnum == b_colorEnum && b_colorEnum == c_colorEnum) {
            if ((ints[2] == ints[1] + 1) && (ints[1] == ints[0] + 1)) {
                return CardTypeEnum.SAME_COLOR_TOGETHER;//顺金
            } else {
                return CardTypeEnum.SAME_COLOR_UNTOGETHER;//金花
            }
        }
        //花色不相同
        if ((ints[2] == ints[1] + 1) && (ints[1] == ints[0] + 1)) {
            return CardTypeEnum.DIF_COLOR_TOGETHER;//顺子
        }
        if(ints[0] == ints[1] && ints[1]==ints[2]){
            return CardTypeEnum.THREE_SAME;  //豹子
        }
        if(ints[0] == ints[1] || ints[1]==ints[2]){
            return CardTypeEnum.TWO_SAME;//对子
        }
        return CardTypeEnum.SINGLE;
    }

    public static int[] getOriginal(int[] cards) {
        int[] result = new int[cards.length];
        for (int i = 0; i < cards.length; i++) {
            int num = genOriginalNum(cards[i]);
            result[i] = num;
        }
        Arrays.sort(result);
        return result;
    }

    private static int genOriginalNum(int card) {
        int num = card % ONE_ROUND;
        num = num == 0 ? ONE_ROUND : num;
        num = num == 1 ? ONE_ROUND + 1 : num;
        return num;
    }

    @Override
    public Boolean compareTo(int[] f_card, int[] t_card) {

        CardTypeEnum f_type = ZJHPlayCard.getCardType(f_card[0], f_card[1], f_card[2]);
        CardTypeEnum t_type = ZJHPlayCard.getCardType(t_card[0], t_card[1], t_card[2]);

        boolean isBiggest = false;
        boolean isSmallest = false;
        //判断特殊牌
        if (f_type == CardTypeEnum.THREE_SAME && t_type == CardTypeEnum.SINGLE) {
            isBiggest = isBiggest(f_card);
            isSmallest = isSmallest(t_card);
            if (isBiggest && isSmallest) {
                return false;
            }
        } else if (t_type == CardTypeEnum.THREE_SAME && f_type == CardTypeEnum.SINGLE) {
            isBiggest = isBiggest(t_card);
            isSmallest = isSmallest(f_card);
            if (isBiggest && isSmallest) {
                return true;
            }
        }

        if (f_type.getPriority() > t_type.getPriority()) { //被动方输
            return true;
        } else if (f_type.getPriority() < t_type.getPriority()) {//主动方输
            return false;
        } else {//牌类型相同，再比牌值

            int[] f_original = ZJHPlayCard.getOriginal(f_card);
            int[] t_original = ZJHPlayCard.getOriginal(t_card);

            int f_biggestCard = f_original[2];
            int t_biggestCard = t_original[2];

            CardColorEnum f_color = ZJHPlayCard.getCardColor(f_card[2]);
            CardColorEnum t_color = ZJHPlayCard.getCardColor(t_card[2]);

            /**
             * 参数说明：
             *
             * flag为true:第一个参数大,  为false:第二个参数大
             */
            Boolean flag=false;
            switch (f_type) {
                case THREE_SAME:
                    //直接比大小
                    if (f_biggestCard > t_biggestCard) {
                        flag=true;
                    } else {
                        flag=false;
                    }
                    break;
                case SAME_COLOR_TOGETHER:
//                    顺金先比牌点再比花色
                case SAME_COLOR_UNTOGETHER:
                case DIF_COLOR_TOGETHER:
//                    金花、顺子比最大的牌的牌点再比花色
                    if (f_biggestCard > t_biggestCard) {
                        flag=true;
                    } else if(f_biggestCard < t_biggestCard){
                        flag=false;
                    }else {
                        if (f_color.getPriority() > t_color.getPriority()) {
                            flag=true;
                        } else if (f_color.getPriority() < t_color.getPriority()) {
                            flag=false;
                        }
                    }
                    break;
                case SINGLE:
                    //比大小，再比花色
                    if (f_biggestCard > t_biggestCard) {
                        flag=true;
                    } else if (f_biggestCard < t_biggestCard) {
                        flag=false;
                    } else {//大小相同
                        if (f_color.getPriority() > t_color.getPriority()) {
                            flag=true;
                        } else {
                            flag=false;
                        }
                    }
                    break;
                case TWO_SAME:
                    //比大小，再比花色
                    f_biggestCard = f_original[1];
                    t_biggestCard = t_original[1];

                    if (f_biggestCard > t_biggestCard) {
                        flag=true;
                    } else if (f_biggestCard < t_biggestCard) {
                        flag=false;
                    } else {//大小相同
                        //比单牌
                        int f_single = f_biggestCard != f_original[0] ? f_original[0] : f_original[2];
                        int t_single = t_biggestCard != t_original[0] ? t_original[0] : t_original[2];

                        if (f_single > t_single) {
                            flag=true;
                        } else if (f_single < t_single) {
                            flag=false;
                        } else {
                            f_color = getCardColorByOrig(f_card, f_single);
                            t_color = getCardColorByOrig(t_card, t_single);
                            if (f_color.getPriority() > t_color.getPriority()) {
                                flag=true;
                            } else {
                                flag=false;
                            }
                        }
                    }
                    break;
                default:
                    throw new RobotBizException(ErrorCodeEnum.COMPARE_CARD_EXCP);
            }
            return flag;
        }
    }

    private static boolean isSmallest(int[] t_card) {
        CardTypeEnum cardTypeEnum = getCardType(t_card[0],t_card[1],t_card[2]);
        int[] small = getOriginal(t_card);
        return Arrays.equals(small, ZJH_SMALLEST) && (cardTypeEnum != CardTypeEnum.SAME_COLOR_TOGETHER && cardTypeEnum != CardTypeEnum.SAME_COLOR_UNTOGETHER);
    }

    private static boolean isBiggest(int[] f_card) {
        int big = genOriginalNum(f_card[0]);
        if (big == ZJH_SINGLE_BIG) {
            return true;
        }
        return false;
    }

    private static CardColorEnum getCardColorByOrig(int[] f_card, int f_single) {
        for (int i = 0; i < f_card.length; i++) {
            if (f_single == genOriginalNum(f_card[i])) {
                return getCardColor(f_card[i]);
            }
        }
        throw new RobotBizException(ErrorCodeEnum.COMPARE_CARD_EXCP);
    }

    public static void main(String[] args) {
        int[] ss = {2, 3, 5};
        System.out.println(Arrays.equals(ss, ZJH_SMALLEST));
    }

    public static String findRealCard(int[] cards){
        StringBuffer sb=new StringBuffer();
        Arrays.stream(cards).forEach(s->{
            switch (s){
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    sb.append(s);
                    break;
                case 10:
                    sb.append("T");
                    break;
                case 11:
                    sb.append("J");
                    break;
                case 12:
                    sb.append("Q");
                    break;
                case 13:
                    sb.append("K");
                    break;
                case 14:
                    sb.append("A");
                    break;
            }
        });
        return sb.toString();
    }

    public static String single(int maxCard){
        switch (maxCard){
            case 10:
                return "T";
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            case 14:
                return "A";
            default:
                return String.valueOf(maxCard);
        }
    }

}