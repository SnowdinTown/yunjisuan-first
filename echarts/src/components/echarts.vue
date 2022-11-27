<template>
  <div class="main" v-for="(item,index) in re" :key="index">
    <div class="pie" style="width:100%;height:380px;">
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import axios from "axios";

export default {
  name: 'EchartsPage',
  data() {
    return {
      timer: null,
      re: []
    }
  },
  created() {
    this.initEcharts();
    this.timer = setInterval(() => {
      setTimeout(this.initEcharts, 0)
    }, 1000)
  },
  methods: {
    initEcharts() {
      axios.get('http://localhost:3001/api?from=' + this.$route.path.substring(1)).then(res => {
        this.re = res.data;
        for (let i = 0; i < this.re.length; i++) {
          this.drawEcharts(document.getElementsByClassName("pie")[i], this.re[i].title, this.re[i].items);
        }
      }).catch(err => {
        console.log("error------------------" + err);
      });
    },
    drawEcharts(element, title, data) {
      const option = {
        legend: {
          type: "scroll",
          top: "20%",
          right: "5%",
          orient: "vertical"
        },
        title: {
          text: title,
          top: "45%",
          left: "center"
        },
        series: [
          {
            type: "pie",
            label: {
              show: true,
              formatter: "{b} : {d}%" // b代表名称，c代表对应值，d代表百分比
            },
            radius: ["40%", "70%"],
            data: data
          }
        ]
      };
      let myChart = echarts.getInstanceByDom(element);
      if (myChart == null) {
        myChart = echarts.init(element);
      }
      myChart.setOption(option);
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    }
  },
  unmounted() {
    clearInterval(this.timer)
    this.timer = null
  }
}
</script>
<style scoped>

</style>
