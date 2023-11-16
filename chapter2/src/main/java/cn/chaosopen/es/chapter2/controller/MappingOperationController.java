package cn.chaosopen.es.chapter2.controller;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * 2.2 映射操作
 */
@RestController
public class MappingOperationController {

    @Autowired
    private RestHighLevelClient client;

    @RequestMapping("/getMapping")
    public Map getMapping(String indexName) throws IOException {
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(indexName);
        GetMappingsResponse mappingsResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);
        Map<String, MappingMetadata> allMappings = mappingsResponse.mappings();
        MappingMetadata indexMapping = allMappings.get(indexName);
        Map<String, Object> mapping = indexMapping.sourceAsMap();
        return mapping;
    }

    @RequestMapping("/addMapping")
    public Boolean addMapping(String indexName) {
        PutMappingRequest request = new PutMappingRequest(indexName);
        request.source(
                "{\n" +
                        "  \"properties\":{\n" +
                        "    \"name3\":{\n" +
                        "      \"type\":\"keyword\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}", XContentType.JSON);
        try {
            client.indices().putMapping(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
