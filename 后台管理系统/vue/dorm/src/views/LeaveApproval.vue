<template>
  <div class="leave-container">
    <!-- 操作按钮/筛选区 -->
    <div style="margin: 10px 0; text-align: left;">
      <el-button 
        type="primary" 
        icon="el-icon-plus"
        @click="handleAddLeave"
        v-if="!isAdmin"
        size="medium"
      >新增请假申请</el-button>
      
      <el-select 
        v-model="approvalStatus" 
        placeholder="请选择审批状态" 
        style="margin-left: 15px; width: 180px"
        @change="loadLeaveList"
        v-if="isAdmin"
        size="medium"
      >
        <el-option label="全部" value=""></el-option>
        <el-option label="待审批" value="0"></el-option>
        <el-option label="已批准" value="1"></el-option>
        <el-option label="已拒绝" value="2"></el-option>
      </el-select>
    </div>

    <!-- 请假列表 -->
    <el-table 
      :data="leaveList" 
      border 
      style="width: 100%; margin-bottom: 20px;"
      :header-cell-style="{background: '#f5f7fa', color: '#303133'}"
      :cell-style="{padding: '12px 0'}"
    >
      <el-table-column prop="applicantName" label="申请人" width="110" align="center"></el-table-column>
      <el-table-column prop="leaveType" label="请假类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTagType(scope.row.leaveType, 'leave')" size="small" effect="soft">
            {{ scope.row.leaveType === 'annual' ? '年假' : scope.row.leaveType === 'sick' ? '病假' : scope.row.leaveType === 'personal' ? '事假' : '其他' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="180" align="center"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="180" align="center"></el-table-column>
      <el-table-column prop="days" label="请假天数" width="100" align="center"></el-table-column>
      <el-table-column prop="reason" label="请假原因" min-width="200" align="center"></el-table-column>
      <el-table-column prop="approvalStatus" label="审批状态" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTagType(scope.row.approvalStatus, 'approval')" size="small" effect="soft">
            {{ scope.row.approvalStatus === '0' ? '待审批' : scope.row.approvalStatus === '1' ? '已批准' : '已拒绝' }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 审批人列：仅管理员可见，且仅已审批记录显示 -->
      <el-table-column label="审批人" width="120" align="center" v-if="isAdmin">
        <template slot-scope="scope">
          <span v-if="scope.row.approvalStatus !== '0'">{{ scope.row.approverName || '无' }}</span>
        </template>
      </el-table-column>
      <!-- 操作列：精准权限控制 -->
      <el-table-column label="操作" width="220" align="center" fixed="right">
        <template slot-scope="scope">
          <!-- 普通用户：仅自己的待审批申请可撤销/编辑 -->
          <div v-if="!isAdmin && scope.row.approvalStatus === '0'">
            <el-button 
              type="danger" 
              size="small"
              icon="el-icon-delete"
              @click="cancelLeave(scope.row.id)"
              style="margin-right: 8px;"
            >撤销申请</el-button>
            <el-button 
              type="primary" 
              size="small"
              icon="el-icon-edit"
              @click="viewLeaveDetail(scope.row)"
            >编辑</el-button>
          </div>
          <!-- 管理员：仅待审批申请可批准/拒绝 -->
          <div v-if="isAdmin && scope.row.approvalStatus === '0'">
            <el-button 
              type="success" 
              size="small"
              icon="el-icon-check"
              @click="approveLeave(scope.row.id)"
              style="margin-right: 8px;"
            >批准</el-button>
            <el-button 
              type="warning" 
              size="small"
              icon="el-icon-close"
              @click="rejectLeave(scope.row.id)"
            >拒绝</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container" style="text-align: right;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
        size="medium"
      ></el-pagination>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      title="请假申请" 
      :visible.sync="dialogVisible" 
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form 
        :model="leaveForm" 
        :rules="leaveRules" 
        ref="leaveForm"
        label-width="100px"
        label-position="right"
      >
        <el-form-item label="请假类型" prop="leaveType">
          <el-radio-group v-model="leaveForm.leaveType">
            <el-radio label="annual">年假</el-radio>
            <el-radio label="sick">病假</el-radio>
            <el-radio label="personal">事假</el-radio>
            <el-radio label="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="leaveForm.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%;" size="medium"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="leaveForm.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%;" size="medium"></el-date-picker>
        </el-form-item>
        <el-form-item label="请假原因" prop="reason">
          <el-input v-model="leaveForm.reason" type="textarea" rows="4" placeholder="请输入请假原因（至少5个字符）" size="medium"></el-input>
        </el-form-item>
        <!-- 仅编辑已保存的申请时显示审批信息 -->
        <el-form-item v-if="leaveForm.id">
          <el-form-item label="审批结果">
            <el-tag :type="getTagType(leaveForm.approvalStatus, 'approval')" size="small" effect="soft">
              {{ leaveForm.approvalStatus === '0' ? '待审批' : leaveForm.approvalStatus === '1' ? '已批准' : '已拒绝' }}
            </el-tag>
          </el-form-item>
          <el-form-item label="审批意见" v-if="leaveForm.approvalStatus !== '0'">
            <el-input v-model="leaveForm.approvalOpinion" disabled size="medium" placeholder="无"></el-input>
          </el-form-item>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false" size="medium">取消</el-button>
        <el-button 
          type="primary" 
          @click="submitLeaveForm"
          v-if="!leaveForm.id || (leaveForm.id && leaveForm.approvalStatus === '0' && !isAdmin)"
          size="medium"
        >提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'LeaveApproval',
  data() {
    return {
      leaveList: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      approvalStatus: '',
      isAdmin: false,
      currentUser: null,
      dialogVisible: false,
      leaveForm: {
        id: '',
        leaveType: 'annual',
        startTime: '',
        endTime: '',
        reason: '',
        approvalStatus: '0',
        approvalOpinion: '',
        approverId: 1,
        approverName: '管理员'
      },
      leaveRules: {
        leaveType: [{ required: true, message: '请选择请假类型', trigger: 'change' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        reason: [
          { required: true, message: '请输入请假原因', trigger: 'blur' },
          { min: 5, message: '请假原因至少5个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    // 解析用户信息（精准权限判断）
    const userStr = localStorage.getItem('user');
    this.currentUser = userStr ? JSON.parse(userStr) : {};
    this.isAdmin = (this.currentUser.isAdmin + '') === '1';
    this.loadLeaveList();
  },
  methods: {
    // 加载请假列表（普通用户仅看自己的，管理员看全部）
    loadLeaveList() {
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        approvalStatus: this.approvalStatus
      }
      if (!this.isAdmin && this.currentUser.id) params.applicantId = this.currentUser.id;
      
      this.request.get("http://localhost:8098/leave/page", { params })
        .then(res => {
          const data = res.code === '200' || res.success ? (res.records || res.data?.records) : [];
          this.leaveList = data;
          this.total = res.total || res.data?.total || 0;
          if (!data.length && !(res.code === '200' || res.success)) {
            this.$message.error('获取请假列表失败: ' + (res.msg || '服务器错误'));
          }
        })
        .catch(err => {
          console.error(err);
          this.$message.error('获取数据失败，请稍后重试');
        });
    },
    // 分页切换（精简写法）
    handleSizeChange(size) { this.pageSize = size; this.pageNum = 1; this.loadLeaveList(); },
    handleCurrentChange(page) { this.pageNum = page; this.loadLeaveList(); },
    // 新增请假（仅普通用户）
    handleAddLeave() {
      this.leaveForm = {
        id: '',
        leaveType: 'annual',
        startTime: '',
        endTime: '',
        reason: '',
        approvalStatus: '0',
        approverId: 1,
        approverName: '管理员'
      };
      this.dialogVisible = true;
    },
    // 查看/编辑请假（仅普通用户的待审批申请）
    viewLeaveDetail(row) {
      this.leaveForm = { ...row };
      this.dialogVisible = true;
    },
    // 提交请假（新增/编辑）
    submitLeaveForm() {
      this.$refs.leaveForm.validate(valid => {
        if (!valid) return;
        if (!this.currentUser.id) return this.$message.error('未获取到用户信息，请重新登录');
        
        const start = new Date(this.leaveForm.startTime);
        const end = new Date(this.leaveForm.endTime);
        if (end <= start) return this.$message.error('结束时间不能早于开始时间');
        
        // 计算请假天数
        this.leaveForm.days = Math.ceil(Math.abs(end - start) / (1000 * 60 * 60 * 24));
        this.leaveForm.applicantId = this.currentUser.id;
        this.leaveForm.applicantName = this.currentUser.nickname || this.currentUser.username;
        
        const method = this.leaveForm.id ? 'put' : 'post';
        const url = this.leaveForm.id ? `http://localhost:8098/leave/${this.leaveForm.id}` : 'http://localhost:8098/leave';
        
        this.request[method](url, this.leaveForm)
          .then(res => {
            if (res.code === '200' || res.success) {
              this.$message.success(this.leaveForm.id ? '修改成功' : '申请提交成功');
              this.dialogVisible = false;
              this.loadLeaveList();
            } else {
              this.$message.error(res.msg || (this.leaveForm.id ? '修改失败' : '提交失败'));
            }
          })
          .catch(err => {
            console.error(err);
            this.$message.error('操作失败，请稍后重试');
          });
      });
    },
    // 撤销请假（仅普通用户的待审批申请）
    cancelLeave(id) {
      this.$confirm('确定要撤销该请假申请吗?', '提示', { type: 'warning' })
        .then(() => {
          this.request.delete(`http://localhost:8098/leave/${id}`)
            .then(res => {
              if (res.code === '200' || res.success) {
                this.$message.success('撤销成功');
                this.loadLeaveList();
              } else {
                this.$message.error(res.msg || '撤销失败');
              }
            })
            .catch(err => {
              console.error(err);
              this.$message.error('操作失败，请稍后重试');
            });
        })
        .catch(() => this.$message.info('已取消操作'));
    },
    // 批准请假（仅管理员）
    approveLeave(id) {
      this.$confirm('确定要批准该请假申请吗?', '提示', { type: 'success' })
        .then(() => this.updateApprovalStatus(id, '1', ''))
        .catch(() => this.$message.info('已取消审批'));
    },
    // 拒绝请假（仅管理员）
    rejectLeave(id) {
      this.$prompt('请输入拒绝原因', '拒绝请假', { inputPlaceholder: '请输入拒绝原因' })
        .then(({ value }) => this.updateApprovalStatus(id, '2', value))
        .catch(() => this.$message.info('已取消审批'));
    },
    // 更新审批状态（仅管理员）
    updateApprovalStatus(id, status, opinion) {
      if (!this.currentUser.id || !this.isAdmin) return;
      this.request.put(`http://localhost:8098/leave/approval/${id}`, {
        approvalStatus: status,
        approverId: this.currentUser.id,
        approverName: this.currentUser.nickname || this.currentUser.username,
        approvalOpinion: opinion
      })
        .then(res => {
          if (res.code === '200' || res.success) {
            this.$message.success(status === '1' ? '批准成功' : '拒绝成功');
            this.loadLeaveList();
          } else {
            this.$message.error(res.msg || '操作失败');
          }
        })
        .catch(err => {
          console.error(err);
          this.$message.error('操作失败，请稍后重试');
        });
    },
    // 统一获取标签类型（精简）
    getTagType(value, type) {
      const leaveMap = { annual: 'primary', sick: 'success', personal: 'warning', other: 'info' };
      const approvalMap = { 0: 'warning', 1: 'success', 2: 'danger' };
      return type === 'leave' ? leaveMap[value] : approvalMap[value] || '';
    }
  }
}
</script>

<style scoped>
.leave-container {
  padding: 20px;
  background: #f9f9f9;
  min-height: calc(100vh - 60px);
}

.pagination-container {
  margin-top: 15px;
}

/* 通用样式优化（精简） */
::v-deep .el-table {
  --el-table-row-hover-bg-color: #f8f9fa;
  border-radius: 4px;
  overflow: hidden;
}
::v-deep .el-table th { font-weight: 600; }
::v-deep .el-button--small {
  padding: 6px 12px;
  border-radius: 4px;
  transition: all 0.2s;
}
::v-deep .el-button--small:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
::v-deep .el-dialog__header {
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}
::v-deep .el-dialog__title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
::v-deep .el-form-item { margin-bottom: 20px; }
::v-deep .el-tag { border-radius: 4px; }
</style>