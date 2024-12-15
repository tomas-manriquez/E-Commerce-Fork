<script>
import api from "@/services/api";
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import proj4 from "proj4";

export default {
  data() {
    return {
      purchase: {
        customerId: Number(localStorage.getItem("userId")),
        compras: [],
        lat: null,
        lon: null,
        address: "",
      },
      products: [],
      storeIds: [], // Almacena los ids de las tiendas seleccionadas
      zones: [], // Almacena las zonas obtenidas
      selectedPoint: null, // Coordenadas seleccionadas como Point
      showMap: false,
      map: null,
      marker: null,
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
      this.purchase.compras.push({
        productId: "",
        nombreProducto: "",
        cantidad: 0,
        stock: 0,
      });
    },
    removePurchase(index) {
      const removedProduct = this.purchase.compras[index];
      if (removedProduct.productId) {
        this.removeStoreId(removedProduct.productId);
      }
      this.purchase.compras.splice(index, 1);
    },
    updateStock(index) {
      const selectedProduct = this.products.find(
          (product) => product.idProducto === this.purchase.compras[index].productId
      );
      if (selectedProduct) {
        this.purchase.compras[index].stock = selectedProduct.stock;
        this.purchase.compras[index].nombreProducto = selectedProduct.nombre;
        this.addStoreId(selectedProduct.idTienda); // Añade el idTienda
      } else {
        this.purchase.compras[index].stock = 0;
        this.purchase.compras[index].nombreProducto = "";
      }
    },
    addStoreId(idTienda) {
      if (!this.storeIds.includes(idTienda)) {
        this.storeIds.push(idTienda);
      }
    },
    removeStoreId(productId) {
      const product = this.products.find((p) => p.idProducto === productId);
      if (product) {
        const idTienda = product.idTienda;
        this.storeIds = this.storeIds.filter((id) => id !== idTienda);
      }
    },
    openMap() {
      this.showMap = true;
      this.$nextTick(() => {
        if (!this.map) {
          this.initializeMap();
        }
      });
    },
    closeMap() {
      if (this.map) {
        this.map.remove(); // Destruye la instancia del mapa
        this.map = null;   // Asegúrate de reiniciar la referencia
      }
      this.showMap = false;
    },
    initializeMap() {
      this.map = L.map("map").setView([-33.4489, -70.6693], 8); // Coordenadas de Santiago, Chile
      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
      }).addTo(this.map);

      this.map.on("click", (e) => {
        const { lat, lng } = e.latlng;

        const projectionFrom = "EPSG:4326"; // WGS84 (Latitud/Longitud)
        const projectionTo = "EPSG:3857";

        // Convierte las coordenadas de UTM a Lat/Lon
        const [x, y] = proj4(projectionFrom, projectionTo, [lng, lat]);

        this.purchase.lat = y;
        this.purchase.lon = x;

        this.selectedPoint = {
          type: "Point",
          coordinates: [x, y],
        };

        const circle = L.circle([lat, lng], {
          color: "blue", // Color del borde
          fillColor: "blue", // Color de relleno
          fillOpacity: 0.5, // Opacidad del relleno
          radius: 100, // Radio en metros
        });
        circle.addTo(this.map);
        alert(`Coordenadas seleccionadas: Lat: ${y}, Lon: ${x}`);
        this.showMap = true; // Cerrar el mapa después de seleccionar
      });
    },
    async validateAndBuy() {
      // Cargar zonas basadas en los ids de tienda seleccionados
      await this.fetchZones();
      const isValid = await this.isPointInZones();
      if (!isValid) {
        alert("Ubicación fuera de la zona de reparto.");
        return;
      }
      await this.buy();
    },
    async fetchZones() {
      const promises = this.storeIds.map((idZona) =>
          api.get(`/api/v1/zonas/${idZona}`)
      );
      const responses = await Promise.all(promises);
      this.zones = responses.map((response) => response.data);
    },
    async isPointInZones() {
      for (const zone of this.zones) {
        const parsedGeoJson = JSON.parse(zone.geojson);
        console.log(parsedGeoJson);
        this.selectedZone = {
          type: parsedGeoJson.type,
          coordinates: parsedGeoJson.coordinates,
        };
        const response = await api.post("/api/v1/zonas/point-in-zona", {
          zona: this.selectedZone,
          point: this.selectedPoint,
        });
        if (response.data) {
          return true;
        }
      }
      return false;
    },
    async buy() {
      const detalles = this.purchase.compras.map((compra) => ({
        idProducto: compra.productId,
        cantidad: compra.cantidad,
      }));
      const response = await api.post(
          "/api/v1/ordenes/create",
          {
            idCliente: this.purchase.customerId,
            detalles,
            coordenadas: {
              lat: this.purchase.lat,
              lon: this.purchase.lon,
            },
          },
          {
            headers: {
              Accept: "application/json",
            },
          }
      );
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
    <form @submit.prevent="validateAndBuy">
      <div v-for="(compra, index) in purchase.compras" :key="index" class="form-row">
        <div class="input-data">
          <select v-model="compra.productId" @change="updateStock(index)" required>
            <option disabled value="">Selecciona un producto</option>
            <option v-for="product in products" :key="product.idProducto" :value="product.idProducto">
              {{ product.nombre }} - ${{ product.precio }}
            </option>
          </select>
        </div>
        <div class="input-data">
          <input type="number" :value="compra.stock" disabled />
          <div class="underline always-active-underline"></div>
          <label class="always-active">Stock</label>
        </div>
        <div class="input-data">
          <input type="number" v-model.number="compra.cantidad" :max="compra.stock" required />
          <div class="underline"></div>
          <label for="cantidad">Cantidad</label>
        </div>
        <div class="erase-button">
          <div class="submit-btn">
            <div class="input-data">
              <div class="inner"></div>
              <input type="button" value="Borrar" @click="removePurchase(index)" />
            </div>
          </div>
        </div>
      </div>
      <div class="add-button">
        <div class="submit-btn">
          <div class="input-data">
            <div class="inner"></div>
            <input type="button" value="Agregar producto" @click="addPurchase" />
          </div>
        </div>
      </div>
      <!-- Dirección -->
      <div class="form-row submit-btn">
        <div class="input-data">
          <div class="inner"></div>
          <input
              type="button"
              v-if="!showMap"
              value="Seleccionar Ubicación"
              @click="openMap"
          />
          <input
              type="button"
              v-else
              value="Cerrar Mapa"
              @click="closeMap"
          />
        </div>
      </div>
      <div v-if="showMap" id="map" style="height: 300px; width: 100%;"></div>
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

#map {
  height: 300px;
  width: 100%;
  margin-top: 10px;
}

</style>