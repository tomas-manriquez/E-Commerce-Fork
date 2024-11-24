<template>
  <div class="products-container">
    <h2>Lista de Productos</h2>

    <!-- Loading state -->
    <div v-if="loading" class="loading">
      Cargando productos...
    </div>

    <!-- Error state -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <!-- Products table -->
    <table v-if="!loading && !error && products.length" class="products-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Descripción</th>
        <th>Precio</th>
        <th>Stock</th>
        <th>Estado</th>
        <th>Categoría ID</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="producto in products" :key="producto.idProducto">
        <td>{{ producto.idProducto }}</td>
        <td>{{ producto.nombre }}</td>
        <td>{{ producto.descripcion }}</td>
        <td>{{ formatPrice(producto.precio) }}</td>
        <td>{{ producto.stock }}</td>
        <td>{{ producto.estado }}</td>
        <td>{{ producto.idCategoria }}</td>
      </tr>
      </tbody>
    </table>

    <!-- Empty state -->
    <div v-if="!loading && !error && !products.length" class="empty">
      No hay productos disponibles.
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import api from "@/services/api";

export default {
  name: 'ProductList',
  setup() {
    const products = ref([])
    const loading = ref(true)
    const error = ref(null)

    const fetchProducts = async () => {
      try {
        loading.value = true
        error.value = null
        const response = await api.get('/api/v1/productos/all')
        products.value = response.data
      } catch (err) {
        error.value = 'Error al cargar los productos: ' + (err.response?.data?.message || err.message)
      } finally {
        loading.value = false
      }
    }

    const formatPrice = (price) => {
      return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'CLP'
      }).format(price)
    }

    onMounted(() => {
      fetchProducts()
    })

    return {
      products,
      loading,
      error,
      formatPrice
    }
  }
}
</script>

<style scoped>
.products-container {
  padding: 20px;
}

.products-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.products-table th,
.products-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.products-table th {
  background-color: #f4f4f4;
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