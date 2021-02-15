package com.zy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zy.mapper.UserMapper;
import com.zy.pojo.User;
import com.zy.pojo.bo.UserBO;
import com.zy.service.UserService;
import com.zy.utils.NettyUtil;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryMyFriends(String id) throws Exception {
        return userMapper.getUserById(id);
    }

    @Override
    public List<User> queryFriendsByUsername(String username) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("username", username);
        return userMapper.getUserListByMap(param);
    }

    @Override
    public void addFriend(UserBO userBO) throws Exception {
        JSONObject jsonObject;
        String id = userBO.getId();
        String friendId = userBO.getFriendId();
        User user = this.queryMyFriends(id);
        String result = user.getFriendIds();
        if (StringUtils.isBlank(result)) {
            jsonObject = new JSONObject();
            jsonObject.put(friendId,friendId);
            user.setUPDATEDTIME(new Date());
            user.setFriendIds(jsonObject.toJSONString());
            userMapper.updateUser(user);
        }else{
            jsonObject = JSONObject.parseObject(result);
            String json = jsonObject.getString(friendId);
            if (StringUtils.isBlank(json)) {
                jsonObject.put(friendId,friendId);
                user.setUPDATEDTIME(new Date());
                user.setFriendIds(jsonObject.toJSONString());
                userMapper.updateUser(user);
            }
        }


    }

    @Override
    public List<User> getMyFriends(JSONObject jsonObject) throws Exception {
        Map<String,String> param = jsonObject.toJavaObject(Map.class);
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> stringStringEntry : param.entrySet()) {
            list.add( stringStringEntry.getKey());
        }
        List<User> myFriends = userMapper.getMyFriends(list);
        myFriends.stream().forEach(node->{
            if (NettyUtil.S_C.containsKey(node.getId())) {
                //如果用户在线
                node.setIsOnline(true);
            }else{
                node.setIsOnline(false);
            }
        });

        return myFriends;
    }
}
