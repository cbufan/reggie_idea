package com.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.mapper.UserMapper;
import com.chen.service.UserService;

import org.springframework.stereotype.Service;
import com.chen.domain.User;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

}
