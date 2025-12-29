<template>
  <div>
    <!-- 头部下拉菜单 -->
    <el-dropdown style="width: 100px; cursor: pointer;">
      <span class="el-dropdown-link">
        {{ name }} <!-- 父组件传递的用户名（如“管理员”） -->
        <i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="showUserProfile">个人信息</el-dropdown-item>
        <!-- 给“退出登录”绑定点击事件 -->
        <el-dropdown-item @click.native="handleLogout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <!-- 个人信息弹窗（保持不变） -->
    <el-dialog title="个人信息" :visible.sync="dialogFormVisible" width="28%" center> 
      <el-form :model="form" label-width="80px" size="small">
        <el-form-item label="id">
          <el-input v-model="form.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" autocomplete="off" type="password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUserInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Header",//设置头部组件
  props: {
    name: String
  },//设置用户名
  data() {
    return {
      dialogFormVisible: false,
      form: {}
    };
  },//设置表单隐藏
  methods: {// 个人信息弹窗相关方法（保持不变）
    showUserProfile() {// 打开个人信息弹窗
      this.dialogFormVisible = true;
      this.getUserDetail();
    },
    getUserDetail() {
      const loginUser = JSON.parse(localStorage.getItem("user"));// 从本地存储读取登录用户信息（登录时存入的user对象）
      if (!loginUser || !loginUser.username) {
        this.$message.error("未获取到登录信息");
        return;
      }
      this.request.get("http://localhost:8098/user/page", {
        params: {
          pageNum: 1,
          pageSize: 1,
          username: loginUser.username
        }//调用后端接口，根据用户名查询用户详情
      }).then(res => {
        if (res.records && res.records.length > 0) {
          this.form = res.records[0];
        } else {
          this.$message.error("未查询到用户信息");
        }
      }).catch(() => {
        this.$message.error("获取个人信息失败");
      });
    },
    saveUserInfo() { // 调用后端新增/更新接口，提交表单数据
      this.request.post("http://localhost:8098/user", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功");
          this.dialogFormVisible = false;
        } else {
          this.$message.error("保存失败");
        }
      });
    },

    // 退出登录
    handleLogout() {
      localStorage.removeItem("user");
      this.$router.push("/login");
      this.$message.success("退出登录成功");
    }
  }
};
</script>