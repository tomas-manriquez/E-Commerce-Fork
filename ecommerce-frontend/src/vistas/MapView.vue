<template>
  <div class="main-container">
    <header class="header">
      <h1>Mapeo de Zonas</h1>
    </header>

    <main class="content">
      <div id="map" style="height: 500px; margin-top: 20px;"></div>
    </main>
  </div>
</template>

<script setup>
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import { ref, onMounted } from "vue";
import api from "@/services/api"; // Asegúrate de que este archivo esté configurado para llamar a tu backend
import proj4 from "proj4";

const mapa = ref(null);
const error = ref(null);


const fetchZonas = async () => {
  try {
    const response = await api.get("/api/v1/zonas/mapeo-zonas");
    return response.data; // Asegúrate de que aquí llegan los datos en formato correcto
  } catch (err) {
    error.value = "Error al cargar las zonas";
    console.error(err);
    return [];
  }
};

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

const initializeMap = async () => {
  // Inicializar el mapa
  mapa.value = L.map("map").setView([-33.4489, -70.6693], 8); // Coordenadas de Santiago, Chile

  // Capa base del mapa
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution: "© OpenStreetMap contributors",
  }).addTo(mapa.value);

  // Cargar zonas desde el backend y agregarlas al mapa
  const zonas = await fetchZonas();

  // Agregar zonas al mapa
  addZonasToMap(zonas);
};

onMounted(() => {
  initializeMap();
});
</script>

<style scoped>
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

</style>
