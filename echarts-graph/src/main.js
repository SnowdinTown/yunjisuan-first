import {createApp} from 'vue';
import {createRouter, createWebHashHistory} from 'vue-router';
// import ElementUI from 'element-ui';
import {ElButton} from 'element-plus';
import App from '@/App';
import LabelPage from "@/components/label";
import GraphPage from "@/components/graph";
// import EchartsPage from "@/components/echarts";

const router = createRouter({
    history: createWebHashHistory(), //访问URL会有#
    scrollBehavior: () => ({y: 0}),
    routes: [
        {
            path: '/',
            redirect: '/label'
        },
        {
            path: '/label',
            component: LabelPage,
            // children: [
            //
            // ]
        },
        {
            name: 'graph',
            path: '/label/:item',
            component: GraphPage,
        }

    ]
});

createApp(App).use(ElButton).use(router).mount('#app');