<script>
export default {
  data() {
    return {
      userType: ''
    }
  },
  mounted() {
    this.userType = localStorage.getItem('userType');
  },
  methods: {
    ordenes() {
      this.$router.push("/add/ordenes");
    },
    productos() {
      this.$router.push("/add/productos");
    }
  }
}
</script>

<template>
  <section class="main-section">
    <div class="overlay">
      <div class="container" @click="ordenes">
        Agregar Ordenes
      </div>
      <div v-if="userType === 'ADMIN'" class="container" @click="productos">
        Agregar Productos
      </div>
    </div>
  </section>
</template>



<style scoped>
.main-section {
  background: linear-gradient(115deg, #FF7E5F 10%, #FEB47B 90%);
  background-size: cover;
  background-position: center;
  height: 600px;
  position: relative;
}

.main-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.main-section > * {
  position: relative;
  z-index: 2;
}

.overlay {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.container {
  background-color: rgba(255, 255, 255, 0.3);
  border: none;
  padding: 1rem 2rem;
  font-size: 1.2rem;
  color: #ffffff;
  margin: 1rem;
  transition: transform 0.3s ease;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}

.container:hover {
  transform: scale(1.1);
}
</style>
