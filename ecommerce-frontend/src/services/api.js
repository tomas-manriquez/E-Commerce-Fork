import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8090', // Dirección del backend
});

// **Interceptor de solicitudes**
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token'); // Obtiene el token del almacenamiento local
    if (token) {
        config.headers.Authorization = `Bearer ${token}`; // Agrega el token al encabezado
    }
    return config;
});

// **Interceptor de respuestas**
api.interceptors.response.use(
    (response) => response, // Respuesta exitosa, no hacemos nada
    (error) => {
        if (error.response && error.response.status === 401) {
            // Si el backend devuelve un error 401 (Token inválido o expirado)
            localStorage.removeItem('token'); // Borra el token del almacenamiento local
            window.location.href = '/login'; // Redirige al login
        }
        return Promise.reject(error); // Rechaza el error para manejarlo en el componente si es necesario
    }
);

export default api;
