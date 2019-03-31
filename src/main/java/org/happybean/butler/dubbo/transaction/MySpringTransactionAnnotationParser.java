package org.happybean.butler.dubbo.transaction;

import org.happybean.butler.dubbo.annition.DubboTransactional;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
public class MySpringTransactionAnnotationParser extends SpringTransactionAnnotationParser {

    @Override
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae) {

        AnnotationAttributes attributes = AnnotatedElementUtils
                .getAnnotationAttributes(ae, DubboTransactional.class.getName());
        if (attributes != null) {
            return parseTransactionAnnotation(attributes);
        } else {
            return null;
        }
    }
    public TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
        return parseTransactionAnnotation(AnnotationUtils.getAnnotationAttributes(attributes, false, false));
    }
}
