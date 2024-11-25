<template>
  <div class="ordenes-container">
    <h1>Lista de Ordenes</h1>
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
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      ordenes: [],
      idCliente: null
    };
  },
  methods: {
    async fetchOrdenes(id) {
      try {
        const response = await api.get("/api/v1/ordenes/byClientId/" + id);
        this.ordenes = response.data;
      } catch (error) {
        console.error("Error al obtener las orden:", error);
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

</style>
