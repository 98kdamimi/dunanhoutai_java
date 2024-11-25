<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 2%;">
      <el-tab-pane label="应用监控" name="second">
      </el-tab-pane>
      <el-tab-pane label="mysql监控" name="first">
      </el-tab-pane>
      <el-tab-pane label="mongodb监控" name="third">
        是大多数都是
      </el-tab-pane>
    </el-tabs>

    <div v-if="tabIndex == 0">
      <iframe
        src="http://localhost:3000/d/sbapmwalker/9daa324?orgId=1&kiosk&from=2024-11-22T02:03:32.785Z&to=2024-11-22T08:03:32.785Z&var-application=shieldSecurity&var-instance=$__all&var-hikaricp=&var-memory_pool_heap=$__all&var-memory_pool_nonheap=$__all&theme=light"
        width="100%"
        height="800"
        frameborder="0"
      ></iframe>
    </div>
    <div v-if="tabIndex == 1">
      <iframe
        src="http://localhost:3000/d/549c2bf8936f7767ea6ac47c47b00f2a/mysql-exporter-quickstart-and-dashboard?orgId=1&kiosk&from=2024-11-22T09:11:59.051Z&to=2024-11-22T10:11:59.051Z&var-datasource=fe4ocukq2l6v4a&var-job=$__all&var-instance=$__all&refresh=10s&theme=light"
        width="100%"
        height="800"
        frameborder="0"
      ></iframe>
    </div>
    <!-- <div v-if="tabIndex == 2">
      <iframe
        src="http://localhost:3000/d/549c2bf8936f7767ea6ac47c47b00f2a/mysql-exporter-quickstart-and-dashboard?orgId=1&kiosk&from=2024-11-22T09:11:59.051Z&to=2024-11-22T10:11:59.051Z&var-datasource=fe4ocukq2l6v4a&var-job=$__all&var-instance=$__all&refresh=10s&theme=light"
        width="100%"
        height="800"
        frameborder="0"
      ></iframe>
    </div> -->
  </div>
</template>

<script>

export default {
  name: "Index",
  
  data() {
    return {
      // 版本号
       activeName: 'second',
       tabIndex:0,
    };
  },
  methods: {
    handleClick(tab, event) {
      this.tabIndex = tab.index
      console.log(tab.index);
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  height: 100%;

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

 }
</style>

