package org.happybean.butler.dubbo.transaction;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
public class DubboTransactionInterceptor extends TransactionInterceptor {
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        System.out.println("transaction method :" +
                invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName());
        Object object = super.invoke(invocation);
        System.out.println(invocation.getMethod().getName() + " result :" + object);
        return object;
    }
}
