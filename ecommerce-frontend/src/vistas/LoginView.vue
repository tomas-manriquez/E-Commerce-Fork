<template>
  <body>
    <div class="container">
      <div class="text">Iniciar Sesión</div>
      <form @submit.prevent="handleLogin">
        <div class="form-row">
          <div class="input-data">
            <input
                id="username"
                type="text"
                v-model="loginData.username"
                required
            />
            <div class="underline"></div>
            <label for="username">Nombre de Usuario</label>
          </div>
          <div class="input-data">
            <input
                id="password"
                type="password"
                v-model="loginData.password"
                required
            />
            <div class="underline"></div>
            <label for="password">Contraseña</label>
          </div>
        </div>
        <div class="form-row submit-btn">
          <div class="input-data">
            <div class="inner"></div>
            <input type="submit" value="Iniciar sesión">
          </div>
        </div>
      </form>
      <p v-if="error" class="error-message">{{ error }}</p>
      <div class="register-link">
        <div class="question">¿No tienes una cuenta?</div>
        <button @click="$router.push('/registro')" class="btn-register">
          Registrarse
        </button>
      </div>
    </div>
  </body>
</template>

<script>
import api from "@/services/api"; // Importar la configuración de Axios
import { setToken } from "@/services/store";
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
        const response = await api.post("/auth/login", this.loginData); // Llamada al backend
        const token = response.headers.authorization; // Obtener el token del header
        const userId = response.data.userId; // Obtener el ID del usuario
        const userType = response.data.rol; // Obtener el rol del usuario
        // Guardar datos en localStorage
        setToken(token);
        localStorage.setItem("userId", userId);
        localStorage.setItem("userType", userType);

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

.input-data {
  position: absolute;
  bottom: 0;
  height: 2px;
  width: 100%;
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
.input-data input:valid ~ .underline:before{
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
