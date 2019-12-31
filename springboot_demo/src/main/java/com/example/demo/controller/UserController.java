package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.annotation.PassToken;
import com.example.demo.common.core.CommonRequestData;
import com.example.demo.common.core.RestCommonResult;
import com.example.demo.common.shiro.JWTUtil;
import com.example.demo.dataobject.UserDO;
import com.example.demo.form.UserForm;
import com.example.demo.jpa.UserJpaDAO;
import com.example.demo.manager.UserManager;
import com.example.demo.service.TokenService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * @author SanKai
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/user/api")
public class UserController {

//    private static final Logger LOGGER = (Logger) LogManager.getLogger(UserController.class);
    

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserJpaDAO userJpaDAO;

//    @Autowired
//    private TokenService tokenService;

    @PostMapping("/add")
    @PassToken
    @ApiOperation(value = "添加用户", notes = "添加用户接口")
    public RestCommonResult<Boolean> createUser(@RequestBody @Valid UserForm form) {
        return new RestCommonResult<>(userManager.createUser(form));
    }

    @GetMapping("/findByName")
    @ApiOperation(value = "按用户名查找用户", notes = "查找用户")
    public RestCommonResult<UserDO> findById(@RequestParam @Valid String name) {
        return new RestCommonResult<>(userManager.findByName(name));
    }

    @GetMapping("/login")
    @PassToken
    @ApiOperation(value = "登录", notes = "登录")
    public RestCommonResult login(@RequestParam String username, @RequestParam String password) {
        
        JSONObject jsonObject = new JSONObject();
        UserDO userDO = userJpaDAO.findByUsernameAndPassword(username,password);
        if (userDO.getPassword().equals(password)){
            return new RestCommonResult(JWTUtil.sign(userDO.getUsername(),userDO.getPassword()));
        }
        
//        String token = tokenService.getToken(userDO);
//        jsonObject.put("token", token);

        return new RestCommonResult(userManager.login(username, password));
    }

    @GetMapping("/hasLogin")
    public RestCommonResult<String> hasLogin(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            System.out.println("已经验证过了");
            return new RestCommonResult<>(RestCommonResult.DEFAULT_SUCCESS_CODE);
        }
        System.out.println("没有进行验证");
        return new RestCommonResult<>( RestCommonResult.DEFAULT_ERROR_CODE);
    }

}
