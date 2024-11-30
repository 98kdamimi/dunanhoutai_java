<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div style="display: flex; justify-content: flex-end;">
          <el-button type="primary" icon="el-icon-refresh-left" @click="getToken">更新Token</el-button>
        </div>
        <el-table :data="dataList" max-height="600">
          <el-table-column label="序号" type="index" width="50" align="center" />
          <el-table-column label="token信息" align="center" key="token" prop="token"/>
          <el-table-column width=120>
            <template slot-scope="scope">
              <el-button size="mini" icon="el-icon-document-checked" type="primary"  @click = "copyToken(scope.row.token)">复制 Token</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { generateToken} from "@/api/system/user";
export default {
  name: "typesOfPoints",
  data() {
    return {
      // 用户表格数据
      dataList: null,
      dialogOpen: false,
    };
  },
  watch: {
  },
  created() {
    this.getToken();
  },
  methods: {
    //列表
    getToken() {
      generateToken(this.queryParams).then(res =>{
        this.dataList = res.data
      })
    },

    //复制token
    copyToken(token){
      try {
        // 使用 Clipboard API 进行复制
        navigator.clipboard.writeText(token);
        this.$modal.msgSuccess("复制成功");
      } catch (err) {
        this.$modal.msgError("复制失败");
      }
    }

  }
};
</script>
<style>
.mainBox {
  height: calc(100vh - 84px);
  overflow-y: auto;
  background-color: #efefef;

}

.app-container {
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

.conetntBox {
  background-color: #fff;
  box-sizing: border-box;
  padding: 20px;
}
</style>
