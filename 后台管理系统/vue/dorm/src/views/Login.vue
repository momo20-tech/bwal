<template>
  <div class="login_container">
    <div class="login-bg"></div>    
    <div class="system-title">
      <h1>霸王龙公司</h1>
    </div>
    <div class="login_box">
      <div style="margin:30px 0; text-align:center; font-size:32px"><b>欢迎加入</b></div>
      <el-form ref="LoginFormRef" :model="loginForm" :rules="LoginFormRules">
        <el-form-item prop="username">
          <!-- 输入框宽度加大，字号放大 -->
          <el-input size="large" style="margin:15px 0px;width: 380px;margin-left:40px" v-model="loginForm.username" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" style="margin:15px 0px;width: 380px;margin-left:40px" show-password v-model="loginForm.password" prefix-icon="el-icon-lock" type="password"></el-input>
        </el-form-item>
        <div style="margin:20px 0; text-align:center">
          <!-- 登录按钮放大 -->
          <el-button type="primary" size="medium" @click="login" style="width: 120px; height: 40px; font-size: 16px;">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      LoginFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  methods: {
    login() {
  this.$refs['LoginFormRef'].validate(async (valid) => {
    if (valid) {
      this.request.post("http://localhost:8098/user/login", this.loginForm)
        .then(res => {
          console.log("后端返回结果：", res); // 打印完整返回信息
          if (res.code === '200') {
            localStorage.removeItem('user'); // 清除旧数据
            localStorage.setItem("user", JSON.stringify(res.data));
            this.$router.push("/home");
            this.$message.success("登录成功");
          } else {
            // 显示后端返回的具体错误（如密码错误、用户不存在）
            this.$message.error("登录失败：" + res.msg);
          }
        })
        .catch(error => {
          // 捕获网络错误或后端500错误
          console.error("登录请求异常：", error);
          this.$message.error("服务器连接失败，请检查后端是否启动");
        });
    }
  });
},
    resetLoginForm() { 
      this.$refs['LoginFormRef'].resetFields();
    },
    // 新增：跳转到注册页面
    toRegister() {
      this.$router.push("/register"); // 假设注册页面路由为/register
    }
  }
};
</script>

<style scoped>
.login_container {
  position: relative; 
  width: 100vw; 
  height: 100vh; 
  overflow: hidden; 
}

.login-bg {
  position: absolute; 
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/login-bg.png'); 
  background-size: cover; 
  background-position: center; 
  opacity: 0.6;
  z-index: 1;
}

.system-title, .login_box {
  position: relative; 
  z-index: 2;
}

/* 登录框整体放大 + 向下移动 */
.login_box {
  width: 460px;    /* 原350px */
  height: 400px;   /* 原300px */
  background-color: rgba(175, 179, 179, 0.3); 
  border-radius: 8px; /* 圆角加大更美观 */
  position: absolute;
  left: 50%;
  top: 60%; /* 原50%，调大数值让登录框向下移 */
  transform: translate(-50%, -60%); /* 对应top值调整，保持水平居中 */
  box-shadow: 0 4px 20px rgba(0,0,0,0.1); /* 增加阴影更立体 */
}

/* 公司名称样式放大 + 向下移动 */
.system-title h1 {
  color: #5c7ebe;
  text-align: center;
  margin-top: 200px; /* 原120px，调大数值让公司名称向下移 */
  font-family: "KaiTi", "楷体", "STKaiti", serif;
  font-size: 60px;   /* 原45px */
  letter-spacing: 5px; /* 字间距加大 */
  font-weight: bold; /* 加粗更醒目 */
}
</style>