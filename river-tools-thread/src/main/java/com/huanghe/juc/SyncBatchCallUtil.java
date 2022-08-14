package com.huanghe.juc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 同步的批量请求的工具类
 */
public class SyncBatchCallUtil {
    private static final ExecutorService executorPool;
    static{
        //线程池维护线程的最少数量。
        int corePoolSize=5;
        //线程池维护线程的最大数量
        int maximumPoolSize=10;
        //空闲时间是10秒
        long keepAliveTime=10;  
        // 单位
        TimeUnit unit=TimeUnit.SECONDS;
        //队列
        BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<>(6);
        //工厂
        ThreadFactory threadFactory=new UserThreadFactory("batch");
        //handler
        RejectedExecutionHandler handler=new MyRejectPolicy();
        executorPool=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);
    }

    public boolean batch(Task... tasks){
        //拿着N个任务创建countDownLatch对象
        return !createPool(tasks);
    }

    private boolean createPool(Task [] tasks){
        //有多少任务，就tasks.length就是多少，那么就需要countDownLatch来控制。
        CountDownLatch countDownLatch = new CountDownLatch(tasks.length);
        for (Task task : tasks) {
            task.setCountDownLatch(countDownLatch);
        }
        for (Task task : tasks) {
            executorPool.execute(task);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    static abstract class Task implements Runnable{
        /**
         * 执行抽象方法
         */
        public abstract void exe();

        /**
         * 计数器
         */
        private CountDownLatch countDownLatch;


        public void setCountDownLatch( CountDownLatch countDownLatch){
            this.countDownLatch=countDownLatch;
        }

        @Override
        public void run() {
            exe();
            countDownLatch.countDown();
        }
    }


    static class UserThreadFactory implements ThreadFactory{

        private final String namePrefix;
        private final AtomicInteger nextId=new AtomicInteger(1);

        UserThreadFactory(String whatFeatureOfGroup){
            namePrefix="FROM UserThreadFactory's "+whatFeatureOfGroup+"-Work-";
        }

        @Override
        public Thread newThread(Runnable task) {
            String name=namePrefix+nextId.getAndIncrement();
            return new Thread(null,task,name);
        }
    }

    static class MyRejectPolicy implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(r);
        }
    }
}
