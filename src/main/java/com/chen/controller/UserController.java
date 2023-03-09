package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.common.BaseContext;
import com.chen.common.Result;
import com.chen.domain.User;
import com.chen.service.UserService;
import com.chen.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * description:
 * className:UserController
 * author: chenqifan
 * date:2023/3/611:03
 **/

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code：" + code);
//            SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            session.setAttribute("phone", code);
            return Result.success("短信发送成功");
        }
        return Result.error("短信发送失败");
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取手机号和验证码
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");

        //从session中获取验证码并进行比对
        String codeInSession = (String) session.getAttribute("phone");
        if (codeInSession != null && codeInSession.equals(code)) {
            //登录成功
            //判断手机号对应的用户是否为新用户，是就自动完成注册
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return Result.success(user);
        }

        return Result.error("登录失败");
    }
}
