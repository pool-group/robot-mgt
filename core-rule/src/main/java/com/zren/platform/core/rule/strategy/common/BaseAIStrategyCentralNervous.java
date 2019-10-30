package com.zren.platform.core.rule.strategy.common;

import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import com.zren.platform.core.rule.entity.in.ZjhStrategyEntity;
import com.zren.platform.core.rule.enums.RobotFeaturesEnum;

/**
 * Classification and Analysis of AI Policies
 *
 * @author k.y
 * @version Id: AIStrategyRule.java, v 0.1 2019年08月02日 下午14:08 k.y Exp $
 */
public interface BaseAIStrategyCentralNervous {

    /**
     * Analysis of Behavior Strategies of Looking at Cards and Find out the Relevant Results
     * There are altogether 15 kinds of behavioral strategies for card-watching.
     *
     * @param zse
     * @param iexact
     */
    default void lookInvoke(ZjhStrategyEntity zse,InvokeExact iexact,ZjhStrategyInfoDTO out){
        String Ax1=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A1":null;
        String Ax2=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A2":null;
        String Ax3=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A3":null;
        String Ax4=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A4":null;
        String Ax5=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A5":null;
        String Ax6=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A6":null;
        String Ax7=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A7":null;
        String Ax8=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A8":null;
        String Ax9=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A9":null;
        String Ax10=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A10":null;
        String Ax11=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A11":null;
        String Ax12=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A12":null;
        String Ax13=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A13":null;
        String Ax14=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A14":null;
        String Ax15=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A15":null;
        String Ax16=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A4":null;
        String Ax17=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A5":null;
        String Ax18=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A9":null;
        String Ax19=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A10":null;
        String Ax20=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A14":null;
        String Ax21=zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A15":null;
        String Ax22=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"A2":null;
        String Ax23=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"A7":null;
        String Ax24=!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"A12":null;
        iexact.bindAx(Ax1).bindAx(Ax2).bindAx(Ax3).bindAx(Ax4).bindAx(Ax5).bindAx(Ax6).bindAx(Ax7).bindAx(Ax8).bindAx(Ax9).bindAx(Ax10).bindAx(Ax11)
                .bindAx(Ax11).bindAx(Ax12).bindAx(Ax13).bindAx(Ax14).bindAx(Ax15).bindAx(Ax16).bindAx(Ax17).bindAx(Ax18).bindAx(Ax19).bindAx(Ax20).bindAx(Ax21).bindAx(Ax22).bindAx(Ax23).bindAx(Ax24);
        out.setLookRate(iexact.analysisLook().doubleValue());
    }

    /**
     * Analysis of Behavior Strategies of Watching and Abandoning, Find out the Relevant Result
     * There are altogether 30 kinds of behavioral strategies for card-watching.
     *
     * @param zse
     * @param iexact
     */
    default void lookgiveupInvoke(ZjhStrategyEntity zse,InvokeExact iexact,ZjhStrategyInfoDTO out){
        String Bx1=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B1":null;
        String Bx2=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B2":null;
        String Bx3=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B3":null;
        String Bx4=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B4":null;
        String Bx5=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B5":null;
        String Bx6=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B6":null;
        String Bx7=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B7":null;
        String Bx8=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B8":null;
        String Bx9=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B9":null;
        String Bx10=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B10":null;
        String Bx11=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B11":null;
        String Bx12=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B12":null;
        String Bx13=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B13":null;
        String Bx14=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B14":null;
        String Bx15=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B15":null;
        String Bx16=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B16":null;
        String Bx17=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B17":null;
        String Bx18=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B18":null;
        String Bx19=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B19":null;
        String Bx20=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B20":null;
        String Bx21=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B21":null;
        String Bx22=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B22":null;
        String Bx23=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B23":null;
        String Bx24=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B24":null;
        String Bx25=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B25":null;
        String Bx26=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B26":null;
        String Bx27=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B27":null;
        String Bx28=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B28":null;
        String Bx29=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B29":null;
        String Bx30=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B30":null;
        String Bx31=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B2":null;
        String Bx32=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B7":null;
        String Bx33=zse.getPableCount()==2&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B12":null;
        String Bx34=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B4":null;
        String Bx35=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B5":null;
        String Bx36=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B9":null;
        String Bx37=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B10":null;
        String Bx38=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B14":null;
        String Bx39=zse.getPableCount()==2&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B15":null;
        String Bx40=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B17":null;
        String Bx41=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B22":null;
        String Bx42=zse.getPableCount()>=3&&!zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B27":null;
        String Bx43=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B19":null;
        String Bx44=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"B20":null;
        String Bx45=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B24":null;
        String Bx46=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"B25":null;
        String Bx47=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B29":null;
        String Bx48=zse.getPableCount()>=3&&zse.getIsPlook()&&zse.getBetType()== RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"B30":null;
        iexact.bindBx(Bx1).bindBx(Bx2).bindBx(Bx3).bindBx(Bx4).bindBx(Bx5).bindBx(Bx6).bindBx(Bx7).bindBx(Bx8).bindBx(Bx9).bindBx(Bx10).bindBx(Bx11).bindBx(Bx12)
                .bindBx(Bx13).bindBx(Bx14).bindBx(Bx15).bindBx(Bx16).bindBx(Bx17).bindBx(Bx18).bindBx(Bx19).bindBx(Bx20).bindBx(Bx21).bindBx(Bx22).bindBx(Bx23).bindBx(Bx24)
                .bindBx(Bx25).bindBx(Bx26).bindBx(Bx27).bindBx(Bx28).bindBx(Bx29).bindBx(Bx30).bindBx(Bx31).bindBx(Bx32).bindBx(Bx33).bindBx(Bx34).bindBx(Bx35).bindBx(Bx36).bindBx(Bx37)
                .bindBx(Bx38).bindBx(Bx39).bindBx(Bx40).bindBx(Bx41).bindBx(Bx42).bindBx(Bx43).bindBx(Bx44).bindBx(Bx45).bindBx(Bx46).bindBx(Bx47).bindBx(Bx48);
        out.setLookToGiveUpRate(iexact.analysisGiveup().doubleValue());
        out.setGiveUpRate(iexact.analysisGiveup().doubleValue());
    }


