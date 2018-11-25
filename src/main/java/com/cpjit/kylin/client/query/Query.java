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

import java.util.List;

/**
 * @author yonghuan
 */
public interface Query {

    /**
     * @throws QueryException
     */
    List<?> list();

    /**
     * @throws QueryException
     */
    <T> List<T> list(Class<T> clazz);

    /**
     * @throws NonUniqueResultException
     */
    Object uniqueResult();

    /**
     * @param project Project to perform query. Default value is ‘DEFAULT’.
     */
    Query setProject(String project);

    /**
     * @param offset Query offset. If offset is set in sql, curIndex will be ignored.
     */
    Query offset(int offset);

    /**
     * @param limit Query limit. If limit is set in sql, perPage will be ignored.
     */
    Query limit(int limit);

    /**
     * Whether accept a partial result
     */
    Query acceptPartial();

    Query useLowerCase();

}
