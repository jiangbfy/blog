<script setup lang="ts">

defineProps<{
  text: string,
  tip: string
}>()

</script>

<template>
  <div class="tooltip">
    <span>{{ text }}</span>
    <span class="tooltiptext">{{ tip }}</span>
  </div>
</template>

<style lang="scss" scoped>
.tooltip {
  position: relative;
  display: inline-block;
  cursor: pointer;
}
.tooltip:hover .tooltiptext {
  visibility: visible;
}

.tooltip .tooltiptext {
  width: 200px;
  bottom: 120%;
  left: 50%;
  margin-left: -100px;
  visibility: hidden;
  background-color: #801eff;
  color: white;
  text-align: center;
  border-radius: 5px;
  padding: 5px;
  position: absolute;
  font-size: 20px;
  z-index: 1;
}

.tooltip .tooltiptext::after {
  top: 100%;
  left: 50%;
  margin-left: -5px;
  border-color: #801eff transparent transparent transparent;
  content: "";
  position: absolute;
  border-width: 5px;
  border-style: solid;
}
</style>
