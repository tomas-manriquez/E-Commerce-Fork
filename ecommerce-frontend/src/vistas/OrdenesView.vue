<template>
  <div class="ordenes-container">
    <h1>Lista de Ordenes</h1>

    <!-- Error state -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Fecha de la orden</th>
        <th>Estado</th>
        <th>Total (CLP)</th>
        <th>Acciones</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="orden in ordenes" :key="orden.idOrden">
        <td>{{ orden.idOrden }}</td>
        <td>{{ orden.fechaOrden }}</td>
        <td>{{ orden.estado }}</td>
        <td>$ {{ orden.total }}</td>
        <td>
          <router-link :to="`/ordenes/detalle/${orden.idOrden}`" class="button">Ver detalles</router-link>
        </td>
      </tr>
      </tbody>
    </table>
    <!-- Empty state -->
    <div v-if="!loading && !error && !ordenes.length">No hay ordenes disponibles.</div>
    <!-- Pagination controls -->
    <div v-if="pagination.totalPages > 1">
      <button :disabled="pagination.currentPage === 1" @click="goToPage(pagination.currentPage - 1)" class="button">
        Anterior
      </button>
      <span>Página {{ pagination.currentPage }} de {{ pagination.totalPages }}</span>
      <button :disabled="pagination.currentPage === pagination.totalPages" @click="goToPage(pagination.currentPage + 1)" class="button">
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
      ordenes: [],
      idCliente: null,
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
    async fetchOrdenes(id, page=1, size=10) {
      try {
        this.loading = true;
        this.error = null;
        const response = await api.get("/api/v1/ordenes/byClientId/pag/" + id, {
          params: { page, size },
        });
        this.ordenes = response.data.content;
        this.pagination = {
          currentPage: response.data.currentPage,
          totalPages: response.data.totalPages,
          totalItems: response.data.totalElements,
        }
      } catch (error) {
        console.error("Error al obtener las orden:", error);
      }
    },
    async goToPage(page) {
      if (page > 0 && page <= this.pagination.totalPages) {
        await this.fetchOrdenes(this.idOrden,page, 10); // Cambiar de página
      }
    },
  },
  mounted() {
    const idcliente = Number(localStorage.getItem("userId"));
    this.idCliente = idcliente;
    this.fetchOrdenes(idcliente);
  },
};
</script>

<style scoped>

.ordenes-container {
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

.button {
  margin: 0 5px;
  padding: 5px 10px;
  border: none;
  color: white;
  background-color: #ff7300;
  cursor: pointer;
  border-radius: 3px;
  text-decoration: none;
}

.button:hover {
  background-color: #cf6100;
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
