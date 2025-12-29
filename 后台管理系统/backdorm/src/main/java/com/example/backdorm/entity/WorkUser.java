package com.example.backdorm.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value="workuser")
public class WorkUser {
    @TableId(value = "id",type = IdType.AUTO)
    private  Integer id;
    private  String username;
    private  String password;
    private  String email;
    private  String phone;
    private  String nickname;
    private  String address;
    private Date createdtime;
    private Integer isAdmin;
}