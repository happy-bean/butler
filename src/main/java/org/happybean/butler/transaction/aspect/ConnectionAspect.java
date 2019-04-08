package org.happybean.butler.transaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.happybean.butler.connection.BuConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
@Aspect
public class ConnectionAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point) throws SQLException {
        Connection connection = null;
        try {
            connection = (Connection) point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        BuConnection buConnection = new BuConnection(connection);
        buConnection.setAutoCommit(false);
        return buConnection;
    }
}
