import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(process.env.VUE_APP_BASE_URL),
    routes:[
        {
            path: '/',
            name: "home",
            component: () => import('../vistas/PrincipalView.vue'),
            alias: '/Home'
        },
        {
            path: '/clientes',
            name: "clientes",
            component: () => import('../vistas/ManejoClienteView.vue'),
            alias: '/Cliente'
        },
        {
            path: '/ordenes',
            name: "ordenes",
            component: () => import('../vistas/ManejoOrdenView.vue'),
            alias: '/Orden'
        },
        {
            path: '/productos',
            name: "productos",
            component: () => import('../vistas/ManejoProductoView.vue'),
            alias: '/Productos'
        }
    ]
});


export default router;