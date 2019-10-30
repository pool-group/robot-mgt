package com.zren.platform.biz.shared.monitor;

import com.zren.platform.common.dal.po.AccountRegainSnapInfoPO;
import com.zren.platform.common.util.tool.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 机器人账变日志监控
 *
 * @author k.y
 * @version Id: RobotAccountMonitor.java, v 0.1 2019年01月10日 下午17:47 k.y Exp $
 */
@Slf4j(topic = "account_monitor")
public class RobotAccountMonitor {

    public final static Logger Log = log;

    public static String fetchSqlFormat(AccountRegainSnapInfoPO po){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(new Date());
        po.setStatus((byte) 0);
        po.setUid(DataUtil.createSequenceUid());
        return String.format(" INSERT INTO `account_regain_snap` VALUES (%s, %s, %s, %s, %s, '%s', %s, %s, %s, '%s', '%s');",null,po.getUid(),po.getUserId(),po.getBatchId(),po.getGainLoss(),po.getBrand(),po.getGameId(),po.getRoomId(),po.getStatus(),dateString,dateString);
    }

}
