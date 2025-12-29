<template>
  <div class="home-container">
    <!-- 轮播图区域 -->
    <el-carousel 
      height="400px"
      indicator-position="bottom"
      autoplay
      interval="3000"
      effect="fade"
      arrow="hover"
    >
      <el-carousel-item>
        <img src="@/assets/first.png" class="carousel-img">
      </el-carousel-item>
      <el-carousel-item>
        <img src="@/assets/two.png" class="carousel-img">
      </el-carousel-item>
      <el-carousel-item>
        <img src="@/assets/three.jpeg" class="carousel-img">
      </el-carousel-item>
    </el-carousel>

    <!-- 页面标题区域 -->
    <div class="page-header">
      <h1>欢迎来到霸王龙公司</h1>
    </div>

    <!-- “总员工数”卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-info">
          <div class="stat-value">{{ totalUsers.toLocaleString() }}</div>
          <div class="stat-label">总员工数</div>
        </div>
        <div class="stat-icon">
          <i class="icon-user"></i>
        </div>
      </div>
    </div>  
    <!-- 最近动态列表 -->
    <div class="recent-activities">
      <h2>最近动态</h2>
      <ul class="activity-list">
        <li class="activity-item">
          <span class="activity-time">今天 09:24</span>
          <span class="activity-content">管理员更新了张三的用户资料</span>
        </li>
        <li class="activity-item">
          <span class="activity-time">2025-10-23</span>
          <span class="activity-content">新增12位注册用户</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

// 创建axios实例并配置基础路径
const request = axios.create({
  baseURL: 'http://localhost:8098', // 后端服务地址
  timeout: 5000
})

// 请求拦截器（可选，处理请求前逻辑）
request.interceptors.request.use(config => {
  return config
}, error => {
  return Promise.reject(error)
})

// 响应拦截器（处理响应数据）
request.interceptors.response.use(response => {
  return response.data
}, error => {
  console.error('请求错误:', error)
  return Promise.reject(error)
})

export default {
  name: "Home",
  data() {
    return {
      totalUsers: 0, // 员工总数
      timer: null // 定时器实例
    }
  },
  mounted() {
    // 页面加载时获取数据
    this.fetchUserCount()
    
    // 设置定时刷新（每30秒一次）
    this.timer = setInterval(() => {
      this.fetchUserCount()
    }, 10000)
  },
  beforeDestroy() {
    // 组件销毁时清除定时器
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    // 获取员工总数
    async fetchUserCount() {
      try {
        const response = await request.get('/user/count')
        if (response.code === '200') {
          this.totalUsers = response.data
        } else {
          this.$message.error('获取数据失败: ' + response.msg)
        }
      } catch (error) {
        console.error('获取员工数量失败:', error)
        this.$message.error('网络错误，请稍后重试')
      }
    }
  }
}
</script>

<style scoped>
.el-carousel {
  width: 100%;
  margin-bottom: 2rem;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover; 
}

.home-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

/* 页面标题 */
.page-header {
  margin-bottom: 2rem;
  text-align: left;
}

.page-header h1 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-size: 1.8rem;
}

.header-desc {
  color: #666;
  margin: 0;
  font-size: 1rem;
}

/* 数据统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
  grid-template-columns: 350px; 
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 0.3rem;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: #f0f7ff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #42b983;
  font-size: 1.2rem;
}

.quick-actions {
  margin-bottom: 2rem;
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.action-btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 500;
}

.action-btn.primary {
  background: #42b983;
  color: white;
}

.action-btn.secondary {
  background: #f5f5f5;
  color: #2c3e50;
}

.action-btn.tertiary {
  background: transparent;
  color: #2c3e50;
  border: 1px solid #ddd;
}

/* 最近动态 */
.recent-activities {
  background: #fff;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.recent-activities h2 {
  margin: 0 0 1rem 0;
  font-size: 1.2rem;
  color: #2c3e50;
  text-align: left;
}

.activity-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.activity-item {
  padding: 0.8rem 0;
  border-bottom: 1px solid #f5f5f5;
  display: flex;
  justify-content: space-between;
  text-align: left;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  color: #999;
  font-size: 0.9rem;
  min-width: 100px;
}

.activity-content {
  color: #555;
}

/* 图标样式 */
.icon-user::before { content: "👤"; }
.icon-active::before { content: "📈"; }
.icon-refresh::before { content: "🔄"; }
.icon-download::before { content: "📥"; }
</style>