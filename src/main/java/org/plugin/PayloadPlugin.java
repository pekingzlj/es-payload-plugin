package org.plugin;

import org.elasticsearch.index.IndexModule;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;
import java.util.List;
import java.util.Collections;

public class PayloadPlugin extends Plugin implements SearchPlugin {
	@Override
	public List<QuerySpec<?>> getQueries() {
				return Collections.singletonList(
				new QuerySpec<>(PayloadQueryBuilder.NAME, PayloadQueryBuilder::new, PayloadQueryBuilder::fromXContent)
		);
	}

	@Override
	public void onIndexModule(IndexModule indexModule) {
		indexModule.addSimilarity("customer-similarity", PayloadSimilarityProvider::new);
	}
}