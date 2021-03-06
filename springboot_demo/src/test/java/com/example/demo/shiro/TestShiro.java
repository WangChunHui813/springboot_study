package com.example.demo.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.DemoApplication;
import org.apache.catalina.realm.JDBCRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestShiro {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
    IniRealm iniRealm = new IniRealm("classpath:user.ini");

    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("sankai", "123456");
    }

    @Test
    public void testAuthentication() {

        JDBCRealm jdbcRealm = new JDBCRealm();


        //1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //2、主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("sankai", "123456");
        subject.login(token);

        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        subject.checkRole("admin");
        subject.checkPermission("user:delete");
        subject.checkPermission("user:update");
//        subject.checkRole("admin");
//        subject.logout();
//        subject.checkRoles("admin","user");
//        System.out.println("isAuthenticated:" + subject.isAuthenticated());

    }


}
