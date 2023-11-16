package cn.chaosopen.es.chapter5.controller;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ProductSearchController {

    @Autowired
    private RestHighLevelClient client;

    @RequestMapping("/keywordSearch")
    public SearchHit[] keywordSearch(@RequestParam String keyword) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("productName", keyword));
        //相关性高的文档加权
        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("productName", keyword)
                .slop(4).boost(2));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("product_index");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse.getHits().getHits();
    }
}
