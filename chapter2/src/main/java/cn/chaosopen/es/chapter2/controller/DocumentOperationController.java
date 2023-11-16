package cn.chaosopen.es.chapter2.controller;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

/**
 * 2.3 文档操作
 */
@RestController
public class DocumentOperationController {

    @Autowired
    private RestHighLevelClient client;

    @RequestMapping("/addDoc")
    public Boolean addDoc() throws IOException {
        IndexRequest request = new IndexRequest().index("index_operation");
        // 指定ID，也可以不指定随机生成
        request.id("21");
        // 这里用Map，也可以创建Java对象操作
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        map.put("description", "一个学习ES的学生");
        // map转换json字符串
        request.source(JSON.toJSONString(map), XContentType.JSON);
        // 请求es
        client.index(request, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/addBatchDoc")
    public Boolean addBatchDoc() throws IOException {
        // 批量插入数据
        BulkRequest request = new BulkRequest();
        // 生成10条数据
        for (int i = 0; i < 10; i++) {
            // 创建对象
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", "张三");
            map.put("age", 18 + i);
            map.put("description", "一个学习ES的学生");
            // 添加文档数据
            IndexRequest source = new IndexRequest().index("index_operation");
            source.source(JSON.toJSONString(map), XContentType.JSON);
            request.add(source);
        }
        // 请求es
        client.bulk(request, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/getDoc")
    public GetResponse getDoc() throws IOException {
        GetRequest request = new GetRequest()
                .index("index_operation")
                .id("1");
        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        return getResponse;
    }
    @RequestMapping("/batchGetDoc")
    public SearchHit[] batchGetDoc() throws IOException {
        SearchRequest request = new SearchRequest("index_operation");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.idsQuery().addIds("1", "4"));
        request.source(builder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        return searchHits;
    }

    @RequestMapping("/select")
    public SearchHit[] select() throws IOException {
        SearchRequest request = new SearchRequest("index_operation");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termQuery("age", 18));
        request.source(builder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        return searchHits;
    }

    @RequestMapping("/select2")
    public SearchHit[] select2() throws IOException {
        SearchRequest request = new SearchRequest("index_operation");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("age", 18));
        boolQueryBuilder.must(QueryBuilders.termQuery("name", "张三"));
        builder.query(boolQueryBuilder);
        request.source(builder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        return searchHits;
    }


    @RequestMapping("/update")
    public Boolean update() throws IOException {
        // 创建对象
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "张三1");
        map.put("age", 20);
        UpdateRequest request = new UpdateRequest().index("index_operation");
        request.id("1");
        // 添加文档数据
        request.doc(JSON.toJSONString(map), XContentType.JSON);
        // 请求es
        client.update(request, RequestOptions.DEFAULT);
        return true;
    }


    @RequestMapping("/batchUpdate")
    public Boolean batchUpdate() throws IOException {
        BulkRequest request = new BulkRequest();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("name", "张三41");
        map1.put("age", 18);
        map1.put("description", "测试4");
        IndexRequest source1 = new IndexRequest().index("index_operation").id("4");
        source1.source(JSON.toJSONString(map1), XContentType.JSON);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "张三51");
        map2.put("age", 18);
        map2.put("description", "测试5");
        IndexRequest source2 = new IndexRequest().index("index_operation").id("5");
        source2.source(JSON.toJSONString(map2), XContentType.JSON);
        request.add(source1);
        request.add(source2);
        // 请求es
        client.bulk(request, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/updateCondition1")
    public Boolean updateCondition1() throws IOException {
        UpdateByQueryRequest updateByQuery  = new UpdateByQueryRequest("index_operation");
        updateByQuery.setQuery(QueryBuilders.termQuery("age", 18));
        updateByQuery.setScript(new Script("ctx._source['name']='成年人'"));
        client.updateByQuery(updateByQuery, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/updateCondition2")
    public Boolean updateCondition2() throws IOException {
        UpdateByQueryRequest updateByQuery  = new UpdateByQueryRequest("index_operation");
        updateByQuery.setQuery(QueryBuilders.rangeQuery("age").gt(18).lt(25));
        updateByQuery.setScript(new Script("ctx._source['name']='青少年'"));
        client.updateByQuery(updateByQuery, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/delete")
    public Boolean delete() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("index_operation").id("1");
        client.delete(deleteRequest, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/deleteCondition1")
    public Boolean deleteCondition1() throws IOException {
        DeleteByQueryRequest updateByQuery  = new DeleteByQueryRequest("index_operation");
        updateByQuery.setQuery(QueryBuilders.termQuery("age", 18));
        client.deleteByQuery(updateByQuery, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/deleteCondition2")
    public Boolean deleteCondition2() throws IOException {
        DeleteByQueryRequest updateByQuery  = new DeleteByQueryRequest("index_operation");
        updateByQuery.setQuery(QueryBuilders.rangeQuery("age").gt(18).lt(25));
        client.deleteByQuery(updateByQuery, RequestOptions.DEFAULT);
        return true;
    }

    @RequestMapping("/deleteAll")
    public Boolean deleteAll() throws IOException {
        DeleteByQueryRequest updateByQuery  = new DeleteByQueryRequest("index_operation");
        updateByQuery.setQuery(QueryBuilders.matchAllQuery());
        client.deleteByQuery(updateByQuery, RequestOptions.DEFAULT);
        return true;
    }

}
