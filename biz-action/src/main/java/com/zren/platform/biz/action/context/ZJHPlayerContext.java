package com.zren.platform.biz.action.context;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 炸金花机器人逻辑处理状态上下文
 *
 * @author k.y
 * @version Id: ZJHPlayer.java, v 0.1 2018年12月18日 下午13:56 k.y Exp $
 */
@Data
public class ZJHPlayerContext {

    /**是否已确定行为*/
    private boolean action;

    /**玩家类型*/
    private String userType;

    /**前一个是否看过牌*/
    private boolean beforeIsLooked;

    /**前一个操作人ID*/
    private String beforPlayerId;

    /**前一个下注分数*/
    private Double lastBetScore;

    /**当前操作人ID*/
    private String currentId;

    /**当前机器人是否看过牌*/
    private boolean currentIsLooked;

    private Boolean isLookedBeforeBet=false;//玩家跟注加注前"是否看牌
    private Boolean isAddBet=false;//玩家是跟注还是加注
    private Double lastBet;//玩家最后一次的投注

    /**真实牌值*/
    private String realCard;

    /**真实牌值*/
    private String strategyKey;

    /**没有弃牌的玩家数(不包含机器人)*/
    private AtomicInteger pcount=new AtomicInteger(0);

    /**机器人特征 1：普通  2：胆大  3：谨慎*/
    private Byte features=1;

    /**当前机器人状态及牌值大小  -1：输并且牌最小  0：输并在牌不是最大也不是最小  1：赢  2：随机*/
    private Integer robotry;

    private int[] cards;

    /**当前机器人下注总分*/
    private Double totalBetScore;

    /**当前机器人余额分数*/
    private BigDecimal balanceScore;

    /**当前机器人盈亏额*/
    private BigDecimal gainLossScore;

    /**当前机器人应该下注分数*/
    private Double currentShouldBetScore;

    /**底注*/
    private Double baseBet;

    /**第几轮操作*/
    private Integer round;

    /**第几轮操作*/
    private Integer v;

    /**比牌->其他玩家ID集合(包含机器人)*/
    private List<String> compareList;

    /**当前机器人台桌所有用户列表(包括机器人)*/
    private List<Map<String,Object>> playerList;

    /**跟注概率*/
    private Double followRate;

    /**加注概率*/
    private Double addBetRate;

    /**敏捷思维概率*/
    private Double quickThinkingRate;

    /**看牌概率*/
    private Double lookRate;

    /**比牌概率*/
    private Double vsRate;

    /**至少几轮才能比牌*/
    private Integer vsmin;

    /**最多几轮必须比牌*/
    private Integer vsmax;

    /**弃牌概率*/
    private Double giveUpRate;

    /**看牌触发弃牌概率*/
    private Double lookToGiveUpRate;

    /**亮牌概率*/
    private Double showRate;

    /**随机一个比牌ID*/
    private String vsRandomUserId;

    /**随机几秒准备 注意：真实准备时间=按当前机器人加入桌台的时间(millisecondJoinTable)+随机时间(millisecondAready)*/
    private Long millisecondAready;

    /**几秒加入桌台*/
    private Long millisecondJoinTable;

    /**下注跟注动态思考时间*/
    private Long betThinkingTime;

    /**入门最低分数*/
    private Double minEntry;

    /**暗牌筹码 数组以，号组成字符串*/
    private String dark;

    /**明牌筹码 数组 以， 号组成字符串*/
    private String brights;

    /**消息码*/
    private Integer code;

    /**平台名称*/
    private String brand;

    /**agentId*/
    private Long agentId;

    /**clientId*/
    private Long clientId;

    /**机器人批次ID*/
    private Long batchId;

    public void resetShouldBetScoreAndBaseBet(){
        if(this.currentIsLooked==true){
            this.setCurrentShouldBetScore(this.lastBetScore*2);
        }else {
            this.setCurrentShouldBetScore(this.lastBetScore);
        }
        this.setBaseBet(this.lastBetScore);
    }

    public void setToIsAddBet(List<Map<String, Object>> playerList) {
        Double d1 = null,d2=null,d3=null,d4=null;
        for(Map map:playerList){
            if(!map.get("userId").equals(this.getCurrentId())&&("WAIT".equals(map.get("state"))||"THINKING".equals(map.get("state")))){
                if((Boolean) map.get("isLookedBeforeBet")&&(Boolean) map.get("isAddBet")){
                    d1=null==d1?Double.valueOf(map.get("lastBet").toString()):d1.compareTo(Double.valueOf(map.get("lastBet").toString()))==-1?Double.valueOf(map.get("lastBet").toString()):d1;
                }else if((Boolean) map.get("isLookedBeforeBet")&&!(Boolean) map.get("isAddBet")){
                    d2=null==d2?Double.valueOf(map.get("lastBet").toString()):d2.compareTo(Double.valueOf(map.get("lastBet").toString()))==-1?Double.valueOf(map.get("lastBet").toString()):d2;
                }else if(!(Boolean) map.get("isLookedBeforeBet")&&(Boolean) map.get("isAddBet")){
                    d3=null==d3?Double.valueOf(map.get("lastBet").toString()):d3.compareTo(Double.valueOf(map.get("lastBet").toString()))==-1?Double.valueOf(map.get("lastBet").toString()):d3;
                }else if(!(Boolean) map.get("isLookedBeforeBet")&&!(Boolean) map.get("isAddBet")){
                    d4=null==d4?Double.valueOf(map.get("lastBet").toString()):d4.compareTo(Double.valueOf(map.get("lastBet").toString()))==-1?Double.valueOf(map.get("lastBet").toString()):d4;
                }
            }
        }
        if(null!=d1){
            this.isLookedBeforeBet=true;
            this.isAddBet=true;
            this.lastBet=d1;
        }else if(null!=d2){
            this.isLookedBeforeBet=true;
            this.isAddBet=false;
            this.lastBet=d2;
        }else if(null!=d3){
            this.isLookedBeforeBet=false;
            this.isAddBet=true;
            this.lastBet=d3;
        }else if(null!=d4){
            this.isLookedBeforeBet=false;
            this.isAddBet=false;
            this.lastBet=d4;
        }
    }
}
