package com.huanghe.mock.mockstatic.service;

import com.huanghe.mock.common.User;
import com.huanghe.mock.mockstatic.dao.UserDao;

public class UserService {


    /**
     * 查询用户数
     */
    public int queryUserCount(){
        System.out.println("queryUserCount");
        return UserDao.getCount();
    }


    /**
     * 插入用户
     * @param user
     */
    public void saveUser(User user){
        UserDao.insertUser(user);
    }
}
