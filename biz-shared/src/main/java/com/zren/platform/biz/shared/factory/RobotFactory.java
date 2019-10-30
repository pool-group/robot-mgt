package com.zren.platform.biz.shared.factory;

import com.google.common.collect.Lists;
import com.zren.platform.common.dal.po.RobotInfoPO;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.common.util.tool.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class RobotFactory {

    public List<RobotInfoPO> create(int count){

        List<RobotInfoPO> robotInfoPOList= Lists.newArrayList();
        for(int i=0;i<count;i++){
            RobotInfoPO po=new RobotInfoPO();
            po.setId(null);
            po.setUserId(DataUtil.createNineSequenceUid());
            po.setBatchId(DataUtil.createSequenceUid());
            po.setStatus(String.valueOf(1));
            po.setDateTime(new Date());
            robotInfoPOList.add(po);
        }
        LogUtil.info(String.format("系统创建机器人参数构建中... 数量=[ %s ], robotInfoPOList=[ %s ]",count,robotInfoPOList));
        return robotInfoPOList;
    }
}
