package com.zren.platform.core.rule.execute;

import com.zren.platform.common.dal.po.RobotInfoPO;
import com.zren.platform.common.util.enums.ErrorCodeEnum;
import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.ProbabilisticUtil;
import com.zren.platform.core.rule.entity.in.CapitalRuleEntity;
import com.zren.platform.common.util.tool.UsernameGenUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 机器人初始化规则实现
 *
 * @author k.y
 * @version Id: DistributeCapitalRuleImpl.java, v 0.1 2018年11月07日 下午17:30 k.y Exp $
 */
@Component
public class DistributeCapitalRuleExcute {

    /**
     * 机器人初始化规则:
     *
     * 随机名称 随机头像编号 随机余额
     *
     * @param capitalRuleEntity
     * @return
     */
    public BigDecimal execute(CapitalRuleEntity capitalRuleEntity) {

        //可用资金为零
        if(capitalRuleEntity.getUsableCapital().compareTo(BigDecimal.valueOf(0))==0){
            throw new RobotBizException(ErrorCodeEnum.ACCOUNT_POOL_ZERO);
        }
        //存在账务异常,冻结资金超过原始本金
        if(capitalRuleEntity.getUsableCapital().compareTo(BigDecimal.valueOf(0))==-1){
            throw new RobotBizException(ErrorCodeEnum.ACCOUNT_POOL_EXCEPTION);
        }
        List<RobotInfoPO> robotList=capitalRuleEntity.getRobotList();
        BigDecimal maxAmount=capitalRuleEntity.getMinAmount().multiply(BigDecimal.valueOf(capitalRuleEntity.getMaxMultiple()));
        BigDecimal minAmount=capitalRuleEntity.getMinAmount().multiply(BigDecimal.valueOf(capitalRuleEntity.getMinMultiple()));
        Integer[] result= DataUtil.randomNumber(minAmount.intValue(),maxAmount.intValue(),robotList.size());
        BigDecimal totalAccount=BigDecimal.valueOf(Arrays.asList(result).stream().mapToInt(s-> s).sum());
        //资金分配不足
        if(totalAccount.compareTo(capitalRuleEntity.getUsableCapital())==1){
            throw new RobotBizException(ErrorCodeEnum.ACCOUNT_POOL_NOT_ENOUGH);
        }
        for(int i=0;i<robotList.size();i++){
            robotList.get(i).setUserName(UsernameGenUtils.generateRandomUsernames(1).get(0));
            robotList.get(i).setHeadUrl(DataUtil.randomNumber(1,capitalRuleEntity.getHeadRandom()+1,1)[0]);
            if(ProbabilisticUtil.excute(BigDecimal.valueOf(0.35))==1){
                robotList.get(i).setBalance(BigDecimal.valueOf(result[i]).add(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf(DataUtil.randomNumber(1,10,1)[0]))));
            }else
                robotList.get(i).setBalance(BigDecimal.valueOf(result[i]));

        }
        return totalAccount;
    }
}
