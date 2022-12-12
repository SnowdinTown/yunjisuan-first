<template>
  <div class="visual" style="width:100%;height:100%;">
    <EchartsPage v-if="show" :item="item" :index="index" :nodes="nodes" :links="links" :category="category"/>
  </div>

</template>

<script>
import EchartsPage from "./echarts";
import neo4j from "neo4j-driver";

export default {
  name: 'GraphPage',
  components: {
    EchartsPage,
  },
  data() {
    return {
      index: Number(this.$route.query.index),
      item: this.$route.params.item,
      show: false,
      driver: null,
      sql: "MATCH p=(n1:?)-->(n2:?) RETURN p",
      nodes: [],
      links: [],
      category: []
    }
  },
  created() {
    this.executeCypher(this.sql.replaceAll("?", this.item));
  },
  methods: {
    executeCypher(query) {
      console.log("index+cql-----", typeof this.index, query);
      if (query == "") return;
      if (this.driver == null) {
        this.driver = neo4j.driver('bolt://172.19.240.57:7687', neo4j.auth.basic('neo4j', 'root'));
      }
      var session = this.driver.session();
      session.run(query, {}).then((result) => {
        var records = result.records;
        var temp = [];
        var label = new Set();
        for (let i = 0; i < records.length; i++) {
          var startNode = {
            name: records[i]._fields[0].segments[0].start.properties.name,
            category: records[i]._fields[0].segments[0].start.labels[0],
            // fixed: true // 节点在力引导布局中是否固定。
          };
          var endNode = {
            name: records[i]._fields[0].segments[0].end.properties.name,
            category: records[i]._fields[0].segments[0].end.labels[0],
          };
          if (temp.indexOf(startNode.name) == -1) {
            temp.push(startNode.name);
            this.nodes.push(startNode);
            label.add({name: startNode.category});
          }
          if (temp.indexOf(endNode.name) == -1) {
            temp.push(endNode.name);
            this.nodes.push(endNode);
            label.add({name: endNode.category});
          }

          this.links.push({
            source: records[i]._fields[0].segments[0].start.properties.name,
            target: records[i]._fields[0].segments[0].end.properties.name,
            // value: Number(records[i]._fields[0].segments[0].relationship.properties.weight),
            lineStyle: {
              width: Number((Math.pow(records[i]._fields[0].segments[0].relationship.properties.weight, 3) + 13)/10)
            }
            // ignoreForceLayout: true //	使此边不进行力导图布局的计算
          });
        }
        this.category = Array.from(label);
        this.show = true;
        console.log(this.nodes.length + "------" + this.links.length);
        session.close();
        // setTimeout(() => {
        // }, 4000);
      }).catch(function (error) {
        console.log("Cypher 执行失败！", error);
        this.driver.close();
      });

    }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
