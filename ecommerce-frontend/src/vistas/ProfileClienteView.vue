<template>
  <div class="profile-cliente">
    <h2>Perfil Cliente</h2>

    <!-- Información del cliente -->
    <div>
      <label for="nombre">Nombre:</label>
      {{cliente.nombre}}
    </div>

    <div>
      <label for="direccion">Dirección:</label>
      {{cliente.direccion}}
    </div>

    <div>
      <label for="email">Email:</label>
      {{cliente.email}}
    </div>

    <div>
      <label for="telefono">Teléfono:</label>
      {{cliente.telefono}}
    </div>

    <div>
      <label for="username">Username:</label>
      {{cliente.username}}
    </div>

    <div>
      <label for="rol">Rol:</label>
      <p>{{ cliente.rol }}</p>
    </div>
    <div>
      <router-link :to="`/perfil/editar/${cliente.idCliente}`" class="btn-editar">
        Editar Perfil
      </router-link>
    </div>
    <div>
      <button @click="eliminarCliente(cliente.idCliente)" class="btn-eliminar">Eliminar</button>
    </div>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      cliente: {
        idCliente: '',
        nombre: '',
        direccion: '',
        email: '',
        telefono: '',
        username: '',
        rol: ''
      }
    };
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
    async eliminarCliente(id) {
      const confirmacion = confirm("¿Estás seguro de que quieres eliminar tu cuenta?");
      if (confirmacion) {
        try {
          await api.delete(`/api/v1/clientes/${id}`);
          alert("Cuenta eliminada con éxito.");
        } catch (error) {
          console.error("Error al eliminar el cliente", error);
        }
      }
    },
  },
  mounted() {
    const idcliente = Number(localStorage.getItem("userId"));
    this.getCliente(idcliente);
  }
};
</script>

<style scoped>
.profile-cliente {
  max-width: 500px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}

.profile-cliente div {
  margin-bottom: 15px;
}

.profile-cliente label {
  display: block;
  font-weight: bold;
}

.profile-cliente input {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.profile-cliente p {
  margin-top: 5px;
}

.btn-editar {
  display: inline-block;
  padding: 10px 20px;
  background-color: #ff7300;
  color: white;
  border: none;
  text-decoration: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn-editar:hover {
  background-color: #c85d00;
}

.btn-eliminar{
  background-color: #dc3545;
  display: inline-block;
  padding: 10px 20px;
  color: white;
  border: none;
  text-decoration: none;
  border-radius: 5px;
  cursor: pointer;
}
.btn-eliminar:hover{
  background-color: #c82333;
}
</style>
