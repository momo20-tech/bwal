<template>
    <el-menu 
        :default-openeds="['1', '3']" 
        style="min-height:100%; overflow-x:hidden"
        background-color="rgb(48,65,86)"
        text-color="#ccc"
        active-text-color="red"
        router
    >
        <!-- 系统标题 -->
        <div style="height:60px; line-height:60px; text-align:center">
            <b style="color:white">霸王龙公司</b>
        </div>

        <!-- 主页菜单：所有用户可见 -->
        <el-menu-item index="/home">
            <template slot="title">
                <i class="el-icon-house"></i>
                <span slot="title">主页</span>
            </template>
        </el-menu-item>

        <!-- 系统管理子菜单：所有用户可见（但用户管理仅管理员可见） -->
        <el-submenu index="1">
            <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">系统管理</span>
            </template>
            <!-- 用户管理：仅管理员可见（添加v-if判断） -->
            <el-menu-item index="/user" v-if="isAdmin">
                <i class="el-icon-s-custom"></i>
                <span slot="title">用户管理</span>
            </el-menu-item>
        </el-submenu>

        <!-- 审批管理子菜单：仅管理员可见 -->
        <el-submenu index="2" v-if="isAdmin">
            <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">审批管理</span>
            </template>
            <el-menu-item index="/leave-approval">
                <i class="el-icon-s-custom"></i>
                <span slot="title">请假审批</span>
            </el-menu-item>
        </el-submenu>

        <!-- 普通用户的请假入口：仅普通用户可见 -->
        <el-menu-item index="/leave-approval" v-if="!isAdmin">
            <template slot="title">
                <i class="el-icon-document"></i>
                <span slot="title">请假</span>
            </template>
        </el-menu-item>
    </el-menu>
</template>

<script> 
export default {
    name: "Aside",
    data() {
        return {
            isAdmin: false
        }
    },
    created() {
        // 判断用户类型
        let userInfo = {};
        try {
            userInfo = JSON.parse(localStorage.getItem('user') || '{}');
        } catch (e) {
            console.error("用户信息解析失败：", e);
            userInfo = {};
        }
        this.isAdmin = Number(userInfo.isAdmin) === 1;
        console.log("当前用户是否为管理员：", this.isAdmin);
    }
}    
</script>