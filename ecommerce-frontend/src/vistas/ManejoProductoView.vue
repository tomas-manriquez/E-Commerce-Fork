<script>
export default {
  data() {
    return {
      product: {
        name: '',
        price: 0
      }
    };
  },
  methods: {
    addProduct() {
      // Aquí puedes agregar el código para enviar los datos del producto al servidor
      console.log('Producto agregado:', this.product);
      this.$router.push('/products');
    }
  }
};
</script>

<template>
  <div>
    <h2>Agregar Producto</h2>
    <form @submit.prevent="addProduct">
      <div>
        <label for="name">Nombre:</label>
        <input type="text" id="name" v-model="product.name" required />
      </div>
      <div>
        <label for="price">Precio:</label>
        <input type="number" id="price" v-model.number="product.price" required />
      </div>
      <button type="submit">Agregar Producto</button>
    </form>
  </div>
</template>

<style scoped>

</style>