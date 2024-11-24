<template>
  <div class="products-container">
    <h1>Lista de Productos</h1>

    <!-- Loading state -->
    <div v-if="loading" class="loading">
      Cargando productos...
    </div>

    <!-- Error state -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <!-- Products table -->
    <table v-if="!loading && !error && products.length">
      <thead>
      <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Descripción</th>
        <th>Precio</th>
        <th>Stock</th>
        <th>Estado</th>
        <th>Categoría ID</th>
        <th v-if="isAdmin">Acciones</th>
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
        <td v-if="isAdmin">
          <button @click="abrirModalEdicion(producto)">Editar</button>
          <button @click="eliminarProducto(producto.idProducto)">Eliminar</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Empty state -->
    <div v-if="!loading && !error && !products.length" class="empty">
      No hay productos disponibles.
    </div>

    <!-- Edit Modal -->
    <div v-if="productoEditando" class="modal">
      <div class="modal-content">
        <h3>Editar Producto</h3>
        <form @submit.prevent="guardarEdicion">
          <div class="form-group">
            <label>Nombre:</label>
            <input type="text" v-model="productoEditando.nombre" required />
          </div>
          <div class="form-group">
            <label>Precio:</label>
            <input type="number" v-model.number="productoEditando.precio" required />
          </div>
          <div class="form-group">
            <label>Estado:</label>
            <select v-model="productoEditando.estado">
              <option value="Disponible">Disponible</option>
              <option value="Agotado">Agotado</option>
            </select>
          </div>
          <div class="form-group">
            <label>Stock:</label>
            <input type="number" v-model.number="productoEditando.stock" required />
          </div>
          <div class="form-group">
            <label>Categoría ID:</label>
            <input type="number" v-model.number="productoEditando.idCategoria" required />
          </div>
          <div class="form-group">
            <label>Descripción:</label>
            <textarea v-model="productoEditando.descripcion" required></textarea>
          </div>
          <div class="modal-actions">
            <button type="submit">Guardar</button>
            <button type="button" @click="cerrarModalEdicion">Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import api from "@/services/api";

export default {
  name: "ProductList",
  setup() {
    const products = ref([]);
    const loading = ref(true);
    const error = ref(null);
    const productoEditando = ref(null);

    const isAdmin = ref(false);

    const fetchProducts = async () => {
      try {
        loading.value = true;
        error.value = null;
        const response = await api.get("/api/v1/productos/all");
        products.value = response.data;
      } catch (err) {
        error.value =
            "Error al cargar los productos: " +
            (err.response?.data?.message || err.message);
      } finally {
        loading.value = false;
      }
    };

    const formatPrice = (price) => {
      return new Intl.NumberFormat("es-ES", {
        style: "currency",
        currency: "CLP",
      }).format(price);
    };

    const eliminarProducto = async (id) => {
      const confirmacion = confirm(
          "¿Estás seguro de que quieres eliminar este producto?"
      );
      if (confirmacion) {
        try {
          await api.delete(`/api/v1/productos/delete/${id}`);
          alert("Producto eliminado con éxito.");
          fetchProducts(); // Refrescar la lista de productos
        } catch (error) {
          console.error("Error al eliminar el producto:", error);
          alert("No se pudo eliminar el producto.");
        }
      }
    };

    const abrirModalEdicion = (producto) => {
      productoEditando.value = { ...producto }; // Crear una copia del producto
    };

    const guardarEdicion = async () => {
      try {
        await api.put(
            `/api/v1/productos/update`,
            productoEditando.value
        );
        alert("Producto actualizado con éxito.");
        cerrarModalEdicion();
        fetchProducts(); // Actualizar la lista
      } catch (error) {
        console.error("Error al editar el producto:", error);
        alert("No se pudo actualizar el producto.");
      }
    };

    const cerrarModalEdicion = () => {
      productoEditando.value = null;
    };

    onMounted(() => {
      fetchProducts();
      const rol = localStorage.getItem("userType");
      isAdmin.value = rol === "ADMIN";
    });

    return {
      products,
      loading,
      error,
      productoEditando,
      formatPrice,
      isAdmin,
      eliminarProducto,
      abrirModalEdicion,
      guardarEdicion,
      cerrarModalEdicion,
    };
  },
};
</script>

<style scoped>

.modal {
  z-index: 1000;
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: #fff;
  padding: 20px;
  padding-right: 40px;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  text-align: left;
}

h3 {
  margin-bottom: 20px;
  text-align: center;
}

form .form-group {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

form .form-group label {
  margin-bottom: 5px;
  font-weight: bold;
}

form .form-group input,
form .form-group select,
form .form-group textarea {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  width: 100%;
}

textarea {
  resize: none;
  height: 80px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.modal-actions button {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.modal-actions button:first-child {
  background-color: #007bff;
  color: white;
}

.modal-actions button:last-child {
  background-color: #dc3545;
  color: white;
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

.products-container {
  padding: 20px;
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