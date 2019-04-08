package org.happybean.butler.transaction;

import org.happybean.butler.annition.BuTransactional;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;

/**
 * @author wgt
 * @date 2019-03-31
 * @description
 **/
@Deprecated
public class DubboTransactionAnnotationParser extends SpringTransactionAnnotationParser {

    @Override
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae) {
        AnnotationAttributes attributes = null;
        if (ae.isAnnotationPresent(Transactional.class)) {
            attributes = AnnotatedElementUtils
                    .getAnnotationAttributes(ae, Transactional.class.getName());
        } else if (ae.isAnnotationPresent(BuTransactional.class)) {
            attributes = AnnotatedElementUtils
                    .getAnnotationAttributes(ae, BuTransactional.class.getName());
        }

        if (attributes != null) {
            return parseTransactionAnnotation(attributes);
        } else {
            return null;
        }
    }

    public TransactionAttribute parseTransactionAnnotation(BuTransactional anno) {
        return parseTransactionAnnotation(AnnotationUtils.getAnnotationAttributes(anno, false, false));
    }
}
