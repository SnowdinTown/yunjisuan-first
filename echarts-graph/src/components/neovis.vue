<template>
  <div class="neovis" id="viz1" ref="viz1"></div>
</template>

<script>
import NeoVis from 'neovis.js';

export default {
  name: "NeovisPage",
  data() {
    return {
      viz: {},
    }
  },
  mounted() {
    this.draw();
  },
  methods: {
    draw() {
      var config = {
        container_id: "viz1",
        server_url: "bolt://localhost:7687",
        server_user: "neo4j",
        server_password: "123456",
        labels: {
          // "CITY": { caption: "name", community: "#5496FF", size: 200, font: { size: 35, color: "#606266", }, },
        },
        relationships: {
          // "待遇政策": { thickness: 1, caption: true, font: { size: 15, color: "#606266", }, },
        },
        arrows: false,
        // hierarchical: true,
        // hierarchical_sort_method: "directed",
        initial_cypher: "MATCH p=()-->() RETURN p",
      };
      var viz = new NeoVis(config);
      viz.render();
    }
  }
}
</script>

<style scoped>

</style>