package cn.ssms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ssms.model.User;
import cn.ssms.realm.ShiroDbRealm;
import cn.ssms.service.UserService;
import cn.ssms.util.CipherUtil;

@Controller
public class UserControler {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	@Autowired
	private UserService userService;

	/**
	 * 测试springmvc与mybatis整合成功
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/{id}/showUser")
	public String showUser(@PathVariable int id, HttpServletRequest request) {
		User user = userService.getUserById(id);
		System.out.println(user.getName());
		request.setAttribute("user", user);
		return "showUser";
	}

	/**
	 * 跳转至登录页
	 * @param request
	 * @return
	 */
	@RequestMapping("/tologin")
	public String tologin(HttpServletRequest request, HttpServletResponse response, Model model){
		logger.debug("来自IP[" + request.getRemoteHost() + "]的访问");
		return "login";
	}

	/**
	 * 登录示例
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		String result = "login";
		// 此处默认有值
		String username = request.getParameter("username");
		//MD5加密
		String password = CipherUtil.generatePassword(request.getParameter("password"));

		//String password = request.getParameter("password");
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		Subject currentUser = SecurityUtils.getSubject();
		try {
			System.out.println("----------------------------");
		     /*以下为测试代码： ERROR [cn.ssms.realm.ShiroDbRealm]*/
//	        Integer Int =1;
//	        UserRole userRole = userService.findUserRoleByUserID(Int);
//	        System.out.println("RoleId:------"+userRole.getRoleid());
//	        Role role = userService.findRoleByRoleID(i);
//	        System.out.println("roleName:------"+role.getName());
	        /*测试结束*/

			if (!currentUser.isAuthenticated()){
				token.setRememberMe(true);
				currentUser.login(token);
			}
			System.out.println("result: " + result);
			result = "index";
//			if(currentUser.hasRole("admin")){
//			    System.out.println("-------admin--------");
//			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = "login";
		}
//测试代码
//      Integer Int =1;
//    UserRole userRole = userService.findUserRoleByUserID(Int);
//    System.out.println("RoleId:------"+userRole.getRoleid());
		return result;
	}

    /**
     * 登出
     * @return
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public String logout() {

        Subject currentUser = SecurityUtils.getSubject();
        String result = "logout";
        currentUser.logout();
        return result;
    }

    /**
     * 检查
     * @return
     */
    @RequestMapping(value = "/chklogin", method = RequestMethod.POST)
    @ResponseBody
    public String chkLogin() {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            return "false";
        }
        return "true";
    }
}
