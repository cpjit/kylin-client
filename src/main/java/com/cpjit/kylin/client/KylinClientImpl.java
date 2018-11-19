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
package com.cpjit.kylin.client;

import com.cpjit.kylin.client.query.Query;
import com.cpjit.kylin.client.query.impl.QueryImpl;
import org.apache.commons.codec.binary.Base64;


/**
 * @author yonghuan
 */
public class KylinClientImpl implements KylinClient {

    private String server;
    private String authorization;

    public KylinClientImpl(String server, String username, String password) {
        this.server = server;
        byte[] key = (username + ":" + password).getBytes();
        authorization = Base64.encodeBase64String(key);
    }


    @Override
    public Query createQuery(String sql) {
        QueryImpl query = new QueryImpl(sql, server, authorization);
        return query;
    }
}
