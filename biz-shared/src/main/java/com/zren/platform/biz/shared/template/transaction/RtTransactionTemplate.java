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

@Component
@Slf4j
public class RtTransactionTemplate {

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void doTransaction(final RobotTransactionCallback callback) {

        //MDC.put("txID", "txID-" + String.valueOf(generator.next()));

        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {

                    callback.execute();

                } catch (RobotBizException e) {

                    LogUtil.error(e,"biz exception then transaction is rollback !");
                    status.setRollbackOnly();
                    throw e;
                } catch (RobotSystemException e) {

                    LogUtil.error(e,"system exception then transaction is rollback !");
                    status.setRollbackOnly();
                    throw e;
                } catch (Exception e) {

                    LogUtil.error(e,"unknown exception then transaction is rollback !");
                    status.setRollbackOnly();
                    throw e;
                }
                return status;
            }
        });
        //MDC.remove("txID");
    }
}
