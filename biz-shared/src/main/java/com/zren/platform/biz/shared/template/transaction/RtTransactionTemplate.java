package com.zren.platform.biz.shared.template.transaction;

import com.zren.platform.common.util.exception.RobotBizException;
import com.zren.platform.common.util.exception.RobotSystemException;
import com.zren.platform.common.util.tool.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务链模板
 *
 * @author k.y
 * @version Id: TransactionTemplate.java, v 0.1 2018年11月09日 下午10:45 k.y Exp $
 */
@Component
@Slf4j
public class RtTransactionTemplate {

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 事务链
     *
     * @param callback
     */
    public void doTransaction(final RobotTransactionCallback callback) {

        //MDC.put("txID", "txID-" + String.valueOf(generator.next()));

        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {

                    callback.execute();

                } catch (RobotBizException e) {

                    LogUtil.error(e,"业务异常,事务回滚");
                    status.setRollbackOnly();
                    throw e;
                } catch (RobotSystemException e) {

                    LogUtil.error(e,"系统异常,事务回滚");
                    status.setRollbackOnly();
                    throw e;
                } catch (Exception e) {

                    LogUtil.error(e,"未知异常,事务回滚");
                    status.setRollbackOnly();
                    throw e;
                }
                return status;
            }
        });
        //MDC.remove("txID");
    }
}
