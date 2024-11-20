<template>
  <div class="register-container">
    <h1>Registrarse</h1>
    <form @submit.prevent="handleRegister">
      <div class="form-group" v-for="(field, key) in registerData" :key="key">
        <label :for="key">{{ fieldLabels[key] }}</label>
        <input
            :id="key"
            v-model="registerData[key]"
            :type="inputTypes[key]"
            required
            :placeholder="'Ingrese su ' + fieldLabels[key]"
        />
      </div>
      <button type="submit" class="btn-register">Registrarse</button>
    </form>
    <p v-if="error" class="error-message">{{ error }}</p>
    <div class="login-link">
      <p>¿Ya tienes una cuenta?</p>
      <button @click="$router.push('/login')" class="btn-login">
        Iniciar Sesión
      </button>
    </div>
  </div>
</template>

<script>
import api from "@/services/api"; // Importar Axios

export default {
  data() {
    return {
      registerData: {
        username: "",
        password: "",
        email: "",
        nombre: "",
        direccion: "",
        telefono: "",
        rol: "USER", // Valor por defecto
      },
      error: null, // Mensaje de error (opcional)
      fieldLabels: {
        username: "Nombre de Usuario",
        password: "Contraseña",
        email: "Correo Electrónico",
        nombre: "Nombre Completo",
        direccion: "Dirección",
        telefono: "Teléfono",
        rol: "Rol",
      },
      inputTypes: {
        username: "text",
        password: "password",
        email: "email",
        nombre: "text",
        direccion: "text",
        telefono: "text",
        rol: "text",
      },
    };
  },
  methods: {
    async handleRegister() {
      // Validar que no existan campos vacíos
      const isEmptyField = Object.values(this.registerData).some(
          (value) => !value.trim()
      );
      if (isEmptyField) {
        this.error = "Por favor, complete todos los campos.";
        return;
      }

      try {
        this.error = null; // Limpiar errores anteriores
        await api.post("/auth/register", this.registerData); // Llamada al backend

        // Redirigir a la vista de login tras un registro exitoso
        this.$router.push("/login");
      } catch (error) {
        // Manejo de errores (opcional)
        this.error = "Ocurrió un error al registrarse. Intente más tarde.";
      }
    },
  },
};
</script>

<style scoped>
.register-container {
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

.btn-register {
  width: 100%;
  padding: 10px;
  background-color: #a15315;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-register:hover {
  background-color: #86420f;
}

.error-message {
  color: red;
  margin-top: 10px;
  text-align: center;
}

.login-link {
  text-align: center;
  margin-top: 20px;
}

.btn-login {
  background-color: #ccc;
  color: #333;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-login:hover {
  background-color: #aaa;
}
</style>
