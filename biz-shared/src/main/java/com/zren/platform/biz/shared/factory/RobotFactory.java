package com.zren.platform.biz.shared.factory;

import com.google.common.collect.Lists;
import com.zren.platform.common.dal.po.RobotInfoPO;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Robot Factory
 *
 * @author k.y
 * @version Id: RobotFactory.java, v 0.1 2019年05月03日 下午20:07 k.y Exp $
 */
@Component
@Slf4j
public class RobotFactory {

    /**
     * Creating Robots
     *
     * @param count Number of robots
     * @return
     */
    public List<RobotInfoPO> create(int count){

        List<RobotInfoPO> robotInfoPOList= Lists.newArrayList();
        for(int i=0;i<count;i++){
            RobotInfoPO po=new RobotInfoPO();
            po.setId(null);
            po.setUserId(DataUtil.createNineSequenceUid());
            po.setBatchId((long) 0);
            po.setStatus(String.valueOf(0));
            po.setDateTime(new Date());
            robotInfoPOList.add(po);
        }
        LogUtil.info(String.format("系统创建机器人参数构建中... 数量=[ %s ], robotInfoPOList=[ %s ]",count,robotInfoPOList));
        return robotInfoPOList;
    }
}
