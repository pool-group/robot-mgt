package com.zren.platform.core.rule.strategy.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zren.platform.common.dal.po.ZjhStrategyPO;
import com.zren.platform.common.dal.repository.ZjhStrategyRepository;
import com.zren.platform.common.util.enums.TacticsEnum;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import com.zren.platform.common.util.tool.RedisCommon;
import com.zren.platform.common.util.tuple.Tuple2x;
import com.zren.platform.common.util.tuple.Tuple3x;
import com.zren.platform.core.rule.entity.in.AlgorithmRuleEntity;
import com.zren.platform.core.rule.entity.in.PlayerStrategyInputEntity;
import com.zren.platform.core.rule.entity.in.ZjhStrategyEntity;
import com.zren.platform.core.rule.entity.out.PlayerStrategyEntity;
import com.zren.platform.intercomm.dto.RobotIntegrationDto;
import com.zren.platform.intercomm.dto.RobotIntegrationReq;
import com.zren.platform.intercomm.gcenter.fegin.RobotIntegrationFeign;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Policy Information Query Base Strategy
 *
 * @author k.y
 * @version Id: BaseIntegration.java, v 0.1 2019年08月01日 下午14:37 k.y Exp $
 */
public abstract class BaseIntegration {

    protected final static Long expire=2*60*1000L;

    protected final static Long p_expire=30*1000L;

    public final static String mark="##";

    public final static String data="data";

    public final static String standard="standard";

    private AtomicInteger atomicInteger=new AtomicInteger(0);

    @Autowired
    private ZjhStrategyRepository zjhStrategyRepository;

    @Autowired
    private RobotIntegrationFeign robotIntegrationFeign;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    protected RedissonClient redisClient;

