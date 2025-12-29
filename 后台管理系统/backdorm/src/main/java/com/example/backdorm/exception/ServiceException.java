package com.example.backdorm.exception;
import lombok.Getter;
//自定义异常，前端可通过 code 精准区分错误类型
@Getter
public class ServiceException extends RuntimeException{
    private String code;
    public  ServiceException(String code,String msg){
        super(msg);
        this.code=code;
    }
}