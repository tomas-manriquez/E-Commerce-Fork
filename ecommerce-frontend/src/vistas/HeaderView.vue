<template>
  <header class="header">
    <h1 class="logo" @click="$router.push('/')">
      <img src="@/images/logo-no-background-no-subtitle.png" alt="logo" class="img" />
    </h1>
    <ul class="main-nav">
      <!-- Si el usuario está autenticado -->
      <template v-if="isLoggedIn">
        <li>
          <router-link to="/top5clientes">Top 5 Clientes</router-link>
        </li>
        <li>
          <router-link to="/productos/all">Ver Productos</router-link>
        </li>
        <li>
          <router-link :to="`/ordenes/${clienteId}`">Mis ordenes</router-link>
        </li>
        <li v-if="userType === 'ADMIN'">
          <router-link to="/clientes">Ver lista de Clientes</router-link>
        </li>
        <li>
          <div class="dropdown">
            <a href="#" @click="toggleDropdown">Perfil</a>
            <ul class="dropdown-menu" v-if="dropdownVisible">
              <li>
                <router-link :to="`/perfil/${clienteId}`">Mi Perfil</router-link>
              </li>
              <li>
                <a href="#" @click="cerrarSesion">Cerrar Sesión</a>
              </li>
            </ul>
          </div>
        </li>
      </template>
      <!-- Si el usuario no está autenticado -->
      <template v-else>
        <li>
          <router-link to="/login">Iniciar sesión</router-link>
        </li>
        <li>
          <router-link to="/registro">Registrarse</router-link>
        </li>
      </template>
    </ul>
  </header>
</template>

<script>
import {token, setToken} from "@/services/store";

export default {
  data() {
    return {
      dropdownVisible: false, // Controla la visibilidad del menú desplegable
      userType: '',
      clienteId: Number(localStorage.getItem("userId"))
    };
  },
  computed: {
    isLoggedIn() {
      return token.value !== null; // Reactivo porque usamos `ref`
    },
  },
  watch: {
    isLoggedIn() {
      // Reinicia el estado del dropdown al cambiar el estado de autenticación
      this.dropdownVisible = false;
    },
  },
  methods: {
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible;
    },
    cerrarSesion() {
      setToken(null); // Actualiza el token y elimina de localStorage
      this.$router.push("/"); // Redirige al inicio
    },
  },
  mounted() {
    this.userType = localStorage.getItem('userType');
  },
};
</script>

<style scoped>

* {
  box-sizing: border-box;
}

body {
  line-height: 1.6;
  margin: 0;
  min-height: 100vh;
}

ul {
  margin: 0;
  padding: 0;
  list-style: none;
}


h2,
h3,
a {
  color: #a15315;
}

a {
  text-decoration: none;
}

.logo {
  margin: 0;
  font-size: 1.45em;
}

.main-nav {
  margin-top: 5px;

}

.logo {
  cursor: pointer;
}

.logo img {
  height: 50px;
  padding: 15px 15px 5px;
}

.main-nav a {
  padding: 10px 15px;
  text-transform: uppercase;
  text-align: center;
  display: block;
}

.main-nav a {
  color: #130912;
  font-size: .99em;
}

.main-nav a:hover {
  color: #a15315;
}

html {
  zoom: 100%;
}

.header {
  font-family: 'Almarai', sans-serif;
  font-weight: 700;
  font-size: 18px;
  padding-top: .5em;
  padding-bottom: .5em;
  border: 1px solid #a2a2a2;
  background-color: #f4f4f4;
  -webkit-box-shadow: 0px 0px 14px 0px rgba(0, 0, 0, 0.75);
  -moz-box-shadow: 0px 0px 14px 0px rgba(0, 0, 0, 0.75);
  box-shadow: 0px 0px 14px 0px rgba(0, 0, 0, 0.75);
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  border-radius: 5px;
}

@media (min-width: 769px) {
  .header,
  .main-nav {
    display: flex;
  }

  .header {
    flex-direction: column;
    align-items: center;

    .header {
      width: 80%;
      margin: 0 auto;
      max-width: 1150px;
    }
  }

}

@media (min-width: 1025px) {
  .header {
    flex-direction: row;
    justify-content: space-between;
  }
}

.dropdown {
  position: relative;
}

.dropdown-menu {
  display: none;
  position: absolute;
  top: 140%;
  right: 0;
  width: auto;
  min-width: 160px;
  white-space: nowrap;
  background-color: #f4f4f4;
  border: 1px solid #ccc;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.15);
  z-index: 99999;
}

.dropdown .dropdown-menu {
  display: block;
}

.img {
  height: 200px;
  width: 200px;
  margin-left: 20px;
}
</style>
