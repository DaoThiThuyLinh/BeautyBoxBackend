package org.beautybox.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchAnalysisConfig implements ElasticsearchAnalysisConfigurer {
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.analyzer("vietnameseAnalyzer")
                .custom()
                .tokenizer("standard")
                .tokenFilters("lowercase", "asciifolding", "vietnamese_stop");
        context.tokenFilter("vietnamese_stop")
                .type("stop")
                .param("stopwords",
                        "và", "là", "của", "cho", "một", "các", "đã", "này", "những", "tôi", "bạn"
                );
    }
}
