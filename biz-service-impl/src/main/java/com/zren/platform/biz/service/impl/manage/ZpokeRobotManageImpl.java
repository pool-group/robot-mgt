package com.zren.platform.biz.service.impl.manage;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.common.service.facade.api.zpoke.ZpokeRobotClientFacade;
import com.zren.platform.common.service.facade.dto.in.zpoke.TableMachineDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.service.integration.api.zpoke.ZpokeRobotClientIntegration;
import com.zren.platform.common.util.tool.BeanUtil;
import com.zren.platform.core.model.domain.client.FollowBetClientModel;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.domain.machine.PlayerMachineModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.RoundEnum;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import com.zren.platform.zpoke.common.util.enums.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Zpoke Robot Manage Impl
 *
 * @author k.y
 * @version Id: ZpokeRobotManageImpl.java, v 0.1 2019年10月22日 下午15:55 k.y Exp $
 */
@RestController
public class ZpokeRobotManageImpl implements ZpokeRobotClientFacade {

    @Autowired
    private ZpokeRobotClientIntegration zpokeRobotClientIntegration;
    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Override
    public RobotBaseResult invoke(@RequestBody TableMachineDTO tableMachineDTO) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<TableMachineDTO,Void>(){

            @Override
            public void initContent(EngineContext<TableMachineDTO, Void> context) {
                context.setInputModel(tableMachineDTO);
            }

            @Override
            public void doProcess(EngineContext<TableMachineDTO, Void> context) {

                TableMachineModel tableMachineModel= new TableMachineModel();
                BeanUtil.copyPropertiesIgnoreNull(tableMachineDTO,tableMachineModel);

                //临时模拟
                Optional<PlayerMachineModel> optional=tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())&&s.getUserTypeEnum().equals(UserTypeEnum.ROBOT)).findFirst();
                PlayerClientModel playerClient;
                if(optional.get().getSeat().equals(tableMachineModel.getBigSeat())&&tableMachineModel.getRoundEnum().equals(RoundEnum.ONE)&&isNextRound(tableMachineModel)){
                    PlayerClientModel playerClientModel=new PlayerClientModel();
                    playerClientModel.setCode(EventClientCode.PASS.getCode());
                    playerClientModel.setTableNo(tableMachineModel.getTableId());
                    playerClientModel.setRoomId(tableMachineModel.getRoomId());
                    playerClientModel.setUserId(optional.get().getUserId());
                    playerClient=playerClientModel;
                }else {
                    FollowBetClientModel followBetClientModel=new FollowBetClientModel();
                    followBetClientModel.setCode(EventClientCode.FOLLOW.getCode());
                    followBetClientModel.setTableNo(tableMachineModel.getTableId());
                    followBetClientModel.setRoomId(tableMachineModel.getRoomId());
                    followBetClientModel.setUserId(optional.get().getUserId());
                    followBetClientModel.setCallBet(findMaxRoundBet(tableMachineModel).subtract(optional.get().getRoundBet()));
                    playerClient=followBetClientModel;
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {}
                }
                String jsonString= JSONObject.toJSONString(playerClient);
                zpokeRobotClientIntegration.invoke(jsonString);
            }
        });
    }

    private boolean isNextRound(TableMachineModel tableMachine){
        AtomicInteger inx=new AtomicInteger(0);
        for(PlayerMachineModel play:tableMachine.getPlayerList()){
            if(play.getPlayActionEnum().equals(PlayActionEnum.GIVE_UP)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.FOLLOW)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.ALL_IN)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.THINKING)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.PASS)){inx.getAndIncrement();}
        }
        int playerSeat=tableMachine.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachine.getCurrentSeat())).findFirst().get().getSeat();
        if(inx.get()==tableMachine.getPlayerList().size()&&tableMachine.getBigSeat().equals(playerSeat))
            return true;
        return false;
    }

    private BigDecimal findMaxRoundBet(TableMachineModel tableMachine){
        Optional<PlayerMachineModel> optional=tableMachine.getPlayerList().stream().max((r1, r2)->r1.getRoundBet().compareTo(r2.getRoundBet()));
        if(optional.isPresent()){
            return optional.get().getRoundBet();
        }
        return null;
    }
}