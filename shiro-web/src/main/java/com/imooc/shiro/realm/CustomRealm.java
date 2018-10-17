package com.imooc.shiro.realm;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.*;

public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //从数据库或缓存中获取角色数据
        Set<String> roles = getRolesByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String userName) {
        Set<String> permissions = new HashSet<String>();
        permissions.add("user:delete");
        permissions.add("user:add");
        return permissions;
    }

    private Set<String> getRolesByUserName(String userName) {
        List<String> list = userDao.getRolesByUserName(userName);
        Set<String> sets = new HashSet<String>(list);
        return  sets;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体传入的认证信息中获取用户名
        String userName = (String) authenticationToken.getPrincipal();

        //通过用户名到数据库或缓存中获取凭证
        String password = getPasswordByUserName(userName);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password,"customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));
        return authenticationInfo;
    }

    /**
     * 模拟数据库获取用户凭证
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {
        User user = userDao.getUserByUserName(userName);
        if(user != null){
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("1234567","Mark");
        System.out.println(md5Hash.toString());
    }
}
