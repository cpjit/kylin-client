/*
 * Copyright 2011-2018 CPJIT Group.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpjit.kylin.client.query.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.cpjit.kylin.client.query.NonUniqueResultException;
import com.cpjit.kylin.client.query.Query;
import com.cpjit.kylin.client.query.QueryException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author yonghuan
 */
public class QueryImpl implements Query {

    private String server;
    private String authorization;

    private String project;
    private String sql;
    private Integer offset;
    private Integer limit;
    private boolean acceptPartial;
    private boolean useLowerCase;

    public QueryImpl(String sql, String server, String authorization) {
        this.sql = sql;
        this.server = server;
        this.authorization = authorization;
    }

    @Override
    public List<?> list() throws QueryException {
        CloseableHttpClient http = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(server + "/api/query");
        post.addHeader("Authorization", "Basic " + authorization);
        Map<String, Object> param = new HashMap<String, Object>();
        if (project != null) {
            param.put("project", project);
        }
        param.put("sql", sql);
        if (offset != null) {
            param.put("offset", offset);
        }
        if (limit != null) {
            param.put("limit", limit);
        }
        if (acceptPartial) {
            param.put("acceptPartial", true);
        }
        post.setEntity(new StringEntity(JSON.toJSONString(param), ContentType.APPLICATION_JSON));
        InputStream is;
        try {
            HttpResponse resp = http.execute(post);
            StatusLine statusLine = resp.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                throw new QueryException("查询失败，Http StatusCode=" + statusLine.getStatusCode() + "," + statusLine.getReasonPhrase());
            }
            is = resp.getEntity().getContent();
        } catch (IOException ioe) {
            closeHttpClient(http);
            throw new QueryException("查询失败", ioe);
        }
        JSONReader reader = new JSONReader(new InputStreamReader(is));
        JSONObject body = (JSONObject) reader.readObject();

        JSONArray results = body.getJSONArray("results");
        closeHttpClient(http);
        if (results.isEmpty()) {
            return Collections.emptyList();
        }

        JSONArray columnMetas = body.getJSONArray("columnMetas");
        List<Object> items = new ArrayList<Object>(results.size());
        for (Object o : body.getJSONArray("results")) {
            JSONArray values = (JSONArray) o;
            Map<String, Object> item = new HashMap<String, Object>(values.size());
            int valueIndex = 0;
            for (Object value : values) {
                JSONObject columnMeta = columnMetas.getJSONObject(valueIndex++);
                String columnName = columnMeta.getString("name");
                if (useLowerCase) {
                    columnName = columnName.toLowerCase();
                }
                item.put(columnName, value);
            }
            items.add(item);
        }
        return items;
    }

    private void closeHttpClient(CloseableHttpClient http) {
        try {
            http.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public Object uniqueResult() {
        List<Map<String, Object>> items = (List<Map<String, Object>>) list();
        if (items.size() > 1) {
            throw new NonUniqueResultException(items.size());
        }
        if (items.size() == 1) {
            return items.get(0).values().iterator().next();
        }
        return null;
    }

    @Override
    public Query setProject(String project) {
        this.project = project;
        return this;
    }

    @Override
    public Query offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query acceptPartial() {
        this.acceptPartial = true;
        return this;
    }

    @Override
    public Query useLowerCase() {
        this.useLowerCase = true;
        return this;
    }
}
