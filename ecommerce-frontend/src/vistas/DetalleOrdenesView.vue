<template>
  <div class="ordenes-container">
    <h1>Detalles de la Orden</h1>
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
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      detalles: [],
      productos: {}, // Objeto reactivo para almacenar los productos
      idOrden: null,
    };
  },
  methods: {
    async fetchDetalles(id) {
      try {
        const response = await api.get("/api/v1/detalles/byOrdenId/" + id);
        this.detalles = response.data;

        // Extraer IDs únicos de productos para precargar sus nombres
        const productoIds = [...new Set(this.detalles.map(d => d.idProducto))];
        await this.precargarProductos(productoIds); // Esperar a que se carguen los productos
      } catch (error) {
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
</style>
