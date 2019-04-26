package org.happybean.butler.transaction.aspect;

import com.alibaba.dubbo.rpc.RpcContext;
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
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
@Aspect
public class TransactionAspect implements Ordered {

    private final ThreadLocal<LocalTransaction> localTransaction = new ThreadLocal<>();

    @Pointcut("@annotation(org.happybean.butler.annition.BuTransactional)")
    public void pointcut() {

    }

    public void doCommit(Connection connection, BuTransactional buTransactional) throws SQLException {
        Object groupIdO = RpcContext.getContext().getAttachments().get("GROUP_ID");
        String groupId = null;
        if (groupIdO != null) {
            groupId = (String) groupIdO;
            String transactionId = BuTransactionManager.getTransactionId(groupId);
            LocalTransaction localTransaction = new LocalTransaction(transactionId, groupId, connection);
            BuTransactionManager.addTransactional(localTransaction);
        } else {
            connection.commit();
            groupId = BuTransactionManager.getGroupId();
        }
        RemoteTx remoteTx = BuTransactionManager.createRemoteTx();
        String transactionId = BuTransactionManager.getTransactionId(groupId);
        remoteTx.setTransactionId(transactionId);
        remoteTx.setGroupId(groupId);
        remoteTx.setCommand(TxCommand.COMMIT);
        doRemote(buTransactional, remoteTx);
    }


    public void doRollBack(Connection connection, BuTransactional buTransactional) throws SQLException {
        Object groupIdO = RpcContext.getContext().getAttachments().get("GROUP_ID");
        String groupId = null;
        if (groupIdO != null) {
            groupId = (String) groupIdO;
            String transactionId = BuTransactionManager.getTransactionId(groupId);
            LocalTransaction localTransaction = new LocalTransaction(transactionId, groupId, connection);
            BuTransactionManager.addTransactional(localTransaction);
        } else {
            connection.commit();
            groupId = BuTransactionManager.getGroupId();
        }
        RemoteTx remoteTx = BuTransactionManager.createRemoteTx();
        String transactionId = BuTransactionManager.getTransactionId(groupId);
        remoteTx.setTransactionId(transactionId);
        remoteTx.setGroupId(groupId);
        remoteTx.setCommand(TxCommand.ROLLBACK);
        doRemote(buTransactional, remoteTx);
    }

    public void doRemote(BuTransactional buTransactional, RemoteTx remoteTx) {
        if (buTransactional.remote()) {
            Remoter remoter = RemoterFactory.getRemoter(buTransactional.register());

            remoter.sendRemoter(remoteTx);
        }
    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint point) throws SQLException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        BuTransactional buTransactional = method.getAnnotation(BuTransactional.class);
        LocalTransaction localTransaction = this.localTransaction.get();
        Connection connection = localTransaction.getConnection();
        try {
            point.proceed();
            doCommit(connection, buTransactional);
        } catch (Throwable throwable) {
            doRollBack(connection, buTransactional);
        }
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
