const {defineConfig} = require('@vue/cli-service')
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin')
module.exports = defineConfig({
    transpileDependencies: true,
    configureWebpack: {
        plugins: [new NodePolyfillPlugin()],
        resolve: {
            fallback: {
                fs: false
            }
        }
    },
    devServer: {
        host: '0.0.0.0',
        port: 8080,
        client: {
            webSocketURL: 'ws://0.0.0.0:8080/ws',
        },
        headers: {
            'Access-Control-Allow-Origin': '*',
        }
    }
})
