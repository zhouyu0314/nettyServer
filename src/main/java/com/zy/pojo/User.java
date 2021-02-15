package com.zy.pojo;
import java.io.Serializable;
import java.util.Date;
/***
*   用户表 
*/
public class User implements Serializable {
    //乐观锁
    private Integer REVISION;
    //创建人
    private String CREATEDBY;
    //创建时间
    private Date CREATEDTIME;
    //更新人
    private String UPDATEDBY;
    //更新时间
    private Date UPDATEDTIME;
    //头像
    private String headImg;
    //id
    private String id;
    //用户名
    private String username;
    //密码
    private String password;
    //好友id
    private String friendIds;

    private boolean isOnline;

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean online) {
        isOnline = online;
    }

    //get set 方法
    public void setREVISION (Integer  REVISION){
        this.REVISION=REVISION;
    }
    public  Integer getREVISION(){
        return this.REVISION;
    }
    public void setCREATEDBY (String  CREATEDBY){
        this.CREATEDBY=CREATEDBY;
    }
    public  String getCREATEDBY(){
        return this.CREATEDBY;
    }
    public void setCREATEDTIME (Date  CREATEDTIME){
        this.CREATEDTIME=CREATEDTIME;
    }
    public  Date getCREATEDTIME(){
        return this.CREATEDTIME;
    }
    public void setUPDATEDBY (String  UPDATEDBY){
        this.UPDATEDBY=UPDATEDBY;
    }
    public  String getUPDATEDBY(){
        return this.UPDATEDBY;
    }
    public void setUPDATEDTIME (Date  UPDATEDTIME){
        this.UPDATEDTIME=UPDATEDTIME;
    }
    public  Date getUPDATEDTIME(){
        return this.UPDATEDTIME;
    }
    public void setHeadImg (String  headImg){
        this.headImg=headImg;
    }
    public  String getHeadImg(){
        return this.headImg;
    }
    public void setId (String  id){
        this.id=id;
    }
    public  String getId(){
        return this.id;
    }
    public void setUsername (String  username){
        this.username=username;
    }
    public  String getUsername(){
        return this.username;
    }
    public void setPassword (String  password){
        this.password=password;
    }
    public  String getPassword(){
        return this.password;
    }
    public void setFriendIds (String  friendIds){
        this.friendIds=friendIds;
    }
    public  String getFriendIds(){
        return this.friendIds;
    }
}
