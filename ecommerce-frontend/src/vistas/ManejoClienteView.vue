<script>
export default {
  data() {
    return {
      customer: {
        name: '',
        email: ''
      }
    };
  },
  methods: {
    addCustomer() {
      // Aquí puedes agregar el código para enviar los datos del cliente al servidor
      console.log('Cliente agregado:', this.customer);
      this.$router.push('/customers');
    }
  }
};
</script>

<template>
  <div>
    <h2>Agregar Cliente</h2>
    <form @submit.prevent="addCustomer">
      <div>
        <label for="name">Nombre:</label>
        <input type="text" id="name" v-model="customer.name" required />
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="customer.email" required />
      </div>
      <button type="submit">Agregar Cliente</button>
    </form>
  </div>
</template>

<style scoped>

</style>