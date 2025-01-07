<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 2%;">
      <el-tab-pane label="用户统计" name="second">
      </el-tab-pane>
      <el-tab-pane label="账户统计" name="first">
      </el-tab-pane>
      <el-tab-pane label="应用监控" name="third">
        是大多数都是
      </el-tab-pane>
    </el-tabs>

    <div v-if="tabIndex == 0">
      <EquipmentChar ref="equipmentChar" />
    </div>
    <div v-if="tabIndex == 1">
      <AccountChar ref="accountChar" />
    </div>
    <div v-if="tabIndex == 2">
      <iframe
        src="http://localhost:3000/d/sbapmwalker/9daa324?orgId=1&kiosk&from=2024-11-22T02:03:32.785Z&to=2024-11-22T08:03:32.785Z&var-application=shieldSecurity&var-instance=$__all&var-hikaricp=&var-memory_pool_heap=$__all&var-memory_pool_nonheap=$__all&theme=light"
        width="100%"
        height="800"
        frameborder="0"
      ></iframe>
    </div>
  </div>
</template>



<script>
import EquipmentChar from "@/components/EquipmentChar";
import AccountChar from "@/components/AccountChar";
export default {
  name: "Index",
  components: { 
    EquipmentChar,
    AccountChar
  },
  data() {
    return {
       activeName: 'second',
       tabIndex:0,
      
    };
  },

  created() {
  },

  methods: {

    handleClick(tab, event) {
      this.tabIndex = tab.index
    },


  }
};
</script>

<style scoped lang="scss">
.home {
  height: 100%;

 }

 .col-item {
    margin-bottom: 20px;
    text-align: center; 
    display: flex; 
    flex-direction: column; 
    justify-content: center; 
    height: 100%;
  }
  .col-num{
    font-size: 24px; 
    font-weight: bold;
  }
</style>

