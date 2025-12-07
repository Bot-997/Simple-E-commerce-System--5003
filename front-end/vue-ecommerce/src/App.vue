<template>
  <div id="app-wrapper">
    <NavBar />
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import NavBar from './components/NavBar.vue'
</script>

<style>
body { 
  margin: 0; 
  background-color: #f5f7fa; 
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; 
}
.main-content { 
  min-height: calc(100vh - 60px); 
}
</style>