    /**
     * Analysis of the Behavior Strategies of Bipai and Find out the Relevant Results
     * There are altogether 27 kinds of behavioral strategies for card-watching.
     *
     * @param zse
     * @param iexact
     */
    default void vsInvoke(ZjhStrategyEntity zse,InvokeExact iexact,ZjhStrategyInfoDTO out){
        String Cx1=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C1":null;
        String Cx2=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C2":null;
        String Cx3=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C3":null;
        String Cx4=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C4":null;
        String Cx5=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C5":null;
        String Cx6=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C6":null;
        String Cx7=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C7":null;
        String Cx8=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C8":null;
        String Cx9=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C9":null;
        String Cx10=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C10":null;
        String Cx11=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C11":null;
        String Cx12=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C12":null;
        String Cx13=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C13":null;
        String Cx14=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C14":null;
        String Cx15=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C15":null;
        String Cx16=zse.getIsRlook()&&!zse.getIsPlook()&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C16":null;
        String Cx17=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C17":null;
        String Cx18=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C18":null;
        String Cx19=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C19":null;
        String Cx20=zse.getIsRlook()&&!zse.getIsPlook()&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C20":null;
        String Cx21=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C21":null;
        String Cx22=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C22":null;
        String Cx23=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C23":null;
        String Cx24=zse.getIsRlook()&&!zse.getIsPlook()&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C24":null;
        String Cx25=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C25":null;
        String Cx26=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C26":null;
        String Cx27=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C27":null;
        String Cx28=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C2":null;
        String Cx29=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C7":null;
        String Cx30=!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C12":null;
        String Cx31=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C4":null;
        String Cx32=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C5":null;
        String Cx33=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C9":null;
        String Cx34=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C10":null;
        String Cx35=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C14":null;
        String Cx36=!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C15":null;
        String Cx37=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"C18":null;
        String Cx38=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"C22":null;
        String Cx39=zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"C26":null;
        iexact.bindCx(Cx1).bindCx(Cx2).bindCx(Cx3).bindCx(Cx4).bindCx(Cx5).bindCx(Cx6).bindCx(Cx7).bindCx(Cx8).bindCx(Cx9).bindCx(Cx10).bindCx(Cx11).bindCx(Cx12)
                .bindCx(Cx13).bindCx(Cx14).bindCx(Cx15).bindCx(Cx16).bindCx(Cx17).bindCx(Cx18).bindCx(Cx19).bindCx(Cx20).bindCx(Cx21).bindCx(Cx22).bindCx(Cx23).bindCx(Cx24)
                .bindCx(Cx25).bindCx(Cx26).bindCx(Cx27).bindCx(Cx28).bindCx(Cx29).bindCx(Cx30).bindCx(Cx31).bindCx(Cx32).bindCx(Cx33).bindCx(Cx34).bindCx(Cx35).bindCx(Cx36).bindCx(Cx37)
                .bindCx(Cx38).bindCx(Cx39);
        out.setVsRate(iexact.analysisVs().doubleValue());
    }


