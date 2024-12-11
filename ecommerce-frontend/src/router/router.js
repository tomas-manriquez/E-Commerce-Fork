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
            path: '/add/ordenes',
            name: 'add/ordenes',
            component: () => import('../vistas/ManejoOrdenView.vue'),
            alias: '/add/Orden',
            meta: { requiresAuth: true }, // Requiere autenticación
        },
        {
            path: '/add/productos',
            name: 'add/productos',
            component: () => import('../vistas/ManejoProductoView.vue'),
            alias: '/add/Productos',
            meta: { requiresAuth: true }, // Requiere autenticación
        },
        {
            path: '/productos/all',
            name: 'productosAll',
            component:() => import('../vistas/ListaProducto.vue'),
            alias: '/ProductosAll',
            meta: { requiresAuth: false },
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
        },{
            path: '/clientes',
            name: 'clientes',
            component: () => import('../vistas/ClientesListView.vue'),
            meta: { requiresAuth: false},
        },{
            path: '/perfil/:id',
            name: 'perfil',
            component: () => import('../vistas/ProfileClienteView.vue'),
            meta: { requiresAuth: true},
        },{
            path: '/perfil/editar/:id',
            name: 'perfil/editar',
            component: () => import('../vistas/EditClienteView.vue'),
            meta: { requiresAuth: true},
        },{
            path: '/ordenes/:id',
            name: '/ordenes',
            component: () => import('../vistas/OrdenesView.vue'),
            meta: { requiresAuth: true},
        },{
            path: '/ordenes/detalle/:id',
            name: '/ordenes/detalle',
            component: () => import('../vistas/DetalleOrdenesView.vue'),
            meta: { requiresAuth: true},
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
