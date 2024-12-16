<template>
  <div class="repartidores-container">
    <h1>Lista de Repartidores</h1>

    <!-- Error state -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <!-- Repartidores table -->
    <table>
      <thead>
      <tr>
        <th>ID repartidor</th>
        <th>Nombre</th>
        <th>Apellido</th>
        <th>Fecha entrega</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="repartidor in repartidores" :key="repartidor.idRepartidor">
        <td>{{ repartidor.idRepartidor }}</td>
        <td>{{ repartidor.nombre}}</td>
        <td>{{ repartidor.apellido }}</td>
        <td>{{ repartidor.fechaEntrega }}</td>
      </tr>
      </tbody>
    </table>

    <div v-if ="repartidores.length===0">
      No hay entregas en esta zona, por lo que no se muestran los repartidores.
    </div>
    <!-- Empty state -->
    <div v-if="!loading && !error && !repartidores.length">No hay repartidores disponibles.</div>

  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      repartidores: [],
      loading: false,
      error: null
    };
  },
  methods: {
    async fetchRepartidores(idzona,idtienda) {
      try {
        this.loading = true;
        this.error = null;
        const response = await api.get("/api/v1/repartidores/"+idzona+"/"+idtienda);

        this.repartidores = response.data;
        console.log(response.data);
      } catch (error) {
        console.error("Error al obtener las repartidores:", error);
      }
    },
  },
  mounted() {
    const { idzona, idtienda } = this.$route.params;
    this.fetchRepartidores(idzona,idtienda);
  },
};
</script>

<style scoped>

.repartidores-container {
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
