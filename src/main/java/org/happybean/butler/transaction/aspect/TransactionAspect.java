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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;


/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
@Aspect
public class TransactionAspect {

    @Autowired
    private BuTransactionManager dubboTransactionManager;

    @Pointcut("@annotation(org.happybean.butler.annition.BuTransactional)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        BuTransactional buTransactional = method.getAnnotation(BuTransactional.class);
        if (buTransactional.remote() == true) {
            Remoter remoter = RemoterFactory.getRemoter(buTransactional.register());
            RemoteTx remoteTx = BuTransactionManager.createRemoteTx();
            try {
                //commit
                point.proceed();
                remoteTx.setCommand(TxCommand.COMMIT);
            } catch (Throwable throwable) {
                //roll back
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

    @After("pointcut()")
    public void after() {

    }
}
