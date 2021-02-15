package com.zy.service;

import com.alibaba.fastjson.JSONObject;
import com.zy.pojo.User;
import com.zy.pojo.bo.UserBO;

import java.util.List;

public interface UserService {
    User queryMyFriends(String id) throws Exception;

    List<User> queryFriendsByUsername(String username) throws Exception;

    void addFriend(UserBO userBO) throws Exception;

    List<User> getMyFriends(JSONObject jsonObject)throws Exception;
}
