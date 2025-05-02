package org.beautybox.bridge;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.mapper.orm.HibernateOrmExtension;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;

public class TotalReviewBridge implements PropertyBridge<String> {

    private final String fieldName;

    public TotalReviewBridge(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void write(DocumentElement target, String productId, PropertyBridgeWriteContext context) {
        Long totalReview = context.extension(HibernateOrmExtension.get()).session().createQuery("SELECT COALESCE(COUNT(r.id), 0L) " +
                                "FROM Review r " +
                                "WHERE r.orderItem.productId = :productId",
                        Long.class)
                .setParameter("productId", productId)
                .getSingleResult();

        target.addValue(fieldName, totalReview != null ? totalReview : 0L);
    }
}
