<template>
  <div id="echarts" style="width:100%;height:100%;"></div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "EchartsPage",
  props: {
    index: Number,
    item: String,
    nodes: {
      type: Array,
    },
    links: {
      type: Array,
    },
    category: {
      type: Array,
    },
  },
  data() {
    return {
      colors: ['#B0C4DE']
    }
  },
  created() {
    // this.executeCypher(this.sql.replaceAll("?", this.item));

  },
  mounted() {
    this.drawEcharts();
  },
  methods: {
    drawEcharts() {
      // console.log("data----", this.nodes, "\nlink-----", this.links);
      var myChart = echarts.init(document.getElementById("echarts"));
      const option = {
        color: [
          '#FFC0CB'
        ],
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true
            },
            restore: {
              show: true
            },
            saveAsImage: {
              show: true
            }
          },
        },
        series: [{
          name: this.item,
          type: 'graph',
          layout: 'force', // none,force,circular
          // layout: 'circular',
          zoom: 15,
          cursor: 'pointer',
          symbolSize: 5,
          roam: true, // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移,可以设置成 'scale' 或者 'move'。设置成 true 为都开启
          draggable: true, // 节点是否可以拖动
          force: {
            repulsion: 15, // 节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
            gravity: 10, // 节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
            // edgeLength: [10, 50], // 边的两个节点之间的距离，这个距离也会受 repulsion影响 。值越大则长度越长
            layoutAnimation: false, // 因为力引导布局会在多次迭代后才会稳定，这个参数决定是否显示布局的迭代动画，在浏览器端节点数据较多（>100）的时候不建议关闭，布局过程会造成浏览器假死。
            // friction: 0.6 // 减缓节点的移动速度。取值范围 0 到 1
          },
          label: {
            show: true,
            position: 'right'
          },
          edgeLabel: {
            show: true,
            fontSize: 20,
            fontWeight: 300,
            opacity: 0.4,
            formatter: '{c}',
          },
          lineStyle: {
            color: 'rgb(176,196,222)',
            curveness: 0.1,
            opacity: 1
          },
          // focusNodeAdjacency: true, // 是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。
          emphasis: {
            scale: 2, // 设置高亮放大倍数，默认放大 1.1 倍
            focus: 'adjacency', // 聚焦关系图中的邻接点和边的图形
            edgeLabel: {
              fontWeight: 400,
              opacity: 1,
            },
            itemStyle: {
              color: '#FFA07A',
              lineStyle: {
                opacity: 1
              }
            }
          },
          data: this.nodes,
          links: this.links,
          categories: this.category,
        }]

      }
      myChart.setOption(option);
    }
  }
}
</script>

<style scoped>

</style>