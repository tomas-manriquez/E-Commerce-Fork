<script>
import api from "@/services/api";


export default {
  data() {
    return {
      purchase: {
        customerId: Number(localStorage.getItem("userId")),
        compras: [], // Lista de compras dinámicas
      },
      products: [],
    };
  },
  mounted() {
    this.fetchProducts();
  },
  methods: {
    async fetchProducts() {
      const response = await api.get("/api/v1/productos/all");
      this.products = response.data;
    },
    addPurchase() {
      // Agregar una nueva compra a la lista
      this.purchase.compras.push({
        productId: "", // ID del producto seleccionado
        nombreProducto: "", // Nombre del producto (opcional para mostrar)
        cantidad: 0,
        stock: 0, // Stock del producto seleccionado
      });
    },
    removePurchase(index) {
      // Eliminar una compra de la lista
      this.purchase.compras.splice(index, 1);
    },
    updateStock(index) {
      // Actualizar el stock basado en el producto seleccionado
      const selectedProduct = this.products.find(
          (product) => product.idProducto === this.purchase.compras[index].productId
      );
      if (selectedProduct) {
        this.purchase.compras[index].stock = selectedProduct.stock;
        this.purchase.compras[index].nombreProducto = selectedProduct.nombre;
      } else {
        this.purchase.compras[index].stock = 0;
        this.purchase.compras[index].nombreProducto = "";
      }
    },
    async buy() {
      // Preparar los detalles para el envío
      const detalles = this.purchase.compras.map((compra) => ({
        idProducto: compra.productId,
        cantidad: compra.cantidad,
      }));
      const response = await api.post("/api/v1/ordenes/create", {
        idCliente: this.purchase.customerId,
        detalles,
      }, {
        headers: {
          'Accept': 'application/json',
        },
        withCredentials: false,
      });
      console.log("Compra realizada:", response);
      alert("Orden creada exitosamente");
    },
  },
};
</script>

<template>
  <body>
  <div class="container">
    <div class="text">Ingresar Compra</div>
    <form @submit.prevent="buy">
      <div
          v-for="(compra, index) in purchase.compras"
          :key="index"
          class="form-row"
      >
        <!-- Selección del producto -->
        <div class="input-data">
          <select
              v-model="compra.productId"
              @change="updateStock(index)"
              required
          >
            <option disabled value="">Selecciona un producto</option>
            <option
                v-for="product in products"
                :key="product.idProducto"
                :value="product.idProducto"
            >
              {{ product.nombre }} - ${{ product.precio }}
            </option>
          </select>
        </div>
        <!-- Stock -->
        <div class="input-data">
          <input type="number" :value="compra.stock" disabled />
          <div class="underline always-active-underline"></div>
          <label class="always-active">Stock</label>
        </div>
        <!-- Cantidad -->
        <div class="input-data">
          <input
              type="number"
              v-model.number="compra.cantidad"
              :max="compra.stock"
              required
          />
          <div class="underline"></div>
          <label for="cantidad">Cantidad</label>
        </div>
        <!-- Botón de borrar -->
        <div class="erase-button">
          <div class="submit-btn">
            <div class="input-data">
              <div class="inner"></div>
              <input type="button" value="Borrar" @click="removePurchase(index)" />
            </div>
          </div>
        </div>
      </div>
      <!-- Botón para agregar más productos -->
      <div class="add-button">
        <div class="submit-btn">
          <div class="input-data">
            <div class="inner"></div>
            <input type="button" value="Agregar producto" @click="addPurchase" />
          </div>
        </div>
      </div>
      <!-- Botón para enviar la compra -->
      <div class="form-row submit-btn">
        <div class="input-data">
          <div class="inner"></div>
          <input type="submit" value="Comprar" />
        </div>
      </div>
    </form>
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
.input-data input,
.textarea textarea{
  display: block;
  width: 100%;
  height: 100%;
  border: none;
  font-size: 17px;
  border-bottom: 2px solid rgba(0,0,0, 0.12);
}
.input-data input:focus ~ label,
.input-data input:valid ~ label{
  transform: translateY(-20px);
  font-size: 14px;
  color: #FF7E5F;
}
.input-data label{
  position: absolute;
  pointer-events: none;
  bottom: 10px;
  font-size: 16px;
  transition: all 0.3s ease;
  pointer-events: none; /* Para que el label no interfiera con el clic en el input */
}

.input-data .always-active {
  top: -10px;  /* Mueve el label hacia arriba de forma permanente */
  font-size: 12px;
  color: #FF7E5F; /* Cambia el color del label cuando está activo */
}

.input-data .always-active-underline {
  background-color: #FF7E5F; /* El underline de Stock siempre será visible en color activo */
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
  width: auto; /* Ajusta al contenido */
  display: inline-block; /* Asegura que respeta su tamaño */
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
  appearance: none; /* Elimina el estilo por defecto del navegador */
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

.add-button .submit-btn .input-data {
  margin-left: 20px;
  height: 25px; /* Compacto */
  width: 170px; /* Tamaño ajustado */
  overflow: hidden;
  border-radius: 5px;
}

.add-button .submit-btn .input-data .inner {
  width: 300%;
  left: -100%;
}

.add-button .submit-btn .input-data:hover .inner {
  left: 0;
}

.add-button .submit-btn .input-data input {
  font-size: 10px; /* Ajustar texto al tamaño */
  color: #130912;
}

.erase-button .submit-btn .input-data {
  height: 25px; /* Compacto */
  width: 70px; /* Tamaño ajustado */
  overflow: hidden;
  border-radius: 5px;
}

.erase-button .submit-btn .input-data .inner {
  width: 300%;
  left: -100%;
}

.erase-button .submit-btn .input-data:hover .inner {
  left: 0;
}

.erase-button .submit-btn .input-data input {
  font-size: 10px; /* Ajustar texto al tamaño */
}

</style>