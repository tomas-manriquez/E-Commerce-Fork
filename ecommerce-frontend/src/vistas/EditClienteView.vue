<template>
  <div class="edit-cliente">
    <h2>Editar Cliente</h2>

    <!-- Formulario para editar los datos del cliente -->
    <form @submit.prevent="updateCliente()">
      <div>
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" v-model="cliente.nombre" required />
      </div>

      <div>
        <label for="direccion">Dirección:</label>
        <input type="text" id="direccion" v-model="cliente.direccion" required />
      </div>

      <div>
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="cliente.email" required />
      </div>

      <div>
        <label for="telefono">Teléfono:</label>
        <input type="text" id="telefono" v-model="cliente.telefono" required />
      </div>

      <div>
        <label for="username">Username:</label>
        <input type="text" id="username" v-model="cliente.username" required />
      </div>

      <div>
        <label for="rol">Rol: {{cliente.rol}}</label>
      </div>

      <button type="submit">Actualizar Cliente</button>
    </form>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      cliente: {
        idcliente: null,
        nombre: '',
        direccion: '',
        email: '',
        telefono: '',
        username: '',
        rol: ''
      },
      idClient: null,
    };
  },
  mounted() {
    const clienteId = this.$route.params.id;
    this.idCliente = clienteId;
    console.log(clienteId);
    this.getCliente(clienteId);
  },
  methods: {
    // Obtener los datos del cliente desde la API
    getCliente(id) {
      api.get(`/api/v1/clientes/byId/${id}`)
          .then(response => {
            this.cliente = response.data; // Asignar los datos al cliente
          })
          .catch(error => {
            console.error('Error al obtener el cliente:', error);
          });
    },
    // Método para actualizar los datos del cliente
    updateCliente() {
      api.put(`/api/v1/clientes/update`, this.cliente)
          .then(() => {
            alert('Cliente actualizado correctamente');
            // Redirigir a otra página después de actualizar, por ejemplo a la lista de clientes
            this.$router.push(`/perfil/${Number(this.idCliente)}`);
          })
          .catch(error => {
            console.error('Error al actualizar el cliente:', error);
            alert('Error al actualizar el cliente');
          });
    }
  }
};
</script>

<style scoped>
.edit-cliente {
  max-width: 500px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}

form div {
  margin-bottom: 15px;
}

form label {
  display: block;
  font-weight: bold;
}

form input, form select {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  padding: 10px 20px;
  background-color: #ff7300;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #c85d00;
}
</style>
