<template>
  <div>
      <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="28%">
          <el-form :model="form" label-width="80px" size="small">
            <el-form-item label="员工号" >
              <el-input 
                v-model="form.id" 
                autocomplete="off" 
                @blur="checkIdExists"  
              ></el-input>
            </el-form-item>
            <el-form-item label="用户名" >
              <el-input v-model="form.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称" >
              <el-input v-model="form.nickname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" >
              <el-input v-model="form.password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" >
              <el-input v-model="form.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话号码" >
              <el-input v-model="form.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="地址" >
              <el-input v-model="form.address" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="insert">确 定</el-button>
          </div>
        </el-dialog>


        <div style="padding: 10px; text-align: center;">
          <el-input style="width: 250px " suffix-icon="el-icon-search" placeholder="请输入名称搜索" v-model="username"></el-input>
          <el-input style="width: 250px " suffix-icon="el-icon-search" placeholder="请输入邮箱搜索" v-model="email"></el-input>
          <el-input style="width: 250px " suffix-icon="el-icon-search" placeholder="请输入地址搜索" v-model="address"></el-input>
          <el-button style="margin-left: 5px;" type="primary" @click="load()">搜索</el-button>
      </div>



      <div style="margin: 10px; text-align: left;">
        <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus"></i></el-button>
        <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这段内容确定删除吗？"
              @confirm="delBatch"
          >
            <el-button slot="reference" type="danger" icon="el-icon-remove"  style="margin: 10px;">批量删除</el-button>
          </el-popconfirm>         
      </div>



      <el-table
  :data="tableData"
  @selection-change="handleSelectionChange">
  <el-table-column
  type="selection"
  width="55">
</el-table-column>
  <el-table-column
    prop="id"
    label="员工号"
    width="80">
  </el-table-column>
  <el-table-column
    prop="username"
    label="姓名"
    width="80">
  </el-table-column>
  <el-table-column
    prop="email"
    label="邮箱"
    width="120">
  </el-table-column>
  <el-table-column
    prop="phone"
    label="电话"
    width="120">
  </el-table-column>
  <el-table-column
    prop="nickname"
    label="昵称"
    width="80">
  </el-table-column>
  <el-table-column
    prop="address"
    label="地址">
  </el-table-column>


      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" size="small">修改</el-button>
         
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这段内容确定删除吗？"
              @confirm="handleDelete(scope.row.id)"
          >
            <el-button slot="reference" type="danger" icon="el-icon-delete" size="small" style="margin: 10px;">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>


</el-table>
<div class="block" style="padding: 10px; text-align: left;">
  <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="pageNum"
    :page-sizes="[5, 10, 15, 20]"
    :page-size="pageSize"
    layout="total, sizes, prev, pager, next, jumper"
    :total="total">
  </el-pagination>
</div> 
  </div>
</template>
<script>
export default {
  name: 'User',
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      username: "",
      email:"",
      address:"",
      dialogFormVisible: false,
      form: {},
      multipleSelection: [],
      idValid: false  // 标记ID是否有效（不存在）
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
           this.request.get("http://localhost:8098/user/page",{
            params:{
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              username: this.username,
              email:this.email,
              address:this.address
            }
          }).then(res=>{
          console.log(res)
          this.tableData=res.records
          this.total=res.total
          })
        },
    handleSizeChange(size) {
      this.pageSize = size;
      this.pageNum = 1; 
      this.load();
    },
    handleCurrentChange(currentPage) {
      this.pageNum = currentPage;
      this.load();
    },

    handleAdd(){
        this.dialogFormVisible = true;
        this.form={};
        this.idValid = false;  // 重置校验状态
      },

      handleEdit(row){
        this.form=JSON.parse(JSON.stringify(row));  // 深拷贝避免修改表格数据
        this.dialogFormVisible=true;
        this.idValid = true;  // 编辑时ID已存在，无需校验
      },

      handleDelete(id){
        this.request.delete(`http://localhost:8098/user/${id}`).then(res=>{
          if(res){
                this.$message.success("删除成功");
                this.dialogFormVisible=false;
                this.load();
          }else{
            this.$message.error("删除失败");
          } 
        })
      },

      delBatch(){ 
         let ids=this.multipleSelection.map(v=>v.id);
         console.log(ids);
         this.request.post("http://localhost:8098/user/del/batch/",ids).then(res=>{
         if(res){
            this.$message.success("批量删除成功");
            this.load();
          }else{
            this.$message.error("批量删除失败");
          } 
        })
      },
      handleSelectionChange(val){
        this.multipleSelection =val;
      },

      // 新增：检查ID是否存在
      checkIdExists() {
        if (!this.form.id) {
          this.idValid = false;
          return;
        }
        this.request.get("http://localhost:8098/user/checkId", {
          params: { id: this.form.id }
        }).then(res => {
          if (res.code === '200') {
            if (res.data) {  // 后端返回true表示ID不存在（可用）
              this.$message.success("员工ID可用");
              this.idValid = true;
            } else {
              this.$message.error("该员工ID已存在，请更换");
              this.idValid = false;
            }
          } else {
            this.$message.error(res.msg || "ID校验失败");
            this.idValid = false;
          }
        }).catch(err => {
          this.$message.error("网络错误，校验失败");
          this.idValid = false;
          console.error(err);
        });
      },

      // 修改：保存方法添加ID校验
      insert() {
        if (!this.form.id) {
          this.$message.warning("请输入员工ID");
          return;
        }
        // 未校验或校验不通过时强制校验
        if (!this.idValid) {
          this.checkIdExists().then(() => {
            if (this.idValid) this.doInsert();
          });
          return;
        }
        this.doInsert();
      },

      // 实际执行保存的方法
      doInsert() {
        this.request.post("http://localhost:8098/user", this.form).then(res => {
          if (res) {
            this.$message.success("保存成功");
            this.dialogFormVisible = false;
            this.load();
            this.idValid = false;  // 重置校验状态
          } else {
            this.$message.error("保存失败");
          }
        });
      }
  },
};
</script>