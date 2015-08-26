package cn.ssms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ssms.dao.RoleMapper;
import cn.ssms.dao.UserMapper;
import cn.ssms.dao.UserRoleMapper;
import cn.ssms.model.Role;
import cn.ssms.model.User;
import cn.ssms.model.UserRole;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	//此处没有注入，导致报空指针异常！
	private RoleMapper roleMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;

	public User getUserById(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	public User findUserByLoginName(String username) {
		System.out.println("findUserByLoginName call!");
		org.apache.ibatis.logging.LogFactory.useLog4JLogging();
		return userMapper.findUserByLoginName(username);
	}

//    public Role getUserRoleByUser(User user) {
//        System.out.println("getUserRoles call!");
//        UserRole userRole = userRoleMapper.getUserRoleByUserID(user.getId());
//        Role role = roleMapper.getRoleByUserID(userRole.getRoleId());
//        return role;
//    }

    public UserRole findUserRoleByUserID(Integer userid) {
        System.out.println("findUserRoleByUserID call!");
//        org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        return userRoleMapper.findUserRoleByUserID(userid);
    }

    public Role findRoleByRoleID(Integer roleid) {
        System.out.println("findRoleByRoleID call!");
//        org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        return roleMapper.findRoleByRoleID(roleid);
    }

}
