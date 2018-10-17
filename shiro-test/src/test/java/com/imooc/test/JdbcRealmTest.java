package com.imooc.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import javax.sql.DataSource;

public class JdbcRealmTest {

    DruidDataSource dataSource = new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("");
    }
    @Test
    public void testAuthentication() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        //校验权限需配置该参数
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql = "select user_password from test_users where user_name = ?";
        jdbcRealm.setAuthenticationQuery(sql);
        String roleSql = "select user_roles from test_user_roles where user_name = ?";
        jdbcRealm.setUserRolesQuery(roleSql);
        String perSql = "select roles_permissions from test_user_permission where user_roles = ?";
        jdbcRealm.setPermissionsQuery(perSql);

        //1,构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2,主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming", "123456");
        subject.login(token);

        System.out.println("isAuthenticated : " + subject.isAuthenticated());
        subject.checkRole("admin");

        subject.checkPermission("user:delete");

    }
}