    /**
     * Analyzing the behavior strategy of adding notes and finding out the corresponding results
     * There are altogether 30 kinds of behavioral strategies for card-watching.
     *
     * @param zse
     * @param iexact
     */
    default void addbetUnlookInvoke(ZjhStrategyEntity zse,InvokeExact iexact,ZjhStrategyInfoDTO out){
        if(zse.getRound()==1&&zse.getLastBetScore().compareTo(Double.valueOf(0))==0){
            String Ex1=zse.getPableCount()<=2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E1":null;
            String Ex2=zse.getPableCount()<=2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E2":null;
            String Ex6=zse.getPableCount()<=2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E6":null;
            String Ex7=zse.getPableCount()<=2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E7":null;
            String Ex11=zse.getPableCount()<=2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E10":null;
            String Ex12=zse.getPableCount()<=2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E12":null;
            String Ex16=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E16":null;
            String Ex17=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E17":null;
            String Ex21=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E21":null;
            String Ex22=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E22":null;
            String Ex26=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E26":null;
            String Ex27=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E27":null;
            iexact.bindEx(Ex1).bindEx(Ex2).bindEx(Ex6).bindEx(Ex7).bindEx(Ex11).bindEx(Ex12).bindEx(Ex16).bindEx(Ex17).bindEx(Ex21).bindEx(Ex22).bindEx(Ex26).bindEx(Ex27);
            out.setAddBetRate(iexact.analysisUnLookAdd().doubleValue());
            return;
        }
        String Ex1=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E1":null;
        String Ex2=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E2":null;
        String Ex3=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E3":null;
        String Ex4=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E4":null;
        String Ex5=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E5":null;
        String Ex6=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E6":null;
        String Ex7=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E7":null;
        String Ex8=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E8":null;
        String Ex9=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E8":null;
        String Ex10=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E9":null;
        String Ex11=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E10":null;
        String Ex12=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E12":null;
        String Ex13=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E13":null;
        String Ex14=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E14":null;
        String Ex15=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E15":null;
        String Ex16=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E16":null;
        String Ex17=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E17":null;
        String Ex18=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E18":null;
        String Ex19=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E19":null;
        String Ex20=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E20":null;
        String Ex21=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E21":null;
        String Ex22=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E22":null;
        String Ex23=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E23":null;
        String Ex24=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E24":null;
        String Ex25=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E25":null;
        String Ex26=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E26":null;
        String Ex27=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E27":null;
        String Ex28=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E28":null;
        String Ex29=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E29":null;
        String Ex30=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E30":null;
        String Ex31=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E2":null;
        String Ex32=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E7":null;
        String Ex33=zse.getPableCount()==2&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E12":null;
        String Ex34=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E4":null;
        String Ex35=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E5":null;
        String Ex36=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E9":null;
        String Ex37=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E10":null;
        String Ex38=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E14":null;
        String Ex39=zse.getPableCount()==2&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E15":null;
        String Ex40=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E17":null;
        String Ex41=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E22":null;
        String Ex42=zse.getPableCount()>=3&&!zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E27":null;
        String Ex43=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E19":null;
        String Ex44=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"E20":null;
        String Ex45=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E24":null;
        String Ex46=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"E25":null;
        String Ex47=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E29":null;
        String Ex48=zse.getPableCount()>=3&&!zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"E30":null;
        iexact.bindEx(Ex1).bindEx(Ex2).bindEx(Ex3).bindEx(Ex4).bindEx(Ex5).bindEx(Ex6).bindEx(Ex7).bindEx(Ex8).bindEx(Ex9).bindEx(Ex10).bindEx(Ex11).bindEx(Ex12)
                .bindEx(Ex13).bindEx(Ex14).bindEx(Ex15).bindEx(Ex16).bindEx(Ex17).bindEx(Ex18).bindEx(Ex19).bindEx(Ex20).bindEx(Ex21).bindEx(Ex22).bindEx(Ex23).bindEx(Ex24)
                .bindEx(Ex25).bindEx(Ex26).bindEx(Ex27).bindEx(Ex28).bindEx(Ex29).bindEx(Ex30).bindEx(Ex31).bindEx(Ex32).bindEx(Ex33).bindEx(Ex34).bindEx(Ex35).bindEx(Ex36).bindEx(Ex37)
                .bindEx(Ex38).bindEx(Ex39).bindEx(Ex40).bindEx(Ex41).bindEx(Ex42).bindEx(Ex43).bindEx(Ex44).bindEx(Ex45).bindEx(Ex46).bindEx(Ex47).bindEx(Ex48);
        out.setAddBetRate(iexact.analysisUnLookAdd().doubleValue());
    }


