package com.example.backdorm.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.backdorm.common.Result;
import com.example.backdorm.entity.Leave;
import com.example.backdorm.entity.WorkUser;
import com.example.backdorm.service.LeaveServiceImpl;
import com.example.backdorm.service.WorkUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveServiceImpl leaveService;

    @Autowired
    private WorkUserServiceImpl workUserService; // 用于获取当前登录用户信息
    @GetMapping("/page")
    public Result findPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String approvalStatus,
            @RequestParam(required = false) Integer applicantId) {

        IPage<Leave> page = leaveService.findPage(pageNum, pageSize, approvalStatus, applicantId);
        return Result.success(page);
    }
    @PostMapping
    public Result save(@RequestBody Leave leave) {//新增请假申请
        boolean success = leaveService.saveLeave(leave);
        return success ? Result.success("操作成功") : Result.error("500","操作失败");
    }
    @GetMapping("/{id}")//根据 ID 查询请假申请详情
    public Result getById(@PathVariable Integer id) {
        Leave leave = leaveService.getLeaveById(id);
        return leave != null ? Result.success(leave) : Result.error("500","请假申请不存在");
    }
    @PutMapping("/approval/{id}")//请假申请审批（通过 / 拒绝）
    public Result approval(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> approvalInfo) {

        // 从前端传递的 approvalInfo 中获取审批人ID
        Integer currentUserId = (Integer) approvalInfo.get("approverId");
        WorkUser currentUser = workUserService.getById(currentUserId);

        if (currentUser == null) {
            return Result.error("500","用户未登录");
        }

        boolean success = leaveService.approvalLeave(id, approvalInfo, currentUser);
        return success ? Result.success("审批成功") : Result.error("500","审批失败");
    }
    @DeleteMapping("/{id}")//取消请假申请（删除 / 撤回）
    public Result cancel(@PathVariable Integer id) {
        boolean success = leaveService.cancelLeave(id);
        return success ? Result.success("取消成功") : Result.error("500","取消失败");
    }
    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Leave leave) {
        // 确保ID一致
        leave.setId(id);
        boolean success = leaveService.saveLeave(leave);
        return success ? Result.success("修改成功") : Result.error("500", "修改失败");
    }
}