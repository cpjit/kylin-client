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
package com.cpjit.kylin.client.query;

import com.cpjit.kylin.client.KylinClient;
import com.cpjit.kylin.client.KylinClientImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author yonghuan
 */
public class QueryTest {

    private KylinClient kylinClient;

    @Before
    public void init() {
        kylinClient = new KylinClientImpl("http://10.0.188.8:8106/kylin", "ADMIN", "KYLIN");
    }

    @Test
    public void testQuery() {
        String sql = "select BUYER_ID, count(*) total from KYLIN_SALES group by BUYER_ID";
        List<?> result = kylinClient.createQuery(sql)
                .setProject("learn_kylin")
                .useLowerCase()
                .limit(10)
                .list();
        System.out.println(result);
    }

    @Test
    public void testUniqueResult() {
        String sql = "select count(*) from KYLIN_SALES";
        Object count = kylinClient.createQuery(sql)
                .setProject("learn_kylin")
                .uniqueResult();
        System.out.println("count: " + count);
    }
}
