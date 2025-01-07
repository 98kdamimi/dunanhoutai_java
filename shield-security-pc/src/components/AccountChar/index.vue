<template>
  <div>
    <el-row :gutter="120" style="display: flex; justify-content: center;">
        <el-col :span="4">
          <el-card shadow="always">
            <div class="col-item">
              <div class="col-num">
                {{dataInfo.countAll}}
              </div>
              <div style="margin-top: 15px;">总注册量</div> 
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card shadow="always">
            <div class="col-item">
              <div class="col-num">
                {{dataInfo.countToday}}
              </div>
              <div style="margin-top: 15px;">今日注册量</div> 
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card shadow="always">
            <div class="col-item">
              <div class="col-num">
                {{dataInfo.countWeek}}
              </div>
              <div style="margin-top: 15px;">近七天注册量</div> 
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card shadow="always">
            <div class="col-item">
              <div class="col-num">
                {{dataInfo.countMonth}}
              </div>
              <div style="margin-top: 15px;">本月注册量</div> 
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card shadow="always">
            <div class="col-item">
              <div class="col-num">
                {{dataInfo.countYear}}
              </div>
              <div style="margin-top: 15px;">本年注册量</div> 
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row style="margin-top: 50px;">
        <el-col :span="24" class="card-box">
          <el-card>
            <div >
              <el-button 
                :type="selectedButton === 'seven' ? 'primary' : 'info'" 
                @click="selectButton('seven')">
                近七天情况
              </el-button>
              <el-button 
                :type="selectedButton === 'month' ? 'primary' : 'info'" 
                @click="selectButton('month')">
                本月情况
              </el-button>
              <el-button 
                :type="selectedButton === 'year' ? 'primary' : 'info'" 
                @click="selectButton('year')">
                本年度情况
              </el-button>
              <el-date-picker
                v-model="monthData"
                format="yyyy 年 MM 月"
                value-format="yyyy-MM"
                type="monthrange"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份"
                style="margin-left: 15px;">
              </el-date-picker>
              <el-button 
                :type="selectedButton === 'but' ? 'primary' : 'info'" 
                @click="selectButton('but')"
                style="margin-left: 15px;">
                搜索
              </el-button>
            </div>
            <div class="el-table el-table--enable-row-hover el-table--medium">
              <div ref="equipment" style="height: 560px" />
            </div>
          </el-card>
        </el-col>
      </el-row>
  </div>
</template>
<script>
import { getAccount,getaAccountChar} from "@/api/statistics/statistics";
import * as echarts from "echarts";
export default {
  name: "Index",
  
  data() {
    return {
       activeName: 'second',
       tabIndex:0,
       dataInfo:{},
       queryParams:{},
       selectedButton: "seven",
       monthData:[],
    };
  },

  created() {
    this.getData()
    this.initChar()
  },

  methods: {
    getData(){
      getAccount().then(res =>{
        this.dataInfo = res.data
      })
    },

    initChar(){
      getaAccountChar(this.queryParams).then(res =>{
        this.equipment = echarts.init(this.$refs.equipment, "macarons");
        this.equipment.setOption({
          tooltip: {
            trigger: "axis", // 设置触发方式为轴触发
            formatter: "{a} <br/>{b} : {c}",
          },
          xAxis: {
            type: "category", // 设置为类目轴
            boundaryGap: false, // 去除边距
            data: res.data.xList, // 自定义横坐标数据
          },
          yAxis: {
            type: "value", // 设置为数值轴
            axisLabel: {
              formatter: '{value}' // 自定义y轴标签的格式
            }
          },
          series: [
            {
              name: "用户",
              type: "line", // 设置为折线图
              data: res.data.yList, // 自定义纵坐标数据
              smooth: true, // 平滑曲线
              animationEasing: "cubicInOut", // 动画效果
              animationDuration: 1000, // 动画持续时间
            }
          ]
        });
      })
    },

    // 更新选中的按钮
    selectButton(button) {
      this.selectedButton = button;
      this.queryParams = {}
      if(button == "seven"){//近七天
        const today = new Date();
        const startDate = new Date(today);
        startDate.setDate(today.getDate() - 7);
        const endDate = new Date(today);
        endDate.setHours(0, 0, 0, 0);
        startDate.setHours(0, 0, 0, 0);
        this.queryParams.begTime = this.formatDate(startDate);
        this.queryParams.endTime = this.formatDate(endDate)
        this.initChar()
      }else if(button == "month"){//本月
        const today = new Date();
        const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
        const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        this.queryParams.begTime = this.formatDate(startOfMonth);
        this.queryParams.endTime = this.formatDate(endOfMonth);
        this.initChar()
      }else if(button == "year"){//本年度
        const currentYear = new Date().getFullYear(); // 获取当前年份
        this.queryParams.begMonth = `${currentYear}-01`;
        this.queryParams.endMonth = `${currentYear}-12`;
        this.initChar()
      }else if(button == "but"){//范围搜索
        this.queryParams.begMonth = this.monthData[0]
        this.queryParams.endMonth = this.monthData[1]
        this.initChar()
      }
    },

    formatDate(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }




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

