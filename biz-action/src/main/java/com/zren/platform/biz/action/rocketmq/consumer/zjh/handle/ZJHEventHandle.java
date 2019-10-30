package com.zren.platform.biz.action.rocketmq.consumer.zjh.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zren.platform.biz.action.biz.RobotBizPush;
import com.zren.platform.biz.action.context.ZJHPlayerContext;
import com.zren.platform.biz.action.enums.RobotEnum;
import com.zren.platform.biz.action.model.zjh.*;
import com.zren.platform.biz.action.rocketmq.consumer.common.BaseEventHandle;
import com.zren.platform.biz.shared.core.robot.RobotManageServiceImpl;
import com.zren.platform.biz.shared.monitor.RobotAccountMonitor;
import com.zren.platform.common.dal.po.AccountRegainSnapInfoPO;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotDestroyInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.zjh.ZjhStrategyInfoDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.log.Log;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.core.rule.entity.in.ZjhStrategyEntity;
import com.zren.platform.core.rule.enums.RobotFeaturesEnum;
import com.zren.platform.core.rule.execute.StrategyRuleExcute;
import com.zren.platform.intercomm.dto.RoomConfigReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 炸金花event基类
 *
 * @author k.y
 * @version Id: ZJHEventHandle.java, v 0.1 2018年12月18日 下午16:14 k.y Exp $
 */
@Component
public abstract class ZJHEventHandle extends BaseEventHandle {

    @Autowired
    private StrategyRuleExcute strategyRuleExcute;

    @Autowired
    private RobotManageServiceImpl robotManageServiceImpl;

    @Autowired
    private RobotBizPush robotBizPush;

    /**
     * 初始化上下文参数
     *
     * @param context
     * @param object
     */
    public void assembleContext(ZJHPlayerContext context,JSONObject object){
        context.setAction(false);
        context.setCode(object.getInteger("code"));
        context.setBrand(object.getString("brand"));
        context.setUserType(object.getString("userType"));
        context.setBatchId(object.getLong("batchId"));
        context.setClientId(object.getLong("clientId"));
        context.setAgentId(object.getLong("agentId"));
        List<Map<String,Object>> lst=(List) JSON.parseArray(object.getString("infoForRobots"), HashMap.class);//当前机器人台桌所有用户列表(包括机器人)
        context.setPlayerList(lst);
        context.setLastBetScore(object.getDouble("lastBetScore"));//前一个下注分数
        context.setRound(object.getInteger("r"));//当前第几轮操作
        context.setV(object.getInteger("v"));//牌型
        context.setMinEntry(object.getDouble("minEntry"));
        context.setDark(object.getString("dark"));
        context.setBrights(object.getString("brights"));
        context.setClientId(object.getLong("clientId"));
        context.setAgentId(object.getLong("agentId"));
    }

    /**
     * 初始化上下文策略参数
     *
     * @param context
     * @param lst
     */
    public void assembleContextStrategy(ZJHPlayerContext context,List<Map<String,Object>> lst){

        //初始化user信息上下文
        this.initContextByUserList(context,lst);

        if(context.getCompareList().size()==0){
            return;
        }

        List<Map<String,Object>> userLst= lst.stream().filter(s -> s.get("userId").equals(context.getBeforPlayerId())).collect(Collectors.toList());
        if(null!=userLst&&userLst.size()>0){
            context.setBeforeIsLooked((Boolean) userLst.get(0).get("isLooked"));
        }
        //重置应该下注分数和底注
        context.resetShouldBetScoreAndBaseBet();

        //刷新上下文概率相关数据
        this.refreshContext(context);

        //刷新不同行为思考策略
        this.refreshThinkingTime(context);

        LogUtil.info(String.format(" code=[ %s ], userId=[ %s ] 初始化机器人策略上下文参数完成: [ %s ]",context.getCode(),context.getCurrentId(),context));
    }

