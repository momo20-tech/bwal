package com.example.backdorm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backdorm.common.Constants;
import com.example.backdorm.entity.Leave;
import com.example.backdorm.entity.WorkUser;
import com.example.backdorm.exception.ServiceException;
import com.example.backdorm.mapper.LeaveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;

@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> {
    private static final Logger log = LoggerFactory.getLogger(LeaveServiceImpl.class);
    //请假申请分页 + 条件查询
    public IPage<Leave> findPage(Integer pageNum, Integer pageSize, String approvalStatus, Integer applicantId) {
        Page<Leave> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        // 筛选条件：审批状态
        if (approvalStatus != null && !approvalStatus.isEmpty()) {
            queryWrapper.eq("approval_status", approvalStatus);
        }
        // 筛选条件：申请人ID（普通用户只能看自己的申请）
        if (applicantId != null) {
            queryWrapper.eq("applicant_id", applicantId);
        }
        // 按创建时间倒序（最新的在前面）
        queryWrapper.orderByDesc("create_time");
        try {
            return baseMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("查询请假列表失败", e);
            throw new ServiceException(Constants.CODE_500, "获取请假列表失败，请重试");
        }
    }
    public boolean saveLeave(Leave leave) {//新增 / 修改请假申请
        try {
            if (leave.getId() == null) {
                // 新增申请：默认待审批状态，设置创建时间
                leave.setApprovalStatus("0");
                leave.setCreateTime(new Date());
                leave.setUpdateTime(new Date());
            } else {
                // 编辑申请：只更新修改时间
                leave.setUpdateTime(new Date());
            }
            return saveOrUpdate(leave);
        } catch (Exception e) {
            log.error("保存请假申请失败", e);
            throw new ServiceException(Constants.CODE_500, "保存失败，请重试");
        }
    }
    //请假申请审批（通过 / 拒绝）
    public boolean approvalLeave(Integer id, Map<String, Object> approvalInfo, WorkUser currentUser) {
        try {
            // 1. 校验申请是否存在
            Leave leave = getById(id);
            if (leave == null) {
                throw new ServiceException(Constants.CODE_600, "请假申请不存在");
            }
            // 2. 校验是否已审批（只能审批待审批状态）
            if (!"0".equals(leave.getApprovalStatus())) {
                throw new ServiceException(Constants.CODE_600, "该申请已完成审批，无法重复操作");
            }
            // 3. 更新审批信息
            leave.setApprovalStatus((String) approvalInfo.get("approvalStatus")); // 1-批准，2-拒绝
            leave.setApproverId(currentUser.getId());
            leave.setApproverName(currentUser.getNickname() != null ? currentUser.getNickname() : currentUser.getUsername());
            leave.setApprovalOpinion((String) approvalInfo.get("approvalOpinion"));
            leave.setUpdateTime(new Date()); // 记录审批时间
            return updateById(leave);
        } catch (ServiceException e) {
            // 已知业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("审批请假失败", e);
            throw new ServiceException(Constants.CODE_500, "审批失败，请重试");
        }
    }
    public boolean cancelLeave(Integer id) {//取消请假申请
        try {
            Leave leave = getById(id);
            if (leave == null) {
                throw new ServiceException(Constants.CODE_600, "请假申请不存在");
            }
            // 只有待审批状态可以取消
            if (!"0".equals(leave.getApprovalStatus())) {
                throw new ServiceException(Constants.CODE_600, "仅待审批的申请可取消");
            }
            return removeById(id);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("取消请假失败", e);
            throw new ServiceException(Constants.CODE_500, "取消失败，请重试");
        }
    }

    public Leave getLeaveById(Integer id) {//查询请假申请详情
        try {
            return getById(id);
        } catch (Exception e) {
            log.error("查询请假详情失败", e);
            throw new ServiceException(Constants.CODE_500, "获取详情失败，请重试");
        }
    }
}