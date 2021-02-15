package com.zy.mapper;
import com.zy.pojo.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

	public User getUserById(@Param(value = "id") String id)throws Exception;

	public List<User>	getUserListByMap(Map<String,Object> param)throws Exception;
	public List<User>	getMyFriends(@Param(value = "paramsList") List<String> paramsList)throws Exception;

	public Integer getUserCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertUser(User user)throws Exception;

	public Integer updateUser(User user)throws Exception;


}
