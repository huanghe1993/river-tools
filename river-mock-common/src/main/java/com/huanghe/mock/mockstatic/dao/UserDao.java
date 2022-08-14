package com.huanghe.mock.mockstatic.dao;

import com.huanghe.mock.common.User;

public class UserDao {

    public static int getCount(){
        throw new UnsupportedOperationException();
    }

    public static void insertUser(User user){
        throw new UnsupportedOperationException();
    }
}
