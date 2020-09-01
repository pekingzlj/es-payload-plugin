package org.plugin;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.similarity.AbstractSimilarityProvider;

public class PayloadSimilarityProvider extends AbstractSimilarityProvider {

    private final PayloadSimilarity similarity;

    @Inject
    public PayloadSimilarityProvider(@Assisted String name, @Assisted Settings settings) {
        super(name);
        this.similarity = new PayloadSimilarity();
    }

    @Override
    public PayloadSimilarity get() {
        return similarity;
    }
}
