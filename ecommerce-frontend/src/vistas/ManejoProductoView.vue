<script>
import api from "@/services/api";

export default {
  data() {
    return {
      product: {
        nombre: '',
        precio: 0,
        descripcion: '',
        stock: 0,
        estado: '',
        idCategoria: null // Aquí se seleccionará el ID de la categoría
      },
      categories: [], // Lista de categorías existentes
      newCategory: '', // Nombre de la nueva categoría a agregar
      showModal: false // Controla si la ventana emergente está visible
    };
  },
  mounted() {
    this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await api.get('/api/v1/categorias/all');
        this.categories = response.data;
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    },

    // Manejar cambio en el select de categoría
    handleCategoryChange() {
      if (this.product.idCategoria === 'add-new-category') {
        this.showModal = true;
        this.product.idCategoria = null; // Reiniciar la selección
      }
      else {
        const selectedCategory = this.categories.find(category => category.idCategoria === this.product.idCategoria);
        if (selectedCategory) {
          this.product.idCategoria = selectedCategory.idCategoria;
        }
      }
    },

    // Agregar nueva categoría
    async confirmAddCategory() {
      if (!this.newCategory.trim()) return; // Validación simple

      try {
        // Enviar nueva categoría al backend
        const response = await api.post('/api/v1/categorias/create', { nombre: this.newCategory }, {
          headers: {
            'Accept': 'application/json',
          },
          withCredentials: false,
        });
        console.log(response);
        console.log(this.newCategory)
        // Agregar la nueva categoría a la lista y seleccionarla automáticamente
        this.categories.unshift(this.newCategory);
        this.product.idCategoria = this.newCategory;

        // Limpiar y cerrar ventana
        this.newCategory = '';
        this.closeModal();
      } catch (error) {
        console.error('Error adding category:', error);
      }
    },

    // Cerrar ventana emergente
    closeModal() {
      this.showModal = false;
    },

    // Enviar producto al backend
    async addProduct() {
      try {
        console.log("producto: ", this.product);
        const response = await api.post('/api/v1/productos/create', this.product);
        console.log('Producto agregado:', response.data);
        this.$router.push('/productos'); // Redirigir después de guardar
      } catch (error) {
        console.error('Error adding product:', error);
      }
    }
  }
};
</script>

<template>
  <body>
    <div class="container">
      <div class="text">Agregar Producto</div>
      <form @submit.prevent="addProduct">
        <div class="form-row">
          <div class="input-data">
            <input type="text" id="nombre" v-model="product.nombre" required :disabled="showModal" />
            <div class="underline"></div>
            <label for="nombre">Nombre</label>
          </div>
          <div class="input-data">
            <input type="text" id="status" v-model="product.estado" required :disabled="showModal" />
            <div class="underline"></div>
            <label for="status">Estado</label>
          </div>
        </div>
        <div class="form-row">
          <div class="input-data">
            <input type="number" id="precio" v-model.number="product.precio" required :disabled="showModal" />
            <div class="underline"></div>
            <label for="precio">Precio</label>
          </div>
          <div class="input-data">
            <input type="number" id="stock" v-model.number="product.stock" required :disabled="showModal" />
            <div class="underline"></div>
            <label for="stock">Stock</label>
          </div>
          <div class="input-data">
            <select v-model="product.idCategoria" @change="handleCategoryChange" required :disabled="showModal">
              <option disabled value="">Seleccionar categoría</option>
              <option value="add-new-category">Agregar nueva categoría</option>
              <option v-for="category in categories" :key="category.idCategoria" :value="category.idCategoria">
                {{ category.nombre }}
              </option>
            </select>
            <div class="underline"></div>
          </div>
        </div>
        <div class="form-row">
          <div class="input-data textarea">
            <textarea rows="8" cols="80" v-model="product.descripcion" :disabled="showModal" required></textarea>
            <br />
            <div class="underline"></div>
            <label for="">Descripción</label>
          </div>
        </div>
        <div class="form-row submit-btn">
          <div class="input-data">
            <div class="inner"></div>
            <input type="submit" value="agregar" :disabled="showModal">
          </div>
        </div>
      </form>

      <div v-if="showModal" class="modal">
        <div class="modal-content">
          <h3>Agregar Nueva Categoría</h3>
          <input type="text" v-model="newCategory" placeholder="Nombre de la nueva categoría" />
          <div class="modal-buttons">
            <div class="submit-btn">
              <div class="input-data">
                <div class="inner"></div>
                <input type="button" value="Guardar" @click="confirmAddCategory">
              </div>
            </div>
            <div class="submit-btn">
              <div class="input-data">
                <div class="inner"></div>
                <input type="button" value="Cancelar" @click="closeModal">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</template>

