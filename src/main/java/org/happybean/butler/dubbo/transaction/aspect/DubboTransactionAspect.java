package org.happybean.butler.dubbo.transaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.happybean.butler.dubbo.annition.DubboTransactional;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;


/**
 * @author wgt
 * @date 2019-04-05
 * @description
 **/
@Aspect
public class DubboTransactionAspect implements Ordered {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public void around(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DubboTransactional dubboTransactional = method.getAnnotation(DubboTransactional.class);
        Transactional springTransactional = method.getAnnotation(Transactional.class);
        if(dubboTransactional.remote()){

        }
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
