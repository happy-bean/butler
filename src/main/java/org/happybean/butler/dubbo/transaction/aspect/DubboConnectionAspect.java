package org.happybean.butler.dubbo.transaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.happybean.butler.dubbo.connection.DubboConnection;

import java.sql.Connection;

/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
@Aspect
public class DubboConnectionAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point) {
        Connection connection = null;
        try {
            connection = (Connection) point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return new DubboConnection(connection);
    }
}
