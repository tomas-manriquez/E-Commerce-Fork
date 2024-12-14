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
import {ref, onMounted, onBeforeUnmount} from "vue";
import api from "@/services/api"; // Asegúrate de que este archivo esté configurado para llamar a tu backend
import proj4 from "proj4";

const mapa = ref(null);
const error = ref(null);


const fetchZonas = async () => {
  try {
    const response = await api.get("/api/v1/zonas/");
    return response.data;
  } catch (err) {
    error.value = "Error al cargar las zonas";
    console.error(err);
    return [];
  }
};

const addZonasToMap = (zonas) => {
  zonas.forEach((zona, index) => { // Usamos `index` para diferenciar las zonas
    try {
      const geojson = JSON.parse(zona.st_asgeojson); // Convierte la cadena a objeto GeoJSON

      // Proj4js para convertir coordenadas
      const projectionFrom = "EPSG:3857"; // Ajusta el sistema de referencia de coordenadas que estés utilizando
      const projectionTo = "EPSG:4326"; // WGS84 (Latitud/Longitud)

      // Convierte las coordenadas de UTM a Lat/Lon
      geojson.coordinates = geojson.coordinates.map((coords) =>
          coords.map(([x, y]) => proj4(projectionFrom, projectionTo, [x, y]))
      );

      console.log("GeoJSON convertido:", geojson); // Verifica que la conversión sea correcta

      // Asignar colores distintos a cada zona basado en el índice (o puedes usar otro atributo)
      const color = getColorForZone(index);

      L.geoJSON(geojson, {
        style: {
          color: color, // Asigna un color diferente a cada zona
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
const getColorForZone = (index) => {
  const colors = ["#fd7f01", "#ac4424", "#ff2600"]; // Rojo, Verde, Azul (agrega más colores si necesitas)
  return colors[index % colors.length]; // Devuelve el color según el índice
};

// Cargar los lugares de entrega
const fetchEntregas = async () => {
  try {
    const response = await api.get("/api/v1/entregas/"); // Asegúrate de que la ruta sea la correcta
    return response.data;
  } catch (err) {
    error.value = "Error al cargar las entregas";
    console.error(err);
    return [];
  }
};

// Agregar los lugares de entrega al mapa
const addEntregasToMap = (entregas) => {
  entregas.forEach((entrega) => {
    try {
      // Extraer lat/lon del objeto lugarentrega
      const { lat, lon } = entrega.lugarentrega;

      // Convertir las coordenadas de EPSG:3857 a EPSG:4326
      const [lonWGS84, latWGS84] = proj4("EPSG:3857", "EPSG:4326", [lon, lat]);

      // Crear un círculo azul en la ubicación de la entrega
      const circle = L.circle([latWGS84, lonWGS84], {
        color: "blue", // Color del borde
        fillColor: "blue", // Color de relleno
        fillOpacity: 0.5, // Opacidad del relleno
        radius: 100, // Radio en metros
      });

      // Agregar un popup con información de la entrega
      circle.bindPopup(
          `<strong>Entrega ID:</strong> ${entrega.idEntrega}<br>
         <strong>Fecha:</strong> ${new Date(entrega.fechaentrega).toLocaleString()}<br>
         <strong>Repartidor ID:</strong> ${entrega.idRepartidor}`
      );

      // Agregar el círculo al mapa
      circle.addTo(mapa.value);
    } catch (error) {
      console.error(`Error procesando la entrega con ID: ${entrega.idEntrega}`, error);
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

  // Cargar zonas y entregas desde el backend y agregarlas al mapa
  const zonas = await fetchZonas();
  const entregas = await fetchEntregas();

  // Agregar zonas y entregas al mapa
  addZonasToMap(zonas);
  addEntregasToMap(entregas);
};

onMounted(() => {
  initializeMap();
});

onBeforeUnmount(() => {
  if (mapa.value) {
    mapa.value.off(); // Remover eventos del mapa
    mapa.value.remove(); // Remover el mapa del DOM
    mapa.value = null; // Limpiar referencia
  }
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
