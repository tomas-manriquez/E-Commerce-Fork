<template>
  <div class="login-container">
    <h1>Iniciar Sesión</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Nombre de Usuario</label>
        <input
            id="username"
            type="text"
            v-model="loginData.username"
            required
            placeholder="Ingrese su usuario"
        />
      </div>
      <div class="form-group">
        <label for="password">Contraseña</label>
        <input
            id="password"
            type="password"
            v-model="loginData.password"
            required
            placeholder="Ingrese su contraseña"
        />
      </div>
      <button type="submit" class="btn-login">Iniciar Sesión</button>
    </form>
    <p v-if="error" class="error-message">{{ error }}</p>
    <div class="register-link">
      <p>¿No tienes una cuenta?</p>
      <button @click="$router.push('/registro')" class="btn-register">
        Registrarse
      </button>
    </div>
  </div>
</template>

<script>
import api from "@/services/api"; // Importar la configuración de Axios

export default {
  data() {
    return {
      loginData: {
        username: "",
        password: "",
      },
      error: null, // Mensaje de error
    };
  },
  methods: {
    async handleLogin() {
      try {
        this.error = null; // Limpiar errores anteriores
        console.log("Estos son los datos: ", this.loginData);
        const response = await api.post("/auth/login", this.loginData); // Llamada al backend
        console.log("este es el response: ", response.headers);
        const token = response.headers.authorization; // Obtener el token del header
        const userId = response.data; // Obtener el ID del usuario

        // Guardar datos en localStorage
        localStorage.setItem("token", token);
        localStorage.setItem("userId", userId);

        // Redirigir al HeaderView
        this.$router.push("/");
      } catch (error) {
        // Manejo de errores
        if (error.response && error.response.status === 401) {
          this.error = "Usuario o contraseña incorrectos. Por favor, intente de nuevo.";
        } else {
          this.error = "Ocurrió un error. Por favor, intente más tarde.";
        }
      }
    },
  },
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  font-family: "Almarai", sans-serif;
}

h1 {
  text-align: center;
  color: #a15315;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

input:focus {
  outline: none;
  border-color: #a15315;
}

.btn-login {
  width: 100%;
  padding: 10px;
  background-color: #a15315;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-login:hover {
  background-color: #86420f;
}

.error-message {
  color: red;
  margin-top: 10px;
  text-align: center;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}

.btn-register {
  background-color: #ccc;
  color: #333;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-register:hover {
  background-color: #aaa;
}
</style>
