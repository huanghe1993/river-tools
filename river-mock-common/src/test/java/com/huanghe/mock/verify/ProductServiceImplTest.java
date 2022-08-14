package com.huanghe.mock.verify;

import org.junit.Test;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

public class ProductServiceImplTest {

    /**
     * after 会阻塞等满时间之后再往下执行，是固定等待多长时间的语义
     * timeout 在等待期内，拿到结果后立即向下执行，不做多余等待；是最多等待多长时间的语义
     */
    @Test
    public void sayAndWait() throws ExecutionException, InterruptedException {
        ProductServiceImpl mock = PowerMockito.mock(ProductServiceImpl.class);

        // Verification with timeout is intended to be used to verify whether or not
        // the operation has been invoked concurrently within the specified timeout.
        when(mock.sayAndWait(anyLong())).thenCallRealMethod();
        CompletableFuture<String> asyncFuture = CompletableFuture.supplyAsync(() -> mock.sayAndWait(1000L));
        //阻塞1100Lms，timeout的时候再验证是否刚好执行了1次
        verify(mock, timeout(1100L).times(1)).sayAndWait(1000L);
        System.out.println(asyncFuture.get());

        //timeout时间后，用自定义的检验模式验证someMethod()
        VerificationMode yourOwnVerificationMode = new VerificationMode() {
            @Override
            public void verify(VerificationData data) {
                // TODO: 2016/12/4 implement me
            }
        };

        // after() awaits full duration to check if verification passes
//        verify(mock, after(1100L).times(1)).sayAndWait(1000L);
    }
}