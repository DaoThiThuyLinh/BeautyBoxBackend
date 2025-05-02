package org.beautybox.binder;

import org.beautybox.bridge.TotalReviewBridge;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.binding.PropertyBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.PropertyBinder;

public class TotalReviewBinder implements PropertyBinder {
    @Override
    public void bind(PropertyBindingContext context) {
        context.dependencies().useRootOnly();

        context.indexSchemaElement().field("totalReview", f -> f.asLong().sortable(Sortable.YES)).toReference();

        context.bridge(String.class, new TotalReviewBridge("totalReview"));
    }
}