    /**
     * 初始化user信息上下文
     *
     * @param context
     * @param lst
     */
    public void initContextByUserList(ZJHPlayerContext context,List<Map<String,Object>> lst){
        List<String> compareList=new ArrayList<>();
        for(Map map:lst){
            if(map.get("userId").equals(context.getCurrentId())){
                context.setBeforPlayerId(String.valueOf(map.get("beforPlayerId")));//上一个操作人ID
                context.setCurrentId(context.getCurrentId());//当前操作人ID
                context.setCurrentIsLooked((Boolean) map.get("isLooked"));//当前机器人是否看过牌
                context.setRobotry((Integer) map.get("robotry"));//当前机器人状态  -1：输并且牌最小  0：输并在牌不是最大也不是最小  1：赢  2：随机
                context.setTotalBetScore(Double.valueOf(map.get("totalBetScore").toString()));//当前机器人下注总分
                context.setBalanceScore(new BigDecimal(map.get("balanceScore").toString()));//当前机器人余额分
                context.setBrand(map.get("brand").toString());//平台名称
                context.setAgentId(888888L);
                context.setClientId(999999L);
                JSONArray jsonArray= (JSONArray) map.get("cards");
                context.setCards(new int[]{(Integer) jsonArray.get(0),(Integer) jsonArray.get(1),(Integer) jsonArray.get(2)});
            }
            if(!compareList.contains(context.getCurrentId())&&!map.get("userId").equals(context.getCurrentId())&&("WAIT".equals(map.get("state"))||"THINKING".equals(map.get("state")))){
                compareList.add((String) map.get("userId"));
            }
        }
        context.setCompareList(compareList);
        //随机取一个玩家或机器人比牌
        if(compareList.size()>0){
            int r=DataUtil.randomNumber(0,context.getCompareList().size(),1)[0];
            context.setVsRandomUserId(compareList.get(r));
        }
    }

