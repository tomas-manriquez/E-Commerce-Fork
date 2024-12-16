<template>
  <div class="main-container">
    <header class="header">
      <h1>Mapeo de la tienda</h1>
    </header>
    <div class="text-center">
      <p v-if="tamanioZona === 0">Por favor espere, datos cargando...</p>
      <p v-else>Tamaño de la zona = {{ tamanioZona }} grados cuadrados</p>
    </div>
    <main class="content">
      <div id="map" style="height: 500px; margin-top: 20px;"></div>
    </main>
    <div class="secondary-section" v-if="userType === 'ADMIN'">
      <router-link :to="`/tienda/${idTienda}/${idZona}/repartidores`" class="btn">
        Ver los repartidores de esta zona
      </router-link>
    </div>
  </div>

</template>

<script setup>
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import { ref, onMounted } from "vue";
import api from "@/services/api"; // Asegúrate de que este archivo esté configurado para llamar a tu backend
import proj4 from "proj4";
import { useRoute } from "vue-router";

const mapa = ref(null);
// const nombreTienda = ref(null);
const idZona = ref(0);
const idTienda = ref(0);
let tamanioZona = ref(0);
const error = ref(null);
const route = useRoute();
const userType = localStorage.getItem('userType');

const fetchZonas = async (idtienda) => {
  try {
    const response = await api.get(`/api/v1/zonas/tienda/${idtienda}`);
    console.log(response.data);
    return response.data;
  } catch (err) {
    error.value = "Error al carga las zonas";
    console.error(err);
    return [];
  }
};

const fetchTamanioZona = async (idZona) => {
  try{
    console.log("idZona: ",idZona.value);
    const response2 = await api.get(`/api/v1/zonas/areaZona/${idZona.value}`);
    console.log("tamaño zona: ",response2.data);
    tamanioZona.value = response2.data;
    tamanioZona.value = tamanioZona.value.toFixed(2);
  }catch (err) {
    error.value = "Error al cargar el area de la zona";
    console.error(err);
    return [];
  }
}

const addZonasToMap = (zonas) => {
  zonas.forEach((zona) => {
    try {
      const geojson = JSON.parse(zona.geojson); // Convierte la cadena a objeto GeoJSON

      // Proj4js para convertir coordenadas
      const projectionFrom = "EPSG:3857";
      const projectionTo = "EPSG:4326"; // WGS84 (Latitud/Longitud)

      // Convierte las coordenadas de UTM a Lat/Lon
      geojson.coordinates = geojson.coordinates.map((coords) => {
        return coords.map(([x, y]) => {
          const converted = proj4(projectionFrom, projectionTo, [x, y]);
          return converted;
        });
      });

      L.geoJSON(geojson, {
        style: {
          color: "blue",
          weight: 2,
          opacity: 0.8,
        },
        onEachFeature: (feature, layer) => {
          layer.bindPopup(`Zona: ${zona.nombrezona}`);
        },
      }).addTo(mapa.value); // Agrega la capa al mapa
    } catch (error) {
      console.error(`Error procesando la zona: ${zona.nombrezona}`, error);
    }
  });
};

const initializeMap = async (idtienda) => {
  // Inicializar el mapa
  mapa.value = L.map("map").setView([-33.4489, -70.6693], 8); // Coordenadas de Santiago, Chile

  // Capa base del mapa
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution: "© OpenStreetMap contributors",
  }).addTo(mapa.value);

  // Cargar zonas desde el backend y agregarlas al mapa
  const zonas = await fetchZonas(idtienda);
  await fetchTamanioZona(idZona);
  console.log(zonas);
  // Agregar zonas al mapa
  addZonasToMap(zonas);

};

// const fetchTienda = async (idtienda) => {
//   try {
//     const response = await api.get(`/api/v1/tiendas/byId/${idtienda}`);
//     nombreTienda.value = response.data.nombre;
//
//   } catch (err) {
//     error.value = "Error al hacer fetch de la tienda";
//     console.error(err);
//   }
// }

onMounted(() => {
  const { idtienda, idzona } = route.params; // Desestructurar los parámetros de la ruta
  idTienda.value = Number(idtienda); // Asignación correcta a las referencias
  idZona.value = Number(idzona);
  console.log("idzona enviado desde el frontend:", idzona);
  console.log("idtienda enviado desde el frontend:", idtienda);
  initializeMap(Number(idtienda));
  // fetchTienda(idtienda);
});
</script>

<style scoped>
.text-center {
  display: flex;            /* Activa flexbox */
  justify-content: center;  /* Centra horizontalmente */
  align-items: center;      /* Centra verticalmente */
  text-align: center;       /* Asegura que el texto esté alineado al centro */
  height: 50px;             /* Altura del contenedor */
  margin-top: 10px;         /* Margen superior */
  font-size: 26px;          /* Tamaño de fuente */
  font-weight: bold;        /* Opcional: texto en negrita */
}

html, body {
  height: 100%; /* Asegúrate de que el html y body ocupen toda la altura */
  margin: 0; /* Elimina cualquier margen por defecto */
}

.main-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  height: 100%;
}

.secondary-section {
  background: linear-gradient(115deg, #FF7E5F 10%, #FEB47B 90%);
  background-size: cover;
  background-position: center;
  align-items: center;
  height: 20%;
  position: relative;
}

.header {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-bottom: 1px solid #ddd;
}

.content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

#map {
  height: 100%;
  width: 100%;
  justify-content: stretch;
}
.btn {
  display: inline-block;
  padding: 10px 20px;
  background-color: #ff7300;
  color: white;
  border: none;
  text-decoration: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn:hover {
  background-color: #c85d00;
}
</style>
