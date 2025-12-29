package com.example.backdorm.service;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backdorm.common.Constants;
import com.example.backdorm.controller.dto.userDTO;
import com.example.backdorm.entity.WorkUser;
import com.example.backdorm.exception.ServiceException;
import com.example.backdorm.mapper.WorkUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service//标记为 Spring 服务组件
public class WorkUserServiceImpl extends ServiceImpl<WorkUserMapper, WorkUser> {
    private static final Logger log = LoggerFactory.getLogger(WorkUserServiceImpl.class);
    //校验用户 ID 是否存在
    public boolean existsById(Integer id) {
        QueryWrapper<WorkUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id); // 根据主键ID查询
        // 若查询结果计数 > 0，则表示存在
        return baseMapper.selectCount(queryWrapper) > 0;
    }
    public Boolean addUserService(WorkUser workuser) {//新增 / 修改用户
        return saveOrUpdate(workuser);
    }
    public userDTO login(userDTO userDTO) {//登录
        QueryWrapper<WorkUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        WorkUser one;
        try {
            one = getOne(queryWrapper); // 查询数据库用户
        } catch (Exception e) {
            log.error("登录查询异常", e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one != null) {
            // 手动赋值关键字段（避免拷贝失败）
            userDTO.setId(one.getId()); // 关键：传递id
            userDTO.setUsername(one.getUsername());
            userDTO.setIsAdmin(one.getIsAdmin()); // 传递管理员标识
            // 不需要返回密码（安全起见）
            userDTO.setPassword(null);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }
}