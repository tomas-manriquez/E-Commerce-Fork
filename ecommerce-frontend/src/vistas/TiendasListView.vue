<template>
  <div class="clients-container">
    <h1>Lista de Tiendas</h1>

    <!-- Error state -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <!-- Tiendas table -->
    <table>
      <thead>
      <tr>
        <th>ID tienda</th>
        <th>Nombre tienda</th>
        <th>ID Zona</th>
        <th>Nombre zona</th>
        <th>Acciones</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="tienda in tiendas" :key="tienda.idtienda">
        <td>{{ tienda.idtienda }}</td>
        <td>{{ tienda.nombre_tienda}}</td>
        <td>{{ tienda.idzona }}</td>
        <td>{{ tienda.nombrezona }}</td>
        <td>
          <router-link :to="`/tienda/${tienda.idtienda}/${tienda.idzona}`" class="btn">
            Ver zonas de esta tienda
          </router-link>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Empty state -->
    <div v-if="!loading && !error && !tiendas.length">No hay tiendas.</div>

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
      tiendas: [],
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
    async fetchTiendas(page = 1, size = 10) {
      try {
        this.loading = true;
        this.error = null;
        const response = await api.get("/api/v1/tiendas/tiendasyzonas", {
          params: { page, size },
        });

        this.tiendas = response.data;
        this.pagination = {
          currentPage: response.data.currentPage,
          totalPages: response.data.totalPages,
          totalItems: response.data.totalElements,
        };

      } catch (error) {
        console.error("Error al obtener las tiendas:", error);
      }
    },
    async goToPage(page) {
      if (page > 0 && page <= this.pagination.totalPages) {
        await this.fetchTiendas(page, 10); // Cambiar de página
      }
    },
  },
  mounted() {
    this.fetchTiendas();
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

.btn {
  display: inline-block;
  padding: 10px 20px;
  background-color: #ff7300;
  color: white;
  border: none;
  text-decoration: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn:hover {
  background-color: #c85d00;
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
