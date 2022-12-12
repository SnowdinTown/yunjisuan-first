import {createApp} from 'vue'
import App from './App.vue'
import {createRouter, createWebHistory} from 'vue-router';
import EchartsPage from "@/components/echarts";

const router = createRouter({
    history: createWebHistory(), //没有history，访问URL会有#
    routes: [
        {
            path: '/spark',
            component: EchartsPage
        },
        {
            path: '/flink',
            component: EchartsPage
        },
    ]
});

createApp(App).use(router).mount('#app')
