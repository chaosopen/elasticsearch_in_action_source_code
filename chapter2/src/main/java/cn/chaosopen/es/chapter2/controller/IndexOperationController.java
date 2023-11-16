package cn.chaosopen.es.chapter2.controller;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 2.1 索引操作
 */
@RestController
public class IndexOperationController {

    @Autowired
    private RestHighLevelClient client;

    @RequestMapping("/createIndex")
    public Boolean createIndex(String indexName) {
        //创建索引请求类，构造函数参数为索引名称
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        //设置source映射字符串，直接把语句复制里面
        request.source(
                "{\n" +
                        "  \"settings\": {\n" +
                        "    \"number_of_shards\": 1,\n" +
                        "    \"number_of_replicas\": 1\n" +
                        "  },\n" +
                        "  \"mappings\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"name1\":{\n" +
                        "         \"type\": \"text\"\n" +
                        "      },\n" +
                        "      \"name2\":{\n" +
                        "        \"type\": \"integer\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
        try {
            //调用创建索引语法
            client.indices().create(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping("/deleteIndex")
    public Boolean deleteIndex(String indexName) {
        //删除索引请求类，构造函数参数为索引名称
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            //调用删除索引语法
            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping("/existsIndex")
    public Boolean existsIndex(String indexName) {
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping("/closeIndex")
    public Boolean closeIndex(String indexName) {
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest(indexName);
        try {
            client.indices().close(closeIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping("/openIndex")
    public Boolean openIndex(String indexName) {
        OpenIndexRequest openIndexRequest = new OpenIndexRequest(indexName);
        try {
            client.indices().open(openIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping("/addAlias")
    public Boolean addAlias(String indexName, String aliasName) {
        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD);
        aliasActions.index(indexName).alias(aliasName);
        indicesAliasesRequest.addAliasAction(aliasActions);
        try {
            client.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping("/removeAlias")
    public Boolean removeAlias(String indexName, String aliasName) {
        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE);
        aliasActions.index(indexName).alias(aliasName);
        indicesAliasesRequest.addAliasAction(aliasActions);
        try {
            client.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping("/changeAlias")
    public Boolean changeAlias() {
        String aliasName = "indexname_alias";
        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions addAliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD);
        addAliasActions.index("indexname1").alias(aliasName);
        IndicesAliasesRequest.AliasActions removeAliasActions = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE);
        removeAliasActions.index("indexname2").alias(aliasName);
        indicesAliasesRequest.addAliasAction(addAliasActions);
        indicesAliasesRequest.addAliasAction(removeAliasActions);
        try {
            client.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping("/selectIndexByAlias")
    public Map selectIndexByAlias(String aliasName) {
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest(aliasName);
        try {
            GetAliasesResponse response = client.indices().getAlias(getAliasesRequest,RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> aliases;
            aliases = response.getAliases();
            return aliases;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/selectAliasByIndex")
    public Map selectAliasByIndex(String indexName) {
        GetIndexRequest getIndexRequest = new GetIndexRequest();
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest();
        // 指定查看某一个索引的别名 不指定，则会搜索所有的别名
        getAliasesRequest.indices(indexName);
        try {
            GetAliasesResponse response = client.indices().getAlias(getAliasesRequest,RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> aliases;
            aliases = response.getAliases();
            return aliases;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/getAliasExist")
    public Boolean getAliasExist(String indexName, String aliasName) {
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest(aliasName);
        getAliasesRequest.indices(indexName);
        try {
            return client.indices().existsAlias(getAliasesRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
