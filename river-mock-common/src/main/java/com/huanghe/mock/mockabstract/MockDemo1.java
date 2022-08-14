package com.huanghe.mock.mockabstract;

public class MockDemo1 extends AbstractMockDemo {

    public void test() {
        String m1 = m1("m1");
        System.out.println("m1 method execute result:" + m1);

        String m2 = m2("m2");
        System.out.println("m2 method execute result:" + m2);

        String m3 = m3("m3");
        System.out.println("m3 method execute result:" + m3);

    }

    public String m2(String str) {
        return str;
    }
}
