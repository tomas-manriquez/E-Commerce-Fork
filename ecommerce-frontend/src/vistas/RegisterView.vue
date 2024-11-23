<template>
  <body>
    <div class="container">
      <div class="text">Registrarse</div>
      <form @submit.prevent="handleRegister">
        <div class="form-row" v-for="(chunk, index) in chunkedRegisterData" :key="index">
          <div class="input-data" v-for="(field, key) in chunk" :key="key">
            <input :type="inputTypes[key]" :id="key" v-model="registerData[key]" required />
            <div class="underline"></div>
            <label :for="key">{{ fieldLabels[key] }}</label>
          </div>
        </div>
        <div class="form-row submit-btn">
          <div class="input-data">
            <div class="inner"></div>
            <input type="submit" value="Registrarse">
          </div>
        </div>
      </form>
      <p v-if="error" class="error-message">{{ error }}</p>
      <div class="login-link">
        <div class="question">¿Ya tienes una cuenta?</div>
          <button @click="$router.push('/login')" class="btn-login">
            Iniciar Sesión
          </button>
      </div>
    </div>
  </body>
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
  computed: {
    chunkedRegisterData() {
      const entries = Object.entries(this.registerData); // Convertir el objeto a un array de pares [key, value]
      const chunks = [];
      for (let i = 0; i < entries.length; i += 2) {
        chunks.push(Object.fromEntries(entries.slice(i, i + 2))); // Dividir en grupos de dos
      }
      return chunks;
    },
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
* {
  margin: 0;
  padding: 0;
  outline: none;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
}

body {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 10px;
  font-family: 'Poppins', sans-serif;
  background: linear-gradient(115deg, #FF7E5F 10%, #FEB47B 90%);
}

.container {
  max-width: 800px;
  background: #fff;
  width: 100%;
  padding: 25px 40px 10px 40px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.container .text {
  text-align: center;
  font-size: 41px;
  font-weight: 600;
  font-family: 'Poppins', sans-serif;
  background: -webkit-linear-gradient(right, #FF7E5F, #FEB47B, #FF7E5F, #FEB47B);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.container form {
  padding: 30px 0 0 0;
}

.container form .form-row {
  display: flex;
  justify-content: space-between;
  margin: 32px 0;
  flex-wrap: wrap;
}

form .form-row .input-data {
  width: calc(50% - 10px);
  height: 40px;
  position: relative;
  margin-bottom: 20px;
}

form .form-row .input-data:last-child {
  margin-right: 0;
}

.question {
  margin-bottom: 5px;
}

.input-data input {
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

.input-data label {
  position: absolute;
  pointer-events: none;
  bottom: 10px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.input-data .underline {
  position: absolute;
  bottom: 0;
  height: 2px;
  width: 100%;
}

.input-data .underline:before {
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
.input-data input:valid ~ .underline:before {
  transform: scale(1);
}

.submit-btn .input-data {
  position: relative;
  overflow: hidden;
  height: 45px;
  width: auto;
  display: inline-block;
}

.submit-btn .input-data .inner {
  height: 100%;
  width: 300%;
  position: absolute;
  left: -100%;
  background: -webkit-linear-gradient(right, #FF7E5F, #FEB47B, #FF7E5F, #FEB47B);
  transition: all 0.4s;
}

.submit-btn .input-data:hover .inner {
  left: 0;
}

.submit-btn .input-data input {
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

.error-message {
  color: red;
  margin-top: 10px;
  text-align: center;
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

/* Media Query para pantallas pequeñas */
@media (max-width: 768px) {
  .form-row .input-data {
    width: 100%;
    margin-bottom: 15px;
  }

  .container {
    padding: 25px;
  }
}
</style>
