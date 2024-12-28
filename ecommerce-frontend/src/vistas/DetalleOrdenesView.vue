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
        <th>Acciones</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="detalle in detalles" :key="detalle.idDetalle">
        <td>{{ detalle.idDetalle }}</td>
        <td>{{ detalle.idProducto }}</td>
        <td>{{ getProductoNombre(detalle.idProducto) }}</td>
        <td>{{ detalle.cantidad }}</td>
        <td>$ {{ detalle.precioUnitario }}</td>
        <td>
          <button
              v-if="ordenEstado === 'recibido por cliente'"
              @click="abrirPopupResena(detalle)"
              class="button"
          >
            Reseñar producto
          </button>
        </td>
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

    <div v-if="mostrarPopup" class="popup-overlay">
      <div class="popup-content">
        <h2>Reseñar producto</h2>
        <p>Producto: {{ productoSeleccionado.nombre }}</p>
        <textarea v-model="resenaTexto" placeholder="Escribe tu reseña aquí"></textarea>
        <label for="puntuacion">Puntuación:</label>
        <div class="star-rating">
    <span
        v-for="star in stars"
        :key="star"
        :class="{'active': star <= (hoverRating || rating)}"
        @click="setRating(star)"
        @mouseover="setHoverRating(star)"
        @mouseleave="hoverRating = 0"
    >
      ★
    </span>
        </div>
        <button @click="enviarResena" class="button">Enviar Reseña</button>
        <button @click="cerrarPopup" class="button">Cancelar</button>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  props: {
    value: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      detalles: [],
      productos: {},
      idOrden: null,
      ordenEstado: null,
      loading: false,
      error: null,
      pagination: {
        currentPage: 1,
        totalPages: 1,
        totalItems: 0,
      },
      mostrarPopup: false,
      productoSeleccionado: {},
      resenaTexto: "",
      rating: this.value,
      hoverRating: 0
    };
  },
  computed: {
    stars() {
      return [1, 2, 3, 4, 5];
    }
  },
  methods: {
    async fetchDetalles(id, page = 1, size = 10) {
      try {
        this.loading = true;
        this.error = null;
        const response = await api.get(`/api/v1/detalles/byOrdenId/pag/${id}`, {
          params: { page, size },
        });
        this.detalles = response.data.content;
        this.pagination = {
          currentPage: response.data.currentPage,
          totalPages: response.data.totalPages,
          totalItems: response.data.totalElements,
        };

        const productoIds = [...new Set(this.detalles.map((d) => d.idProducto))];
        await this.precargarProductos(productoIds);

        // Obtener estado de la orden
        const ordenResponse = await api.get(`/api/v1/ordenes/byId/${id}`);
        this.ordenEstado = ordenResponse.data.estado;
        console.log("Estado de la orden:", this.ordenEstado);
      } catch (error) {
        this.error = "Error al obtener los detalles de la orden.";
        console.error("Error al obtener los detalles de la orden:", error);
      } finally {
        this.loading = false;
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
    abrirPopupResena(detalle) {
      this.productoSeleccionado = this.productos[detalle.idProducto];
      this.mostrarPopup = true;
    },
    cerrarPopup() {
      this.mostrarPopup = false;
      this.productoSeleccionado = {};
      this.resenaTexto = "";
      this.rating = 0;
    },
    async goToPage(page) {
      if (page > 0 && page <= this.pagination.totalPages) {
        await this.fetchDetalles(this.idOrden,page,10); // Cambiar de página
      }
    },
    async enviarResena() {
      try {
        const resenaData = {
          idCliente: localStorage.getItem("userId"),
          idProducto: this.productoSeleccionado.idProducto,
          idCategoria: this.productoSeleccionado.idCategoria,
          fecha: new Date().toISOString(),
          comentario: this.resenaTexto,
          puntuacion: this.rating,
        };
        await api.post("/api/v1/opiniones/create", resenaData);
        alert("Reseña enviada con éxito.");
        this.cerrarPopup();
      } catch (error) {
        console.error("Error al enviar la reseña:", error);
        alert("Hubo un error al enviar la reseña.");
      }
    },
    setRating(star) {
      this.rating = star;
      this.$emit('input', this.rating);
    },
    setHoverRating(star) {
      this.hoverRating = star;
    },
    resetHover() {
      this.hoverRating = 0;
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

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.popup-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

textarea {
  width: 100%;
  height: 100px;
  margin-bottom: 15px;
}

.star-rating {
  display: flex;
  cursor: pointer;
}
.star-rating span {
  font-size: 2em;
  color: #ccc;
}
.star-rating span.active,
.star-rating span:hover {
  color: #ff7300;
}
</style>
