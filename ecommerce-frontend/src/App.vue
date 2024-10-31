<script >
import Header from './vistas/HeaderView.vue';
import FooterView from "./vistas/FooterView.vue";

export default {
  components: {
    FooterView,
    Header,

  }
}
</script>

<template>
  <Header />
  <router-view />
  <FooterView />
</template>

<style>
html,
body
{
  height: 100vh;
  width: 100%;
  margin: 0;
}

</style>