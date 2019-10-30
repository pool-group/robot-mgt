package com.zren.platform.core.rule.strategy.common.process;

import com.zren.platform.common.dal.po.ZjhStrategyPO;
import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.core.rule.entity.in.ZjhStrategyEntity;
import com.zren.platform.core.rule.enums.CardTypeEnum;
import com.zren.platform.core.rule.enums.RobotFeaturesEnum;
import com.zren.platform.core.rule.strategy.common.BaseAIStrategyCentralNervous;
import com.zren.platform.core.rule.strategy.common.BaseStrategy;
import com.zren.platform.core.rule.strategy.common.InvokeExact;
import com.zren.platform.core.rule.strategy.zjh.biz.ZJHPlayCard;

import java.util.Arrays;

/**
 * Analysis of AI Policy Processing
 *
 * @author k.y
 * @version Id: StrategyRuleProcess.java, v 0.1 2019年08月01日 下午17:13 k.y Exp $
 */
public interface AIStrategyRuleProcess extends BaseAIStrategyCentralNervous {

    /**
     * According to different types of strategies, they are analyzed separately.
     * Return to the final analysis of the card-watching, abandoning, matching and betting strategies
     * <p>
     * @param zse
     * @param po
     * @param out
     * @return
     */
    default ZjhStrategyInfoDTO analysisResult(ZjhStrategyEntity zse,ZjhStrategyPO po,ZjhStrategyInfoDTO out){
        analysisAction(zse);
        LogUtil.info(String.format("=========================== loading param..  zse=[ %s ]",zse));
        InvokeExact iexact=new InvokeExact(po);
        if(zse.getRound()==1){
            out.setLookRate(Double.valueOf(po.getLookStrategy()));
            out.setLookToGiveUpRate(Double.valueOf(po.getLookToGiveUpStrategy()));
            out.setGiveUpRate(Double.valueOf(po.getGiveUpStrategy()));
            out.setVsRate(Double.valueOf(po.getVsStrategy()));
            addbetUnlookInvoke(zse,iexact,out);
        }else {
            if(!zse.getIsRlook()){
                lookInvoke(zse,iexact,out);
                addbetUnlookInvoke(zse,iexact,out);
            }else {
                lookgiveupInvoke(zse,iexact,out);
                addbetInvoke(zse,iexact,out);
            }
            vsInvoke(zse,iexact,out);
        }
        out.setIndex(zse.getRound());
        out.setVsmin(po.getVsmin());
        out.setVsmax(po.getVsmax());
        out.setQuickThinkingRate(po.getQuickThinkingStrategy().doubleValue());
        out.setFollowRate(po.getFollowRate().doubleValue());
        return out;
    }

    default String analysisKey(ZjhStrategyEntity zse){
        CardTypeEnum cardType=analysisCardType(zse.getIsRlook(),zse.getCards());
        String cardKey=analysisCardKey(cardType,zse);
        Integer roundstr=zse.getRound().compareTo(2)>=0?2:1;
        return cardType.toString()+ BaseStrategy.mark+cardKey+ BaseStrategy.mark+String.valueOf(roundstr);
    }

    default CardTypeEnum analysisCardType(Boolean bool,int[] cards){
        if(bool){
            return ZJHPlayCard.getCardType(cards[0],cards[1],cards[2]);
        }else {
            return CardTypeEnum.UNKNOW;
        }
    }

    default String findRealCard(int[] cards){
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

    default String analysisCardKey(CardTypeEnum cardType,ZjhStrategyEntity zse){
        int[] realCards=ZJHPlayCard.getOriginal(zse.getCards());
        Arrays.sort(realCards);
        zse.setRealCard(findRealCard(realCards));
        switch (cardType){
            case UNKNOW:
                return "D";
            case SINGLE:
                return single(realCards[realCards.length-1]);
            case TWO_SAME:
                return twosame(realCards);
            case DIF_COLOR_TOGETHER:
                return "D";
            case SAME_COLOR_TOGETHER:
                return "D";
            case SAME_COLOR_UNTOGETHER:
                return sameColorUntogether(realCards[realCards.length-1]);
            case THREE_SAME:
                return "D";
            default:
                return "D";
        }
    }

    default String sameColorUntogether(int maxCard){
        if(maxCard<=10){
            return single(10);
        }else {
            return single(14);
        }

    }

    default String single(int maxCard){
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

    default String twosame(int[] realCards){
        if(realCards[0]==realCards[1]){
            return single(realCards[0]);
        }else {
            return single(realCards[1]);
        }
    }

    default void analysisAction(ZjhStrategyEntity zse){
        String[] dark=zse.getDark().split(",");
        String[] brights=zse.getBrights().split(",");
        Double lastBetScore=zse.getLastBetScore();
        if(!zse.getIsPlook()){
            if(lastBetScore.compareTo((double) 0)==1&&lastBetScore.compareTo(Double.valueOf(dark[0]))==-1)
                lastBetScore= Double.valueOf(dark[0]);
            for (int i=0;i<dark.length;i++){
                if(Double.parseDouble(dark[i])==lastBetScore){
                    if(i==0){
                        zse.setLevel(RobotFeaturesEnum.LEVEL_ONE);
                        zse.setBetType(RobotFeaturesEnum.FOLLOW);
                    }
                    else if(i==1)
                        zse.setLevel(RobotFeaturesEnum.LEVEL_TWO);
                    else if (i==2)
                        zse.setLevel(RobotFeaturesEnum.LEVEL_THREE);
                    else if (i==3)
                        zse.setLevel(RobotFeaturesEnum.LEVEL_FOUR);
                    else
                        throw new RobotSystemException(String.format("dark.size=[ %s ], analysis dark is Exception!",dark.length));
                }
            }
        }else {
            if(lastBetScore.compareTo((double) 0)==1&&lastBetScore.compareTo(Double.valueOf(brights[0]))==-1)
                lastBetScore= Double.valueOf(brights[0]);
            for (int i=0;i<brights.length;i++){
                if(Double.parseDouble(brights[i])==lastBetScore){
                    if(i==0){
                        zse.setLevel(RobotFeaturesEnum.LEVEL_ONE);
                        zse.setBetType(RobotFeaturesEnum.FOLLOW);
                    }
                    else if(i==1)
                        zse.setLevel(RobotFeaturesEnum.LEVEL_TWO);
                    else if (i==2)
                        zse.setLevel(RobotFeaturesEnum.LEVEL_THREE);
                    else if (i==3)
                        zse.setLevel(RobotFeaturesEnum.LEVEL_FOUR);
                    else
                        throw new RobotSystemException(String.format("brights.size=[ %s ], analysis brights is Exception!",brights.length));
                }
            }
        }
    }
}