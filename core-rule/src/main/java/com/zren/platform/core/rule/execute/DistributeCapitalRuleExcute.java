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

@Component
public class DistributeCapitalRuleExcute {

    public BigDecimal execute(CapitalRuleEntity capitalRuleEntity) {

        if(capitalRuleEntity.getUsableCapital().compareTo(BigDecimal.valueOf(0))==0){
            throw new RobotBizException(ErrorCodeEnum.ACCOUNT_POOL_ZERO);
        }
        if(capitalRuleEntity.getUsableCapital().compareTo(BigDecimal.valueOf(0))==-1){
            throw new RobotBizException(ErrorCodeEnum.ACCOUNT_POOL_EXCEPTION);
        }
        List<RobotInfoPO> robotList=capitalRuleEntity.getRobotList();
        BigDecimal maxAmount=capitalRuleEntity.getMinAmount().multiply(BigDecimal.valueOf(capitalRuleEntity.getMaxMultiple()));
        BigDecimal minAmount=capitalRuleEntity.getMinAmount().multiply(BigDecimal.valueOf(capitalRuleEntity.getMinMultiple()));
        Integer[] result= DataUtil.randomNumber(minAmount.intValue(),maxAmount.intValue(),robotList.size());
        BigDecimal totalAccount=BigDecimal.valueOf(Arrays.asList(result).stream().mapToInt(s-> s).sum());

        if(totalAccount.compareTo(capitalRuleEntity.getUsableCapital())==1){
            throw new RobotBizException(ErrorCodeEnum.ACCOUNT_POOL_NOT_ENOUGH);
        }
        for(int i=0;i<robotList.size();i++){
            robotList.get(i).setUserName(ProbabilisticUtil.excute(BigDecimal.valueOf(0.7))==1?UsernameGenUtils.randomAlphanumeric(11):UsernameGenUtils.generateRandomUsernames(1).get(0));
            robotList.get(i).setHeadUrl(DataUtil.randomNumber(1,capitalRuleEntity.getHeadRandom()+1,1)[0]);
            BigDecimal a=BigDecimal.ZERO;
            if(ProbabilisticUtil.excute(BigDecimal.valueOf(0.4))==1){
                robotList.get(i).setBalance(BigDecimal.valueOf(result[i]).add(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf(DataUtil.randomNumber(1,10,1)[0]))));
                a=a.add(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf(DataUtil.randomNumber(1,10,1)[0])));
            }else if(ProbabilisticUtil.excute(BigDecimal.valueOf(0.6))==1)
                a=a.add(BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(DataUtil.randomNumber(1,10,1)[0])));
            else
                robotList.get(i).setBalance(BigDecimal.valueOf(result[i]));
            robotList.get(i).setBalance(BigDecimal.valueOf(result[i]).add(a));
        }
        return totalAccount;
    }
}
