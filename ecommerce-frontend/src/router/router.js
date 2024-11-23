import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(process.env.VUE_APP_BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: () => import('../vistas/PrincipalView.vue'),
            alias: '/Home',
            meta: { requiresAuth: false }, // No requiere autenticación
        },
        {
            path: '/clientes',
            name: 'clientes',
            component: () => import('../vistas/ManejoClienteView.vue'),
            alias: '/Cliente',
            meta: { requiresAuth: true }, // Requiere autenticación
        },
        {
            path: '/ordenes',
            name: 'ordenes',
            component: () => import('../vistas/ManejoOrdenView.vue'),
            alias: '/Orden',
            meta: { requiresAuth: true }, // Requiere autenticación
        },
        {
            path: '/productos',
            name: 'productos',
            component: () => import('../vistas/ManejoProductoView.vue'),
            alias: '/Productos',
            meta: { requiresAuth: true }, // Requiere autenticación
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../vistas/LoginView.vue'),
            meta: { requiresAuth: false }, // No requiere autenticación
        },{
            path: '/registro',
            name: 'registro',
            component: () => import('../vistas/RegisterView.vue'),
            meta: { requiresAuth: false}, // No requiere autenticación
        },{
            path: '/top5clientes',
            name: 'top5clientes',
            component: () => import('../vistas/Query48View.vue'),
            meta: { requiresAuth: false},
        }
    ],
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');

    if (to.meta.requiresAuth && !token) {
        next('/login');
    } else {
        next();
    }
});

export default router;
