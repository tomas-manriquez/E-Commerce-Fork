<script>
export default {
  data() {
    return {
      purchase: {
        customerId: '',
        productId: ''
      },
      customers: [],
      products: []
    };
  },
  mounted() {
    this.fetchCustomers();
    this.fetchProducts();
  },
  methods: {
    async fetchCustomers() {
      // Aquí puedes agregar el código para obtener los clientes del servidor
      console.log('Fetching customers...');
      // Ejemplo de datos ficticios
      this.customers = [
        { id: 1, name: 'Cliente A' },
        { id: 2, name: 'Cliente B' }
      ];
    },
    async fetchProducts() {
      // Aquí puedes agregar el código para obtener los productos del servidor
      console.log('Fetching products...');
      // Ejemplo de datos ficticios
      this.products = [
        { id: 1, name: 'Producto A', price: 10.99 },
        { id: 2, name: 'Producto B', price: 15.49 }
      ];
    },
    buy() {
      // Aquí puedes agregar el código para enviar los datos de la compra al servidor
      console.log('Compra realizada:', this.purchase);
      this.$router.push('/purchases');
    }
  }
};
</script>

<template>
  <div>
    <h2>Ingresar Compra</h2>
    <form @submit.prevent="buy">
      <div>
        <label for="customer">Cliente:</label>
        <select id="customer" v-model="purchase.customerId" required>
          <option disabled value="">Selecciona un cliente</option>
          <option v-for="customer in customers" :key="customer.id" :value="customer.id">
            {{ customer.name }}
          </option>
        </select>
      </div>
      <div>
        <label for="product">Producto:</label>
        <select id="product" v-model="purchase.productId" required>
          <option disabled value="">Selecciona un producto</option>
          <option v-for="product in products" :key="product.id" :value="product.id">
            {{ product.name }} - ${{ product.price }}
          </option>
        </select>
      </div>
      <button type="submit">Comprar</button>
    </form>
  </div>
</template>

<style scoped>

</style>