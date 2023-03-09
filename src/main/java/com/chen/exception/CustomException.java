package com.chen.exception;

/**
 * description:
 * className:CustomEexception
 * author: chenqifan
 * date:2023/3/717:34
 **/

/**
 * 自定义业务异常类
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
