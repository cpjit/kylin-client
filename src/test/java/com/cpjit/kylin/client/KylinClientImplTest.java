package com.cpjit.kylin.client;

import org.junit.Test;

public class KylinClientImplTest {

    @Test
    public void test() throws Exception {
        KylinClient kylinClient = new KylinClientImpl("http://10.0.188.8:8106/kylin", "ADMIN", "KYLIN");
    }

}