    /**
     * 刷新不同行为思考时间，目前暂不提供支持配置化
     *
     * @param context
     */
    public void refreshThinkingTime(ZJHPlayerContext context){

        //是否具有敏捷思维
        if(super.isHitRate(context.getQuickThinkingRate())){
            context.setBetThinkingTime(Long.valueOf(DataUtil.randomNumber(1,3,1)[0])*1000);
        }else {
            context.setBetThinkingTime(Long.valueOf(DataUtil.randomNumber(2,6,1)[0])*1000);
        }

        if(super.isHitRate(context.getAddBetRate())){
            String[] dark=context.getDark().split(",");
            String[] brights=context.getBrights().split(",");
            if(context.isCurrentIsLooked()){
                for(int i=brights.length-1;i>=0;i--){
                    if(Double.valueOf(brights[i]).compareTo(context.getCurrentShouldBetScore())==1&&super.isHitRate(strategyRuleExcute.findAddBetStrategy(context.getRobotry(),i))){
                        context.setCurrentShouldBetScore(Double.valueOf(brights[i]));
                        context.setBaseBet(Double.valueOf(brights[i])/2);
                    }
                }
            }else {
                for(int i=dark.length-1;i>=0;i--){
                    if(Double.valueOf(dark[i]).compareTo(context.getCurrentShouldBetScore())==1&&super.isHitRate(strategyRuleExcute.findAddBetStrategy(context.getRobotry(),i))){
                        context.setCurrentShouldBetScore(Double.valueOf(dark[i]));
                        context.setBaseBet(Double.valueOf(dark[i]));
                    }
                }
            }
            LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], addRate=[ %s ], robot action：adding....",context.getCurrentId(),context.getStrategyKey(), context.getRealCard(),1+context.getRound(), context.getAddBetRate()));
        }else {
            LogUtil.info(Log.AI_STRATEGY.LOG,String.format("useId=[ %s ], key=[ %s ], realCard= [ %s ], round=[ %s ], addRate=[ %s ], robot action：following....",context.getCurrentId(),context.getStrategyKey(), context.getRealCard(),1+context.getRound(), context.getAddBetRate()));
        }

    }

    public void destroyRobot(ZJHPlayerContext context, RoomConfigReq req){
        String robotInfoKey= RedisCommon.getUserRedisKey(RedisCommon.ROBOTINFO,req.getGameId(),req.getRoomId(),req.getTableId());
        //2. 如果当前离开的是机器人，则调用回收机器人接口
        if(RobotEnum.ROBOT.toString().equals(context.getUserType())){
            LogUtil.info(String.format(" code=[ %s ], 桌台 [ %s ], 机器人[ %s ]离开房间",context.getCode(),req.getTableId(),context.getCurrentId()));
            RobotDestroyInputModelDTO dto=new RobotDestroyInputModelDTO();
            dto.setUserId(Long.valueOf(context.getCurrentId()));
            dto.setBrand(context.getBrand());
            dto.setBatchId(context.getBatchId());
            dto.setBalance(context.getBalanceScore());
            dto.setGameId(req.getGameId());
            dto.setRoomId(req.getRoomId());
            List<RobotDestroyInputModelDTO> robotDestroyInputModelList= Lists.newArrayList(dto);
            try {

                RobotBaseResult result=robotManageServiceImpl.freezeRobot(robotDestroyInputModelList);
                if(!result.isSuccess()){
                    throw new RobotSystemException(result.getErrorContext().fetchCurrentError().getDescription(),ErrorCodeEnum.ROBOT_DESTROY_SYS);
                }
                LogUtil.info(String.format(" code=[ %s ], 机器人 [ %s ] 成功被系统回收, info=[ %s ]",context.getCode(),dto.getUserId(),dto));

//                //销毁桌台机器人座位
//                robotTableFeign.robotLeaveTable(req.getGameId(),req.getRoomId(),req.getTableId(),1);
//                LogUtil.info(String.format(" code=[ %s ], 桌台 [ %s ], 机器人 [ %s ] 的座位号消毁成功",context.getCode(),req.getTableId(),context.getCurrentId()));

                //3. redis清除该机器人信息
                redisTemplate.opsForSet().remove(robotInfoKey,context.getCurrentId());

                //4. redis清除该机器人策略
                String redisUserStrategyKey= RedisCommon.getRedisStrategyKey(req.getGameId(),req.getRoomId(),req.getTableId(),context.getCurrentId(),RedisCommon.STRATEGY);
                if(redisTemplate.hasKey(redisUserStrategyKey)){
                    redisTemplate.delete(redisUserStrategyKey);
                }
            } catch (Exception e) {
                throw e;
            }
        }else {
            LogUtil.info(String.format(" code=[ %s ], 桌台 [ %s ], 真实玩家[ %s ] 离开房间",context.getCode(),req.getTableId(),context.getCurrentId()));
        }

        //5. 判断桌台里剩余玩家是否都是机器人
        int robotCount=0;
        int realPlayerCount=0;
        if(null==context.getPlayerList())
            return;
        for(Map userMap:context.getPlayerList()){
            if(RobotEnum.ROBOT.toString().equals(userMap.get("userType"))){
                robotCount++;
            }else {
                realPlayerCount++;
            }
        }
        for(Map userMap:context.getPlayerList()) {
            if (robotCount == context.getPlayerList().size() && context.getPlayerList().size() == 1 && RobotEnum.ROBOT.toString().equals(userMap.get("userType"))) {
                LogUtil.info(String.format(" code=[ %s ], 桌台 [ %s ], 已不存在真实玩家: 机器人[ %s ], 发起离开房间申请",context.getCode(),req.getTableId(),String.valueOf(userMap.get("userId"))));
                this.sendLeaveMessage(req, String.valueOf(userMap.get("userId")));
            }
        }
        //机器人处理
        this.robotProcess(context,req,realPlayerCount);

    }


    /**
     * 更新机器人账变
     *
     * @param context
     * @param req
     */
    public void updateRobotAccount(ZJHPlayerContext context, RoomConfigReq req){

        RobotDestroyInputModelDTO dto=new RobotDestroyInputModelDTO();
        dto.setUserId(Long.valueOf(context.getCurrentId()));
        dto.setBrand(context.getBrand());
        dto.setBatchId(context.getBatchId());
        dto.setBalance(context.getBalanceScore());
        dto.setGameId(req.getGameId());
        dto.setRoomId(req.getRoomId());
        List<RobotDestroyInputModelDTO> robotDestroyInputModelList= Lists.newArrayList(dto);
        RobotBaseResult result= null;
        try {
            result = robotManageServiceImpl.updateRobotAccount(robotDestroyInputModelList);
            if(!result.isSuccess()){
                throw new RobotSystemException(result.getErrorContext().fetchCurrentError().getDescription(),ErrorCodeEnum.ROBOT_UPDATE_ACCOUNT_SYS);
            }
            LogUtil.info(String.format(" code=[ %s ], 机器人 [ %s ] 账变更新成功, info=[ %s ]",context.getCode(),dto.getUserId(),dto));
        } catch (Exception e) {
            LogUtil.error(String.format(" code=[ %s ], 机器人 [ %s ] 盈亏账变更新失败, info=[ %s ]",context.getCode(),dto.getUserId(),dto));
            AccountRegainSnapInfoPO po=new AccountRegainSnapInfoPO();
            po.setUserId(dto.getUserId());
            po.setBatchId(dto.getBatchId());
            po.setGainLoss(context.getGainLossScore());
            po.setBrand(dto.getBrand());
            po.setGameId(dto.getGameId());
            po.setRoomId(dto.getRoomId());
            //记录账变异常监控日志
            LogUtil.error(RobotAccountMonitor.Log,RobotAccountMonitor.fetchSqlFormat(po));
        }
    }

    /**
     * 向该桌台推送机器人
     *
     * @param context
     * @param req
     * @param realPlayerCount
     */
    public void robotProcess(ZJHPlayerContext context, RoomConfigReq req,int realPlayerCount){
        //6. 桌台只剩1-2个真实玩家,向该桌台推送机器人
//        if(realPlayerCount>0 && context.getPlayerList().size()<3){
//            int count=DataUtil.randomNumber(1,4-context.getPlayerList().size()+1,1)[0];
//            LogUtil.info(String.format(" code=[ %s ], 桌台 [ %s ] 总玩家数: [ %s ], 其中机器人数: [ %s ], 推送机器人执行中.., 请求参数: gameId=[ %s ],roomId=[ %s ],tableId=[ %s ],counts=[ %s ] ",context.getCode(),req.getTableId(),context.getPlayerList().size(),(context.getPlayerList().size()-realPlayerCount),req.getGameId(),req.getRoomId(),req.getTableId(),count));
//            robotTableFeign.robotJoinTable(req.getGameId(),req.getRoomId(),req.getTableId(),count);
//            AIRobotInitInputModelDTO initInputModelDTO=new AIRobotInitInputModelDTO();
//            try {
//                initInputModelDTO.setBrand(context.getBrand());
//                initInputModelDTO.setGameId(req.getGameId());
//                initInputModelDTO.setRoomId(req.getRoomId());
//                initInputModelDTO.setTableId(req.getTableId());
//                initInputModelDTO.setMinAmount(BigDecimal.valueOf(context.getMinEntry()));
//                initInputModelDTO.setCount(count);
//                initInputModelDTO.setAgentId(null==context.getAgentId()?88888:context.getAgentId());
//                initInputModelDTO.setClientId(null==context.getClientId()?88889:context.getClientId());
//                LogUtil.info(String.format(" code=[ %s ], 桌台 [ %s ], 推送中,initInputModelDTO=[ %s ]",context.getCode(), req.getTableId(),initInputModelDTO));
//                robotBizPush.createRobot(initInputModelDTO);
//            } catch (Exception e) {
//                //TCC事务补偿，销毁桌台机器人占位
//                robotTableFeign.robotLeaveTable(req.getGameId(),req.getRoomId(),req.getTableId(),count);
//                LogUtil.error(e,String.format(" code=[ %s ], 桌台 [ %s ], 申请推送机器人失败",context.getCode(), req.getTableId()));
//            }
//        }
    }

    /**
     * 刷新上下文和缓存
     *
     * @param context
     */
    public ZjhStrategyInfoDTO refreshContext(ZJHPlayerContext context){
        ZjhStrategyEntity zse = new ZjhStrategyEntity();
        zse.setPableCount(context.getCompareList().size()+1);
        zse.setIsRlook(context.isCurrentIsLooked());
        context.setToIsAddBet(context.getPlayerList());
        zse.setIsPlook(context.getIsLookedBeforeBet());
        zse.setBetType(context.getIsAddBet()?RobotFeaturesEnum.ADD:RobotFeaturesEnum.FOLLOW);
        zse.setLastBetScore(context.getLastBet());
        zse.setFeatures(RobotFeaturesEnum.features(context.getFeatures()));
        zse.setIsWin(context.getRobotry());
        zse.setBrights(context.getBrights());
        zse.setDark(context.getDark());
        zse.setCards(context.getCards());
        zse.setRound(context.getRound()+1);
        ZjhStrategyInfoDTO zjhStrategyInfoDTO=strategyRuleExcute.refreshZjhAIStrategy(zse);
        context.setLookRate(zjhStrategyInfoDTO.getLookRate());
        context.setLookToGiveUpRate(zjhStrategyInfoDTO.getLookToGiveUpRate());
        context.setGiveUpRate(zjhStrategyInfoDTO.getGiveUpRate());
        context.setShowRate(zjhStrategyInfoDTO.getShowRate());
        context.setVsRate(zjhStrategyInfoDTO.getVsRate());
        context.setVsmin(zjhStrategyInfoDTO.getVsmin());
        context.setVsmax(zjhStrategyInfoDTO.getVsmax());
        context.setFollowRate(zjhStrategyInfoDTO.getFollowRate());
        context.setAddBetRate(zjhStrategyInfoDTO.getAddBetRate());
        context.setQuickThinkingRate(zjhStrategyInfoDTO.getQuickThinkingRate());
        context.setRealCard(zjhStrategyInfoDTO.getRealCard());
        context.setStrategyKey(zjhStrategyInfoDTO.getKey());
        return zjhStrategyInfoDTO;
    }

    /**
     * 孤注一掷
     *
     * @param req
     * @param userId
     */
    public void sendAloneMessage(RoomConfigReq req, String userId){
        AloneMessageMqModel model=new AloneMessageMqModel();
        model.setCode(1009);
        model.setUserId(userId);
        sendDelayMessage(req,model,Long.valueOf(DataUtil.randomNumber(1,8,1)[0])*1000);
        LogUtil.info(String.format(" 机器人 [ %s ], 发起 [ %s ] 孤注一掷",userId,model.getCode()));
    }

    /**
     * 弃牌
     *
     * @param req
     * @param userId
     */
    public void sendGiveUpMessage(RoomConfigReq req,String userId,Long giveUpMillisecond){
        GiveUpMessageMqModel model=new GiveUpMessageMqModel();
        model.setCode(1005);
        model.setUserId(userId);
        model.setReason("robot TEST");
        sendDelayMessage(req,model,giveUpMillisecond);
        LogUtil.info(String.format(" 机器人 [ %s ], 发起 [ %s ]ms后 [ %s ] 弃牌",userId,giveUpMillisecond,model.getCode()));
    }

    /**
     * 看牌
     *
     * @param req
     * @param userId
     */
    public void sendLookMessage(RoomConfigReq req,String userId){
        LookMessageMqModel model=new LookMessageMqModel();
        model.setCode(1004);
        model.setUserId(userId);
        Long millisecond=Long.valueOf(DataUtil.randomNumber(1,4,1)[0])*1000;
        sendDelayMessage(req,model,millisecond);
        LogUtil.info(String.format(" 机器人 [ %s ], 发起 [ %s ]ms后 [ %s ] 看牌",userId,millisecond,model.getCode()));
    }

    /**
     * 比牌
     *
     * @param req
     * @param userId
     */
    public void sendVsMessage(RoomConfigReq req,String userId,String compareUserId){
        CompareToMessageMqModel model=new CompareToMessageMqModel();
        model.setCode(1006);
        model.setUserId(userId);
        model.setI(compareUserId);
        sendDelayMessage(req,model,Long.valueOf(DataUtil.randomNumber(1,7,1)[0])*1000);
        LogUtil.info(String.format(" 机器人 [ %s ], 发起 [ %s ] 比牌",userId,model.getCode()));
    }

    /**
     * 下注、跟注
     *
     * @param req
     * @param userId
     */
    public void sendFollowMessage(RoomConfigReq req,String userId,ZJHPlayerContext context){
        FollowMessageMqModel model=new FollowMessageMqModel();
        model.setCode(1003);
        model.setUserId(userId);
        model.setV(String.valueOf(context.getCurrentShouldBetScore()));
        model.setBaseBet(String.valueOf(context.getBaseBet()));
        sendDelayMessage(req,model,context.getBetThinkingTime());
        LogUtil.info(String.format(" 机器人 [ %s ], 发起 [ %s ] 下注、跟注",userId,model.getCode()));
    }

    /**
     * 机器人准备
     *
     * @param req
     * @param userId
     */
    public void sendAlreadyMessage(RoomConfigReq req,String userId,Long areadyMillisecond){
        AlreadyMessageMqModel model=new AlreadyMessageMqModel();
        model.setUserId(userId);
        model.setCode(1001);
        super.sendDelayMessage(req, model,areadyMillisecond);
        LogUtil.info(String.format(" 机器人 [ %s ], 发起 [ %s ] 准备",userId,model.getCode()));
    }

    /**
     * 离开桌台
     *
     * @param req
     * @param userId
     */
    public void sendLeaveMessage(RoomConfigReq req,String userId){
        LeaveMessageMqModel model=new LeaveMessageMqModel();
        model.setCode(1008);
        model.setUserId(userId);
        super.sendDelayMessage(req, model,Long.valueOf(DataUtil.randomNumber(1,8,1)[0])*1000);
    }
}
