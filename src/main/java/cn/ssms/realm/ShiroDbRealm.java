package cn.ssms.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ssms.model.Role;
import cn.ssms.model.User;
import cn.ssms.model.UserRole;
import cn.ssms.service.UserService;

public class ShiroDbRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	private static final String ALGORITHM = "MD5";

	@Autowired
	private UserService userService;

	public ShiroDbRealm() {
		super();
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
	    /* 这里编写认证代码 */
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		System.out.println(token.getUsername());
		User user = userService.findUserByLoginName(token.getUsername());
		System.out.println(user);




		//根据用户名查找拥有的角色
//        Role role = userService.getUserRoleByUser(user);
//        if (role != null) {
//            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//            System.out.println(role.getName());
//            info.addRole(role.getName());
//        }

		if (user != null) {
			return new SimpleAuthenticationInfo(user.getName(), user.getPassword(), getName());
		}else{
			throw new AuthenticationException();
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {


	       //SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	        //获取当前登录的用户名

//	        List<String> permissions = null;

//	      if(user != null){
//	          //加载用户拥有的权限
//	          permissions =roleService.getHasPermissByUser(user.getId());
//	      }else{
//	          throw new AuthorizationException();
//	      }
	        //给当前用户设置权限
//	        info.addStringPermissions(permissions);
//	        return info;


		/* 这里编写授权代码 */
		Set<String> roleNames = new HashSet<String>();
	    Set<String> permissions = new HashSet<String>();

	    /**start**/
//        User user = (User) super.getAvailablePrincipal(principals);
	    String username = (String)super.getAvailablePrincipal(principals);
	    User user = userService.findUserByLoginName(username);
        UserRole userRole = userService.findUserRoleByUserID(user.getId());
        Role role = userService.findRoleByRoleID(userRole.getRoleid());
        String strrole = role.getName();
        System.out.println("!!!该用户的角色为：!!!!"+strrole);
	    roleNames.add(strrole);
	    /**end**/

	    //roleNames.add("admin");
	    roleNames.add("zhangsan");
	    permissions.add("user.do?myjsp");
	    permissions.add("login.do?main");
	    permissions.add("login.do?logout");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
	    info.setStringPermissions(permissions);
		return info;
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

//	@PostConstruct
//	public void initCredentialsMatcher() {//MD5加密
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ALGORITHM);
//		setCredentialsMatcher(matcher);
//	}

    @Override
    public boolean hasRole(PrincipalCollection principal, String roleIdentifier) {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        return hasRole(roleIdentifier, info);
    }
}