    protected void refresh(PlayerStrategyInputEntity inputEntity){
        redisTemplate.opsForHash().put(RedisCommon.getStandardStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()), BaseIntegration.standard, JSONObject.toJSONString(new Tuple3x<>(inputEntity.getDenyPipAmount(),inputEntity.getPositiveTotalbetAlarm(),inputEntity.getReverseTotalbetAlarm())));
    }

    public void initcontainer(Map<String,Tuple2x> AI_ACTION_MODEL){
        List<ZjhStrategyPO> lst=zjhStrategyRepository.findAll();
        lst.stream().forEach(s -> {
            AI_ACTION_MODEL.put(s.getCardType()+mark+s.getCardKey()+mark+s.getRound(),new Tuple2x<Long,ZjhStrategyPO>(System.currentTimeMillis()+expire,s));
        });
    }

    public List<RobotIntegrationDto> loading(PlayerStrategyInputEntity inputEntity){
        RobotIntegrationReq robotIntegrationReq=new RobotIntegrationReq();
        robotIntegrationReq.setBrand(inputEntity.getBrand());
        robotIntegrationReq.setGameId(inputEntity.getGameId());
        robotIntegrationReq.setRoomId(inputEntity.getRoomId());
        robotIntegrationReq.setDay(inputEntity.getDay());
        robotIntegrationReq.setLimit(inputEntity.getLimit());
        robotIntegrationReq.setMins(inputEntity.getMins());
        robotIntegrationReq.setState(inputEntity.getState());
         return robotIntegrationFeign.getReqMessage(robotIntegrationReq);
    }

    public Tuple3x<Long,Integer,List<RobotIntegrationDto>> find(PlayerStrategyInputEntity inputEntity){
        Map<String,Object> model=redisTemplate.opsForHash().entries(RedisCommon.getPlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()));
        if (null==model||model.size()==0)
            return null;
        JSONArray jsonArray=JSONObject.parseArray((String) model.get(BaseIntegration.data));
        return new Tuple3x<Long,Integer,List<RobotIntegrationDto>>((Long)jsonArray.get(0),(Integer)jsonArray.get(1),JSONObject.parseArray(jsonArray.get(2).toString(),RobotIntegrationDto.class));
    }

    public Boolean saveOrUpdate(PlayerStrategyInputEntity inputEntity,Map<String,Tuple3x<Long,Integer,List<RobotIntegrationDto>>> CURRENT_DATA_TOTAL) {
        Long startTime=System.currentTimeMillis();
        try {
            List<RobotIntegrationDto> robotIntegrationDtoList=loading(inputEntity);
            if(null==robotIntegrationDtoList){
                return false;
            }
            robotIntegrationDtoList.sort(new Comparator<RobotIntegrationDto>(){
                @Override
                public int compare(RobotIntegrationDto o1, RobotIntegrationDto o2) {

                    return o2.getGainloss().subtract(o1.getGainloss()).intValue();
                }
            });
            Tuple3x<Long,Integer,List<RobotIntegrationDto>> tuple3x=new Tuple3x(System.currentTimeMillis()+BaseIntegration.p_expire,robotIntegrationDtoList.size(),robotIntegrationDtoList);
            CURRENT_DATA_TOTAL.put(getCurrentDataTotalKey(inputEntity),tuple3x);
            redisTemplate.opsForHash().put(RedisCommon.getPlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()), BaseIntegration.data, JSONObject.toJSONString(tuple3x));
            LogUtil.info(String.format("Player Strategy container parameters refreshed success!  data=[ %s ]",JSONObject.toJSONString(tuple3x)));
            return true;
        } catch (Exception e) {
            LogUtil.warn(e,String.format(" saveOrUpdate redis or local container is fail .. input=[ %s ], getReqMessage() requestTime=[ %s ]ms",inputEntity,(System.currentTimeMillis()-startTime)));
            return false;
        }
    }

    public String getCurrentDataTotalKey(PlayerStrategyInputEntity inputEntity){
        return inputEntity.getBrand()+BaseIntegration.mark+inputEntity.getGameId()+BaseIntegration.mark+inputEntity.getRoomId();
    }

    public Boolean haveDennyEffective(AlgorithmRuleEntity inputEntity, PlayerStrategyEntity output){
        int index= DataUtil.randomNumber(0,output.getDeny().size(),1)[0];
        Long p_expire=System.currentTimeMillis()+(inputEntity.getPositiveGrayTime()*60*1000);
        if (!redisTemplate.hasKey(RedisCommon.getExpirePlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()))){
            redisTemplate.opsForValue().set(RedisCommon.getExpirePlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()),String.valueOf(p_expire));
            return false;
        }
        Long r_expire= Long.valueOf(String.valueOf(redisTemplate.opsForValue().get(RedisCommon.getExpirePlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()))));
        if(System.currentTimeMillis()>r_expire){
            redisTemplate.delete(RedisCommon.getDenyPlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()));
            redisTemplate.opsForValue().set(RedisCommon.getExpirePlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()),String.valueOf(p_expire));
            output.setTarget(output.getDeny().get(index));
            return true;
        }else {
            Long id=output.getDeny().get(index);
            if(!redisTemplate.opsForSet().isMember(RedisCommon.getDenyPlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()),String.valueOf(id))){
                output.setTarget(id);
                return true;
            }
            for(Long playerId:output.getDeny()){
                if(!redisTemplate.opsForSet().isMember(RedisCommon.getDenyPlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()),String.valueOf(playerId))){
                    output.setTarget(playerId);
                    return true;
                }
            }
            return false;
        }
    }

    protected Void addDenyMember(ZjhStrategyEntity inputEntity){
        if(inputEntity.getRobotry().equals(TacticsEnum.SEC_MAX.getCode())){
            Object model=redisTemplate.opsForHash().get(RedisCommon.getStandardStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()),BaseIntegration.standard);
            if (null==model)
                return null;
            JSONArray jsonArray=JSONObject.parseArray((String) model);
            BigDecimal denyPipAmount= (BigDecimal) jsonArray.get(0);
            String[] brights=inputEntity.getBrights().split(",");
            Double maxPip=Double.valueOf(brights[brights.length-1]);
            BigDecimal finalPipAmount=denyPipAmount.multiply(BigDecimal.valueOf(maxPip));
            LogUtil.info(String.format("Request：finalPipAmount=[ %s ], playerTotalBetScore=[ %s ]",finalPipAmount,inputEntity.getPlayerTotalBetScore()));
            if(inputEntity.getPlayerTotalBetScore().compareTo(finalPipAmount.doubleValue())>=0){
                try {
                    redisTemplate.opsForSet().add(RedisCommon.getDenyPlayerStrategyKey(inputEntity.getBrand(),inputEntity.getGameId(),inputEntity.getRoomId()),inputEntity.getRealPlayerId());
                    LogUtil.info(String.format("addDenyMember is success !@Request playerId=[ %s ]",inputEntity.getRealPlayerId()));
                } catch (Exception e) {
                    atomicInteger.incrementAndGet();
                    LogUtil.warn(e,String.format("addDenyMember is fail !@Error times equal to [ %s ]",atomicInteger.get()));
                }
            }
        }
        return null;
    }
}
