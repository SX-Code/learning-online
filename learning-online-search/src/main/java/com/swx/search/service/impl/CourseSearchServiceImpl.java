package com.swx.search.service.impl;

import com.alibaba.fastjson2.JSON;
import com.swx.base.model.PageParam;
import com.swx.search.dto.SearchCourseParamDTO;
import com.swx.search.vo.SearchPageResultVO;
import com.swx.search.po.CourseIndex;
import com.swx.search.service.CourseSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RefreshScope
public class CourseSearchServiceImpl implements CourseSearchService {

    @Value("${elasticsearch.course.index}")
    private String courseIndexStore;

    @Value("${elasticsearch.course.source-fields}")
    private String sourceFields;

    private final RestHighLevelClient restHighLevelClient;

    public CourseSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * 搜索课程列表
     *
     * @param pageParam 分页参数
     * @param dto       搜索条件
     * @return com.swx.base.model.PageResult<com.swx.search.po.CourseIndex> 课程列表
     */
    @Override
    public SearchPageResultVO<CourseIndex> queryCoursePubIndex(PageParam pageParam, SearchCourseParamDTO dto) {
        // 设置索引
        SearchRequest request = new SearchRequest(courseIndexStore);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // source源字段过滤
        String[] sourceFieldsArray = sourceFields.split(",");
        builder.fetchSource(sourceFieldsArray, new String[]{});
        if (dto == null) {
            dto = new SearchCourseParamDTO();
        }

        // 关键字
        if (StringUtils.hasText(dto.getKeywords())) {
            // 匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(dto.getKeywords(), "name", "description");
            //设置匹配占比
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //提升另个字段的Boost值
            multiMatchQueryBuilder.field("name", 10);
            boolQuery.must(multiMatchQueryBuilder);
        }
        //过滤
        if (StringUtils.hasText(dto.getMt())) {
            boolQuery.filter(QueryBuilders.termQuery("mtName", dto.getMt()));
        }
        if (StringUtils.hasText(dto.getSt())) {
            boolQuery.filter(QueryBuilders.termQuery("stName", dto.getSt()));
        }
        if (StringUtils.hasText(dto.getGrade())) {
            boolQuery.filter(QueryBuilders.termQuery("grade", dto.getGrade()));
        }
        //分页
        Long pageNo = pageParam.getPageNo();
        Long pageSize = pageParam.getPageSize();
        int start = (int) ((pageNo - 1) * pageSize);
        builder.from(start).size(Math.toIntExact(pageSize));
        //布尔查询
        builder.query(boolQuery);
        // 高亮 title
        builder.highlighter(new HighlightBuilder()
                .field("name")
                .preTags("<font style='color:red;font-size:inherit;'>")
                .postTags("</font>"));
        // 请求搜索
        request.source(builder);
        //聚合设置
        buildAggregation(request);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("课程搜索异常：", e);
            return new SearchPageResultVO<CourseIndex>(new ArrayList<CourseIndex>(), 0, 0, 0);
        }

        //结果集处理
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        //记录总数
        TotalHits totalHits = hits.getTotalHits();
        //数据列表
        List<CourseIndex> list = new ArrayList<>();

        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            CourseIndex courseIndex = JSON.parseObject(sourceAsString, CourseIndex.class);
            //课程id
            Long id = courseIndex.getId();
            //取出名称
            String name = courseIndex.getName();
            //取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                HighlightField nameField = highlightFields.get("name");
                if (nameField != null) {
                    Text[] names = nameField.getFragments();
                    StringBuilder stringBuffer = new StringBuilder();
                    for (Text str : names) {
                        stringBuffer.append(str.string());
                    }
                    name = stringBuffer.toString();
                }
            }
            courseIndex.setId(id);
            courseIndex.setName(name);
            list.add(courseIndex);
        }
        SearchPageResultVO<CourseIndex> pageResult = new SearchPageResultVO<>(list, totalHits.value, pageNo, pageSize);
        //获取聚合结果
        List<String> mtList= getAggregation(searchResponse.getAggregations(), "mtAgg");
        List<String> stList = getAggregation(searchResponse.getAggregations(), "stAgg");
        pageResult.setMtList(mtList);
        pageResult.setStList(stList);

        return pageResult;
    }

    private void buildAggregation(SearchRequest request) {
        request.source().aggregation(AggregationBuilders
                .terms("mtAgg")
                .field("mtName")
                .size(100)
        );
        request.source().aggregation(AggregationBuilders
                .terms("stAgg")
                .field("stName")
                .size(100)
        );
    }

    private List<String> getAggregation(Aggregations aggregations, String aggName) {
        // 4.1.根据聚合名称获取聚合结果
        Terms brandTerms = aggregations.get(aggName);
        // 4.2.获取buckets
        List<? extends Terms.Bucket> buckets = brandTerms.getBuckets();
        // 4.3.遍历
        List<String> brandList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            // 4.4.获取key
            String key = bucket.getKeyAsString();
            brandList.add(key);
        }
        return brandList;
    }
}
