package com.huanghe.mock.spymock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Calculator.class})
public class CalculatorTest {

    @Test
    public void testSumXXBySpy_Call_Private_Method() throws Exception {
        Calculator cal= PowerMockito.spy(new Calculator());
        // 会调用真实的方法，mock掉了私有的方法
        PowerMockito.when(cal,"sumXX",anyInt(),anyInt()).thenReturn(4);
        int i = cal.callSumXX(1, 2);
        assertEquals(4, cal.callSumXX(1, 2));
    }


    @Test
    public void testSumXXBySpy_Not_Call_Private_Method() throws Exception {
        Calculator cal= PowerMockito.spy(new Calculator());
        // 不会调用真实的方法
        PowerMockito.doReturn(2).when(cal,"sumXX",anyInt(),anyInt());
        assertEquals(2, cal.callSumXX(1, 2));
    }

    @Test
    public void testSumXXByMock_Not_Call_Real_Method() throws Exception {
        // mock默认不执行，有返回值的，默认返回null
        Calculator cal= PowerMockito.mock(Calculator.class);
        PowerMockito.when(cal,"sumXX",anyInt(),anyInt()).thenReturn(2);
        assertEquals(0, cal.callSumXX(1, 2));
    }
    @Test
    public void testSumXXByMock_Call_Real_Method() throws Exception {
        Calculator cal= PowerMockito.mock(Calculator.class);
        PowerMockito.when(cal,"sumXX",anyInt(),anyInt()).thenReturn(2);
        // 指明callSumXX调用真实的方法,其他的不调用真实的方法
        PowerMockito.when(cal.callSumXX(anyInt(),anyInt())).thenCallRealMethod();
        assertEquals(2, cal.callSumXX(1, 2));
    }
}