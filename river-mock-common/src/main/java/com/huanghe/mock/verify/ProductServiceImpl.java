package com.huanghe.mock.verify;

public class ProductServiceImpl {

    public String sayAndWait(long million) {
        try {
            Thread.sleep(million);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "callMethod sleep " + million;
    }
}
