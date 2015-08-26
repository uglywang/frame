package cn.ssms.service;

import cn.ssms.model.Role;
import cn.ssms.model.User;
import cn.ssms.model.UserRole;

public interface UserService {
	public User getUserById(Integer id);

	public User findUserByLoginName(String username);

    public UserRole findUserRoleByUserID(Integer userid);

    public Role findRoleByRoleID(Integer roleid);
}
