<template>
  <div class="mainBox">
    <div class="app-container">
      <div class="conetntBox">
        <div class="flex_sb">
       
        </div>

        <el-table :data="dataList" max-height="600" v-loading="loading">
          <el-table-column label="序号" type="index" width="50" align="center" />

          <el-table-column label="标签名称" align="center" prop="lableName" class-name="small-padding fixed-width"/>
          <el-table-column label="展示状态" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <span v-if="scope.row.states">显示</span>
              <span v-else>隐藏</span>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="createdAt" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="text" style="color: red;" v-if="scope.row.states" @click="hideLable(scope.row.id,false)">隐藏标签</el-button>
              <el-button type="text" style="color: lawngreen;" v-else @click="showLable(scope.row.id,true)">显示标签</el-button>
            </template>
          </el-table-column>
        </el-table>
        
      </div>
    </div>
  </div>
</template>

<script>
import uploadImg from '@/components/uploadImg/uploadImg.vue'
import { tradingLableList,tradingLableUpdate} from "@/api/trading/trading";
export default {
  name: "typesOfPoints",
  components: {
      uploadImg
  },
  data() {
    return {
      dataList: null,
      loading :true,
      rules:{

      }
    };
  },
  watch: {
  },
  created() {
    this.getList();

  },
  methods: {

    getList() {
      this.loading = true;
      tradingLableList().then(res =>{
        this.dataList = res.data
        this.loading = false;
      })
      
    },
   
    
    //隐藏
    hideLable(id,states){
      this.$modal.confirm('是否确认隐藏标签？').then(function () {
        return tradingLableUpdate(id,states);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("隐藏成功");
      }).catch(() => { });
    },

    //下线
    showLable(id,states){
      this.$modal.confirm('是否确认显示标签？').then(function () {
        return tradingLableUpdate(id,states);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("显示成功");
      }).catch(() => { });
    } , 

  }
};
</script>
<style>
.mainBox {
  height: calc(100vh - 84px);
  overflow-y: auto;
  background-color: #efefef;

}

.flex_sb {
  display: flex;
  justify-content: space-between;
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
