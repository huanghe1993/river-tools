package com.huanghe.mock.mockabstract;


import com.huanghe.mock.mockabstract.MockDemo1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MockDemo1.class})
public class MockDemo1Test {


    @Test
    public void m1_mock() {
        MockDemo1 mockDemo1 = PowerMockito.spy(new MockDemo1());
        // Spy中用doReturn..when才会不执行真实的方法。
        PowerMockito.doReturn("m1-mock").when(mockDemo1).m1(anyString());
        mockDemo1.test();
    }

    @Test
    public void test_demo() throws Exception {
        MockDemo1 mockDemo1 = PowerMockito.spy(new MockDemo1());
        PowerMockito.doReturn("m2-mock").when(mockDemo1, "m2", anyString());
        mockDemo1.test();
    }
}