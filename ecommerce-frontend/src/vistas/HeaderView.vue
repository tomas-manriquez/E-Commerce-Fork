<template>
  <header class="header">
    <link href="https://fonts.googleapis.com/css2?family=Almarai:wght@400;700&display=swap" rel="stylesheet">
    <h1 class="logo" @click="$router.push('/')">
      <img src="@/images/logo-no-background-no-subtitle.png" alt="logo" class="img">
    </h1>
    <ul class="main-nav">
      <li v-if="tipoUsuario === 'UsuarioCorredor'">
        <router-link to="/publicar-proyecto">Publicar Proyecto</router-link>
      </li>
      <li v-if="tipoUsuario === 'UsuarioParticular' || tipoUsuario === 'UsuarioInmobiliaria'">
        <router-link to="/publicarpropiedad">Publicar Propiedad</router-link>
      </li>
      <li>
        <router-link to="/busquedapropiedades">Buscar</router-link>
      </li>
      <li v-if="isLoggedIn">
        <div class="dropdown">
          <a href="#" @click="toggleDropdown">{{ username }}</a>
          <ul class="dropdown-menu" :class="{ fixed: dropdownFixed }" v-if="dropdownVisible">
            <li>
              <router-link to="/perfil">Mi Perfil</router-link>
            </li>
            <li v-if="tipoUsuario === 'UsuarioParticular' || tipoUsuario === 'UsuarioInmobiliaria'">
              <router-link :to="`/usuario/${idUsuario}/gestionarPublicaciones`">Mis Propiedades</router-link>
            </li>
            <li v-if="tipoUsuario === 'UsuarioCorredor'">
              <router-link to="/">Mis Proyectos</router-link>
            </li>
            <li v-if="tipoUsuario === 'Admin'">
              <router-link to="/admintickets">Revisar Soporte</router-link>
            </li>
            <li v-if="tipoUsuario === 'Admin'">
              <router-link to="/">Revisar Reportes</router-link>
            </li>
            <li><a href="#" @click="cerrarSesion">Cerrar Sesión</a></li>
          </ul>
        </div>
      </li>
      <li v-if="!isLoggedIn">
        <router-link to="/login">Iniciar sesión</router-link>
      </li>
      <li v-if="!isLoggedIn">
        <router-link to="/registro">Registrarse</router-link>
      </li>
    </ul>
  </header>
</template>

<script>

export default {
  data() {
    return {
      isLoggedIn: false,
      username: '',
      tipoUsuario: '',
      idUsuario: '',
      dropdownVisible: false
    }
  },
  mounted() {
    this.isLoggedIn = localStorage.getItem('login') !== null;
    this.username = localStorage.getItem('username');
    this.tipoUsuario = localStorage.getItem('tipoUsuario');
    this.idUsuario = localStorage.getItem('idUsuario');
  },
  methods: {
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible;
    },
    async cerrarSesion() {
      localStorage.removeItem('login');
      localStorage.removeItem('username');
      localStorage.removeItem('idUsuario');
      localStorage.setItem('tipoUsuario', 'guest');
      this.userType = 'guest';
      this.$router.push('/').then(() => {
        window.location.reload();
      });
    }
  }
}
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
