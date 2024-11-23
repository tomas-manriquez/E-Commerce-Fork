<script>
import axios from "axios";

export default {
  data() {
    return {
      topClientes: [],
      errorMessage: null,
      isLoading: true,
    };
  },
  methods: {
    async fetchQuery() {
      this.isLoading = true;
      try {
        const response = await axios.get('/api/v1/clientes/top-gastadores-tecnologia', {
          headers: {
            'Accept': 'application/json',
          },
          withCredentials: false,
        });
        console.log("Headers enviados:", response.config.headers);
        this.topClientes = response.data;
      } catch (error) {
        if (error.response && error.response.status === 431) {
          this.errorMessage = 'La solicitud excedió el límite permitido. Intenta limpiar las cookies o reducir datos enviados.';
        } else {
          this.errorMessage = 'Error al cargar los datos. Inténtalo de nuevo.';
        }
        console.error('Error fetching query:', error);
      } finally {
        this.isLoading = false; // Termina el estado de carga
      }
    },

  },
  mounted() {
    this.fetchQuery();
  }
}
</script>

<template>
  <section class="main-section">
    <div class="overlay">
      <div class="container">
        <h1>Top 5 Clientes - Tecnología</h1>
        <!-- Mostrar una tabla si hay datos -->
        <template v-if="!isLoading && topClientes.length">
          <table>
            <thead>
            <tr>
              <th>Cliente</th>
              <th>Total Gastado</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(cliente, index) in topClientes" :key="index">
              <td>{{ cliente.cliente }}</td>
              <td>${{ cliente.total_gastado.toFixed(2) }}</td>
            </tr>
            </tbody>
          </table>
        </template>

        <!-- Mensajes en caso de error o carga -->
        <template v-else-if="isLoading">
          <p>Cargando datos...</p>
        </template>
        <template v-else>
          <p>{{ errorMessage || 'No hay datos disponibles.' }}</p>
        </template>
      </div>
    </div>
  </section>
</template>

<style scoped>
.main-section {
  background: linear-gradient(115deg, #FF7E5F 10%, #FEB47B 90%);
  background-size: cover;
  background-position: center;
  height: 600px;
  position: relative;
}

.main-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.main-section > * {
  position: relative;
  z-index: 2;
}

.overlay {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.container {
  background-color: rgba(255, 255, 255, 0.3);
  border: none;
  padding: 1rem 2rem;
  font-size: 1.2rem;
  color: #ffffff;
  margin: 1rem;
  transition: transform 0.3s ease;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}

.container:hover {
  transform: scale(1.1);
}
table {
  width: 100%;
  border-collapse: collapse;
  margin: 1rem 0;
}

th, td {
  border: 1px solid #fff;
  padding: 8px 12px;
  text-align: left;
  color: #fff;
}

th {
  background-color: rgba(255, 255, 255, 0.2);
}

td {
  background-color: rgba(255, 255, 255, 0.1);
}

h1 {
  color: #fff;
  text-align: center;
}
</style>
