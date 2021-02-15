package com.zy.controller;

import com.alibaba.fastjson.JSONObject;
import com.zy.pojo.User;
import com.zy.pojo.bo.UserBO;
import com.zy.service.UserService;
import com.zy.utils.IMOOCJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wechat")
public class WeChatController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatController.class);


    /**
     * 根据id查询自己的好友
     *
     * @param id
     * @return
     */
    @GetMapping("/queryMyFriends")
    public IMOOCJSONResult queryMyFriends(@RequestParam String id) {
        try {
            User result = userService.queryMyFriends(id);
            if (result != null) {
                JSONObject jsonObject = JSONObject.parseObject(result.getFriendIds());
                if (jsonObject== null || jsonObject.size()==0) {
                    return IMOOCJSONResult.ok();
                }
                List<User> lists = userService.getMyFriends(jsonObject);
                return IMOOCJSONResult.ok(lists);
            }
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }
    }

    /**
     * 根据用户名查询是否有此人
     *
     * @param username
     * @return
     */
    @GetMapping("/queryFriendsByUsername")
    public IMOOCJSONResult queryFriendsByUsername(@RequestParam String username) {
        try {
            List<User> users = userService.queryFriendsByUsername(username);
            return IMOOCJSONResult.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }
    }

    /**
     * 添加好友
     *
     * @param userBO
     * @return
     */
    @PostMapping("/addFriend")
    public IMOOCJSONResult addFriend(@RequestBody UserBO userBO) {
        try {
            userService.addFriend(userBO);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }
    }

}
