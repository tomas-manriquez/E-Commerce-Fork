<template>
  <div class="ordenes-container">
    <h1>Detalles de la Orden</h1>
    <div v-if="error" class="error">
      {{ error }}
    </div>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>ID Producto</th>
        <th>Nombre producto</th>
        <th>Cantidad</th>
        <th>Precio unitario (CLP)</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="detalle in detalles" :key="detalle.idDetalle">
        <td>{{ detalle.idDetalle }}</td>
        <td>{{ detalle.idProducto }}</td>
        <td>{{ getProductoNombre(detalle.idProducto) }}</td>
        <td>{{ detalle.cantidad }}</td>
        <td>$ {{ detalle.precioUnitario }}</td>
      </tr>
      </tbody>
    </table>
    <!-- Empty state -->
    <div v-if="!loading && !error && !detalles.length">No hay detalles disponibles.</div>
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
      detalles: [],
      productos: {},
      idOrden: null,
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
    async fetchDetalles(id, page =1, size = 10) {
      try {
        this.loading = true;
        this.error = null;
        const response = await api.get(`/api/v1/detalles/byOrdenId/pag/${id}`,{
          params: { page, size },
        });
        this.detalles = response.data.content;
        this.pagination = {
          currentPage: response.data.currentPage,
          totalPages: response.data.totalPages,
          totalItems: response.data.totalElements,
        };

        // Extraer IDs únicos de productos para precargar sus nombres
        const productoIds = [...new Set(this.detalles.map(d => d.idProducto))];
        await this.precargarProductos(productoIds); // Esperar a que se carguen los productos
      } catch (error) {
        this.error = "Error al obtener los detalles de la orden.";
        console.error("Error al obtener los detalles de la orden:", error);
      }
    },
    async precargarProductos(ids) {
      try {
        const requests = ids.map(id => api.get("/api/v1/productos/byId/" + id));
        const responses = await Promise.all(requests);

        // Transformar los datos en un objeto reactivo { idProducto: producto }
        responses.forEach(response => {
          const producto = response.data;
          this.productos[producto.idProducto] = producto; // Actualizar el objeto reactivo directamente
        });
      } catch (error) {
        console.error("Error al precargar los productos:", error);
      }
    },
    getProductoNombre(idProducto) {
      // Verificar si el producto está cargado antes de acceder a su nombre
      if (this.productos[idProducto]) {
        return this.productos[idProducto].nombre;
      }
      return "Cargando..."; // Si aún no se ha cargado, mostrar "Cargando..."
    },
    async goToPage(page) {
      if (page > 0 && page <= this.pagination.totalPages) {
        await this.fetchDetalles(this.idOrden,page,10); // Cambiar de página
      }
    },
  },
  mounted() {
    const idOrden = this.$route.params.id;
    this.idOrden = idOrden;
    this.fetchDetalles(idOrden);
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
</style>
