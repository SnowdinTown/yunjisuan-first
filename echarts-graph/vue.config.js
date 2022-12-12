const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        // host: 'localhost',
        port: 8083,
        // client: {
        //     webSocketURL: 'ws://localhost:8083/ws',
        // },
        // headers: {
        //     'Access-Control-Allow-Origin': '*',
        // }
    },
    publicPath: "./"
})
