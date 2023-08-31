package com.swx.search.service.impl;

import com.alibaba.fastjson2.JSON;
import com.swx.base.exception.BizException;
import com.swx.search.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    private final RestHighLevelClient restHighLevelClient;

    public IndexServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * 添加索引
     *
     * @param indexName 索引名称
     * @param id        主键
     * @param object    索引对象
     * @return Boolean true表示成功,false失败
     */
    @Override
    public Boolean addCourseIndex(String indexName, String id, Object object) {
        String jsonString = JSON.toJSONString(object);
        IndexRequest indexRequest = new IndexRequest(indexName).id(id);
        // 指定索引文档内容
        indexRequest.source(jsonString, XContentType.JSON);
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("添加索引出错: ", e);
            throw new BizException("添加索引出错");
        }
        String name = indexResponse.getResult().name();
        return name.equalsIgnoreCase("created") || name.equalsIgnoreCase("updated");
    }

    /**
     * 更新索引
     *
     * @param indexName 索引名称
     * @param id        主键
     * @param object    索引对象
     * @return Boolean true表示成功,false失败
     */
    @Override
    public Boolean updateCourseIndex(String indexName, String id, Object object) {
        String jsonString = JSON.toJSONString(object);
        UpdateRequest updateRequest = new UpdateRequest(indexName, id);
        updateRequest.doc(jsonString, XContentType.JSON);
        UpdateResponse updateResponse = null;
        try {
            updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("更新索引出错: ",e);
            throw new BizException("更新索引出错");
        }
        DocWriteResponse.Result result = updateResponse.getResult();
        return result.name().equalsIgnoreCase("updated");
    }

    /**
     * 删除索引
     *
     * @param indexName 索引名称
     * @param id        主键
     * @return java.lang.Boolean
     */
    @Override
    public Boolean deleteCourseIndex(String indexName, String id) {
        //删除索引请求对象
        DeleteRequest deleteRequest = new DeleteRequest(indexName,id);
        //响应对象
        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("删除索引出错: ",e);
            throw new BizException("删除索引出错");
        }
        //获取响应结果
        DocWriteResponse.Result result = deleteResponse.getResult();
        return result.name().equalsIgnoreCase("deleted");
    }
}
