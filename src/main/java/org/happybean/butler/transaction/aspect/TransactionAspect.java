package org.happybean.butler.transaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.happybean.butler.annition.BuTransactional;
import org.happybean.butler.remote.Remoter;
import org.happybean.butler.remote.RemoterFactory;
import org.happybean.butler.remote.RemoteTx;
import org.happybean.butler.remote.TxCommand;
import org.happybean.butler.transaction.BuTransactionManager;
import org.happybean.butler.transaction.LocalTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.sql.SQLException;


/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
@Aspect
public class TransactionAspect implements Ordered {

    @Autowired
    private BuTransactionManager dubboTransactionManager;

    private final ThreadLocal<LocalTransaction> localTransaction = new ThreadLocal<>();

    @Pointcut("@annotation(org.happybean.butler.annition.BuTransactional)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint point) throws SQLException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        BuTransactional buTransactional = method.getAnnotation(BuTransactional.class);
        if (buTransactional.remote() == true) {
            Remoter remoter = RemoterFactory.getRemoter(buTransactional.register());
            RemoteTx remoteTx = BuTransactionManager.createRemoteTx();
            LocalTransaction localTransaction = this.localTransaction.get();
            remoteTx.setGroupId(localTransaction.getGroupId());
            try {
                //commit
                localTransaction.getConnection().commit();
                point.proceed();
                remoteTx.setCommand(TxCommand.COMMIT);
            } catch (Throwable throwable) {
                //roll back
                localTransaction.getConnection().rollback();
                remoteTx.setCommand(TxCommand.ROLLBACK);
            }
            remoter.sendRemoter(remoteTx);
        } else {
            try {
                point.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
