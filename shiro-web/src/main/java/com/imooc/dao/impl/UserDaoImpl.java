package com.imooc.dao.impl;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;
    public User getUserByUserName(String userName) {
        String sql = "select user_name,user_password from test_users where user_name = ?";
        List<User> list = jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<User>() {
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUsername(resultSet.getString("user_name"));
                user.setPassword(resultSet.getString("user_password"));
                return user;
            }
        });
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    public List<String> getRolesByUserName(String userName) {
        String sql = "select user_roles from test_user_roles where user_name = ?";
        List<String> list = jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<String>() {
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("user_roles");
            }
        });
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list;
    }
}
