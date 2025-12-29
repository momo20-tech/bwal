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
@TableName(value = "t_leave")
public class Leave {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 申请人ID（关联WorkUser表的id）
    private Integer applicantId;

    // 申请人姓名
    private String applicantName;

    // 请假类型：annual(年假)、sick(病假)、personal(事假)、other(其他)
    private String leaveType;

    // 开始时间
    private Date startTime;

    // 结束时间
    private Date endTime;

    // 请假天数
    private Integer days;

    // 请假原因
    private String reason;

    // 审批状态：0(待审批)、1(已批准)、2(已拒绝)
    private String approvalStatus;

    // 审批人ID
    private Integer approverId;

    // 审批人姓名
    private String approverName;

    // 审批意见
    private String approvalOpinion;

    // 申请时间
    private Date createTime;

    // 审批时间
    private Date updateTime;
}
