package com.zy.pojo.bo;

import java.io.Serializable;

public class UserBO implements Serializable {
    private String id;
    private String friendId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
