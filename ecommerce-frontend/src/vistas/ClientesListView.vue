<template>
  <div class="clients-container">
    <h1>Lista de Clientes</h1>

    <!-- Error state -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <!-- Clientes table -->
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Dirección</th>
        <th>Email</th>
        <th>Teléfono</th>
        <th>Rol</th>
        <th>Acciones</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cliente in clientes" :key="cliente.idCliente">
        <td>{{ cliente.idCliente }}</td>
        <td>{{ cliente.nombre }}</td>
        <td>{{ cliente.direccion }}</td>
        <td>{{ cliente.email }}</td>
        <td>{{ cliente.telefono }}</td>
        <td>{{ cliente.rol }}</td>
        <td>
          <button @click="eliminarCliente(cliente.idCliente)">Eliminar</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Empty state -->
    <div v-if="!loading && !error && !clientes.length">No hay clientes disponibles.</div>

    <!-- Pagination controls -->
    <div v-if="pagination.totalPages > 1">
      <button :disabled="pagination.currentPage === 1" @click="goToPage(pagination.currentPage - 1)">
        Anterior
      </button>
      <span>Página {{ pagination.currentPage }} de {{ pagination.totalPages }}</span>
      <button :disabled="pagination.currentPage === pagination.totalPages" @click="goToPage(pagination.currentPage + 1)">
        Siguiente
      </button>
    </div>

  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      clientes: [],
      loading: false,
      error: null,
      pagination: {
        currentPage: 1,
        totalPages: 1,
        totalItems: 0,
      },
    };
  },
  methods: {
    async fetchClientes(page = 1, size = 10) {
      try {
        this.loading = true;
        this.error = null;
        const response = await api.get("/api/v1/clientes/page", {
          params: { page, size },
        });
        
        this.clientes = response.data.content;
        this.pagination = {
          currentPage: response.data.currentPage,
          totalPages: response.data.totalPages,
          totalItems: response.data.totalElements,
        };        

      } catch (error) {
        console.error("Error al obtener los clientes:", error);
      }
    },
    async eliminarCliente(id) {
      const confirmacion = confirm("¿Estás seguro de que quieres eliminar este cliente?");
      if (confirmacion) {
        try {
          await api.delete(`/api/v1/clientes/${id}`);
          alert("Cliente eliminado con éxito.");
          await this.fetchClientes(); // Refrescar la lista de clientes
        } catch (error) {
          console.error("Error al eliminar el cliente:", error);
          alert("No se pudo eliminar el cliente.");
        }
      }
    },

    async goToPage(page) {
      if (page > 0 && page <= this.pagination.totalPages) {
        await this.fetchClientes(page, 10); // Cambiar de página
      }
    },

  },
  mounted() {
    this.fetchClientes();
  },
};
</script>

<style scoped>

.clients-container {
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

thead th {
  background-color: #f4f4f4;
  padding: 10px;
}

tbody td {
  padding: 10px;
  text-align: center;
}

button {
  margin: 0 5px;
  padding: 5px 10px;
  border: none;
  color: white;
  background-color: #007bff;
  cursor: pointer;
  border-radius: 3px;
}

button:hover {
  background-color: #0056b3;
}

button:last-child {
  background-color: #dc3545;
}

button:last-child:hover {
  background-color: #c82333;
}

.loading,
.error,
.empty {
  text-align: center;
  padding: 20px;
  margin: 20px 0;
}

.error {
  color: red;
  background-color: #ffebee;
  border-radius: 4px;
}

</style>
