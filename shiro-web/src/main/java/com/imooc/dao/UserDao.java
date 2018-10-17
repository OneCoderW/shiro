package com.imooc.dao;

import com.imooc.vo.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    User getUserByUserName(String userName);

    List<String> getRolesByUserName(String userName);
}