<style scoped>
*{
  margin: 0;
  padding: 0;
  outline: none;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
}
body{
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 10px;
  font-family: 'Poppins', sans-serif;
  background: linear-gradient(115deg, #FF7E5F 10%, #FEB47B 90%);
}

.container{
  max-width: 800px;
  background: #fff;
  width: 800px;
  padding: 25px 40px 10px 40px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}
.container .text{
  text-align: center;
  font-size: 41px;
  font-weight: 600;
  font-family: 'Poppins', sans-serif;
  background: -webkit-linear-gradient(right, #FF7E5F, #FEB47B, #FF7E5F, #FEB47B);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.container form{
  padding: 30px 0 0 0;
}
.container form .form-row{
  display: flex;
  margin: 32px 0;
}
form .form-row .input-data{
  width: 100%;
  height: 40px;
  margin: 0 20px;
  position: relative;
}
form .form-row .textarea{
  height: 70px;
}
.input-data input,
.textarea textarea{
  display: block;
  width: 100%;
  height: 100%;
  border: none;
  font-size: 17px;
  border-bottom: 2px solid rgba(0,0,0, 0.12);
}
.input-data input:focus ~ label, .textarea textarea:focus ~ label,
.input-data input:valid ~ label, .textarea textarea:valid ~ label{
  transform: translateY(-20px);
  font-size: 14px;
  color: #FF7E5F;
}
.textarea textarea{
  resize: none;
  padding-top: 10px;
}
.input-data label{
  position: absolute;
  pointer-events: none;
  bottom: 10px;
  font-size: 16px;
  transition: all 0.3s ease;
}
.textarea label{
  width: 100%;
  bottom: 40px;
  background: #fff;
}
.input-data .underline{
  position: absolute;
  bottom: 0;
  height: 2px;
  width: 100%;
}
.input-data .underline:before{
  position: absolute;
  content: "";
  height: 2px;
  width: 100%;
  background: #FF7E5F;
  transform: scaleX(0);
  transform-origin: center;
  transition: transform 0.3s ease;
}

.input-data input:focus ~ .underline:before,
.input-data input:valid ~ .underline:before,
.textarea textarea:focus ~ .underline:before,
.textarea textarea:valid ~ .underline:before{
  transform: scale(1);
}

.submit-btn .input-data {
  position: relative;
  overflow: hidden;
  height: 45px;
  width: auto;
  display: inline-block;
}
.submit-btn .input-data .inner{
  height: 100%;
  width: 300%;
  position: absolute;
  left: -100%;
  background: -webkit-linear-gradient(right, #FF7E5F, #FEB47B, #FF7E5F, #FEB47B);
  transition: all 0.4s;
}
.submit-btn .input-data:hover .inner{
  left: 0;
}
.submit-btn .input-data input{
  background: none;
  border: none;
  color: #fff;
  font-size: 17px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 1px;
  cursor: pointer;
  position: relative;
  z-index: 2;
}

.input-data select {
  display: block;
  width: 100%;
  height: 100%;
  border: none;
  font-size: 17px;
  border-bottom: 2px solid rgba(0,0,0, 0.12);
  background: none;
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;
  cursor: pointer;
}

.input-data select:focus ~ label,
.input-data select:valid ~ label {
  transform: translateY(-20px);
  font-size: 14px;
  color: #FF7E5F;
}

.input-data select:focus ~ .underline:before,
.input-data select:valid ~ .underline:before {
  transform: scale(1);
}

.input-data select + .underline {
  position: absolute;
  bottom: 0;
  height: 2px;
  width: 100%;
}

.input-data select + .underline:before {
  position: absolute;
  content: "";
  height: 2px;
  width: 100%;
  background: #FF7E5F;
  transform: scaleX(0);
  transform-origin: center;
  transition: transform 0.3s ease;
}

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
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.modal-content h3 {
  margin-bottom: 15px;
}

.modal-content input {
  width: 100%;
  margin-bottom: 15px;
  padding: 10px;
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 20px; /* Espacio entre botones */
}

.modal-buttons .submit-btn .input-data {
  height: 40px; /* Compacto */
  width: 120px; /* Tamaño ajustado */
  overflow: hidden;
}

.modal-buttons .submit-btn .input-data .inner {
  width: 300%;
  left: -100%;
}

.modal-buttons .submit-btn .input-data:hover .inner {
  left: 0;
}

.modal-buttons .submit-btn .input-data input {
  font-size: 14px; /* Ajustar texto al tamaño */
}


.modal-content h3 {
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  font-family: 'Poppins', sans-serif;
  background: -webkit-linear-gradient(right, #FF7E5F, #FEB47B, #FF7E5F, #FEB47B);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

</style>