package com.example.backdorm.controller;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backdorm.common.Constants;
import com.example.backdorm.common.Result;
import com.example.backdorm.controller.dto.userDTO;
import com.example.backdorm.entity.WorkUser;
import com.example.backdorm.service.WorkUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController//所有方法返回值自动转为 JSON 格式
@RequestMapping("/user")//定义该控制器的基础请求路径
public class WorkUserController {
    @Autowired//自动注入 WorkUserServiceImpl 实例
    WorkUserServiceImpl workUserService;

    @GetMapping("/selectAll")//查询所有用户
    public List<WorkUser> selectAll() {
        return workUserService.list();
    }
    @PostMapping//新增 / 修改用户
    public Boolean save(@RequestBody WorkUser user) {
        return workUserService.addUserService(user);
    }
    @DeleteMapping("/{id}")//根据 ID 删除单个用户
    public Boolean deleteByIdUser(@PathVariable Integer id) {
       return workUserService.removeById(id);
    }
    @GetMapping("/{id}")
    public WorkUser insertByIdUser(@PathVariable Integer id) {
        return workUserService.getById(id);
    }
    @GetMapping("/page")
    public IPage<WorkUser> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String email,
                                @RequestParam(defaultValue = "") String address){
        IPage<WorkUser> page=new Page<>(pageNum,pageSize);
        QueryWrapper<WorkUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("username",username);
        queryWrapper.like("email",email);
        queryWrapper.like("address",address);
        return workUserService.page(page,queryWrapper);
    }
    @PostMapping("/del/batch/")//批量删除用户
    public Boolean deleteBatch(@RequestBody List<Integer> ids){
        return workUserService.removeByIds(ids);
    }
    @PostMapping("/login")
    public Result login(@RequestBody userDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        // 移除硬编码的"管理员"判断，所有用户统一验证
        userDTO dto = workUserService.login(userDTO);
        return Result.success(dto);
    }
    @GetMapping("/count")
    public Result getUserCount() {
        long count = workUserService.count();
        return Result.success(count);
    }
    @GetMapping("/checkId")//新增用户时候校验用户 ID 是否可用
    public Result checkIdExists(@RequestParam Integer id) {
        if (id == null) {
            return Result.error(Constants.CODE_400, "ID不能为空");
        }
        boolean exists = workUserService.existsById(id);
        return Result.success(!exists); // 返回true表示ID不存在（可用）
    }

}