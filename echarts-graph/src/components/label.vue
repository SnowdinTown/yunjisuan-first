<template>
  <div class="label">
    <el-button class="button"
               v-for="(item, index) in label"
               :key="index"
               @click="go(index)"
               style="cursor: pointer">
      {{ item }}
    </el-button>
  </div>
</template>

<script>

export default {
  name: 'LabelPage',
  data() {
    return {
      label: [
        // "java",
        "android",
        "python",
        "javascript",
        "hacktoberfest",
        "kotlin",
        "spring",
        "springboot"
      ]
    }
  },
  methods: {
    go(index) {
      this.$router.push({name: 'graph', query: {index: index}, params: {item: this.label[index]}});
    }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.label {
  padding: 300px;
}

.button {
  background: #fac858;
  color: #FFFFFF;
  border: 15px;
  padding: 15px;
  /*width: 150px;*/
  border-radius: 10px;
  display: inline-block;
  margin-left: 10px;
  margin-top: 20px;
  font-size: 35px;
}
</style>
