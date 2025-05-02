package org.beautybox.bridge;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.mapper.orm.HibernateOrmExtension;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;

public class TotalSoldBridge implements PropertyBridge<String> {

    private final String fieldName;

    public TotalSoldBridge(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void write(DocumentElement target, String productId, PropertyBridgeWriteContext context) {
        Long totalSold = context.extension(HibernateOrmExtension.get()).session().createQuery("SELECT COALESCE(SUM(oi.quantity), 0L) FROM OrderItem oi WHERE oi.productId = :productId",
                Long.class)
                .setParameter("productId", productId)
                .getSingleResult();

        target.addValue(fieldName, totalSold != null ? totalSold : 0L);
    }
}