    /**
     * Analyzing the behavior strategy of adding notes and finding out the corresponding results
     * There are altogether 30 kinds of behavioral strategies for card-watching.
     *
     * @param zse
     * @param iexact
     */
    default void addbetInvoke(ZjhStrategyEntity zse,InvokeExact iexact,ZjhStrategyInfoDTO out){
        String EVx1=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV1":null;
        String EVx2=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV2":null;
        String EVx3=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL1,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV3":null;
        String EVx4=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV4":null;
        String EVx5=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV5":null;
        String EVx6=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV6":null;
        String EVx7=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV7":null;
        String EVx8=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL1,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV8":null;
        String EVx9=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV9":null;
        String EVx10=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV10":null;
        String EVx11=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV11":null;
        String EVx12=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV12":null;
        String EVx13=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL1,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV13":null;
        String EVx14=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV14":null;
        String EVx15=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV15":null;
        String EVx16=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV6":null;
        String EVx17=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV17":null;
        String EVx18=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL1,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV18":null;
        String EVx19=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV19":null;
        String EVx20=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV20":null;
        String EVx21=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV21":null;
        String EVx22=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV22":null;
        String EVx23=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL1,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV23":null;
        String EVx24=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV24":null;
        String EVx25=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV25":null;
        String EVx26=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL12,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV26":null;
        String EVx27=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL234,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV27":null;
        String EVx28=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL1,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV28":null;
        String EVx29=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV29":null;
        String EVx30=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.ADD&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV30":null;
        String EVx31=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV2":null;
        String EVx32=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV4":null;
        String EVx33=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV5":null;
        String EVx34=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV7":null;
        String EVx35=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV9":null;
        String EVx36=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV10":null;
        String EVx37=zse.getPableCount()==2&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV12":null;
        String EVx38=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV14":null;
        String EVx39=zse.getPableCount()==2&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV15":null;
        String EVx40=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV17":null;
        String EVx41=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV19":null;
        String EVx42=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.NORMAL?"EV20":null;
        String EVx43=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV22":null;
        String EVx44=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV24":null;
        String EVx45=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.BOLD?"EV25":null;
        String EVx46=zse.getPableCount()>=3&&zse.getIsRlook()&&!zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL34,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV27":null;
        String EVx47=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL23,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV29":null;
        String EVx48=zse.getPableCount()>=3&&zse.getIsRlook()&&zse.getIsPlook()&&zse.getBetType()==RobotFeaturesEnum.FOLLOW&&RobotFeaturesEnum.isLevel(RobotFeaturesEnum.LEVEL4,zse.getLevel())&&zse.getFeatures()==RobotFeaturesEnum.CAUTIOUS?"EV30":null;
        iexact.bindEVx(EVx1).bindEVx(EVx2).bindEVx(EVx3).bindEVx(EVx4).bindEVx(EVx5).bindEVx(EVx6).bindEVx(EVx7).bindEVx(EVx8).bindEVx(EVx9).bindEVx(EVx10).bindEVx(EVx11).bindEVx(EVx12)
                .bindEVx(EVx13).bindEVx(EVx14).bindEVx(EVx15).bindEVx(EVx16).bindEVx(EVx17).bindEVx(EVx18).bindEVx(EVx19).bindEVx(EVx20).bindEVx(EVx21).bindEVx(EVx22).bindEVx(EVx23).bindEVx(EVx24)
                .bindEVx(EVx25).bindEVx(EVx26).bindEVx(EVx27).bindEVx(EVx28).bindEVx(EVx29).bindEVx(EVx30).bindEVx(EVx31).bindEVx(EVx32).bindEVx(EVx33).bindEVx(EVx34).bindEVx(EVx35).bindEVx(EVx36).bindEVx(EVx37)
                .bindEVx(EVx38).bindEVx(EVx39).bindEVx(EVx40).bindEVx(EVx41).bindEVx(EVx42).bindEVx(EVx43).bindEVx(EVx44).bindEVx(EVx45).bindEVx(EVx46).bindEVx(EVx47).bindEVx(EVx48);
        out.setAddBetRate(iexact.analysisAdd().doubleValue());
    }
}
