'use strict'
const path = require('path')

function resolve(dir) {
    return path.join(__dirname, dir)
}

const CompressionPlugin = require('compression-webpack-plugin')

const name = process.env.VUE_APP_TITLE || '盾安项目管理平台' // 网页标题

const port = process.env.port || process.env.npm_config_port || 80 // 端口

module.exports = {
    publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
    outputDir: 'dist',
    assetsDir: 'static',
    lintOnSave: process.env.NODE_ENV === 'development',
    productionSourceMap: false,
    devServer: {
        host: '0.0.0.0',
        port: port,
        open: true,
        // proxy: {
        //     // detail: https://cli.vuejs.org/config/#devserver-proxy
        //     [process.env.VUE_APP_BASE_API]: {
        //         // target: `http://192.168.0.75:10001/tianbaoWork`,
        //         target: `http://192.168.0.58:8083/tianbaoWork`,
        //         changeOrigin: true,
        //         pathRewrite: {
        //             ['^' + process.env.VUE_APP_BASE_API]: ''
        //         }
        //     }
        // },
        disableHostCheck: true
    },
    css: {
        loaderOptions: {
            sass: {
                sassOptions: { outputStyle: "expanded" }
            }
        }
    },
    configureWebpack: {
        name: name,
        resolve: {
            alias: {
                '@': resolve('src')
            }
        },
        plugins: [
            new CompressionPlugin({
                cache: false, // 不启用文件缓存
                test: /\.(js|css|html)?$/i, // 压缩文件格式
                filename: '[path].gz[query]', // 压缩后的文件名
                algorithm: 'gzip', // 使用gzip压缩
                minRatio: 0.8 // 压缩率小于1才会压缩
            })
        ],
    },
    chainWebpack(config) {
        config.plugins.delete('preload') // TODO: need test
        config.plugins.delete('prefetch') // TODO: need test

        // set svg-sprite-loader
        config.module
            .rule('svg')
            .exclude.add(resolve('src/assets/icons'))
            .end()
        config.module
            .rule('icons')
            .test(/\.svg$/)
            .include.add(resolve('src/assets/icons'))
            .end()
            .use('svg-sprite-loader')
            .loader('svg-sprite-loader')
            .options({
                symbolId: 'icon-[name]'
            })
            .end()

        config.when(process.env.NODE_ENV !== 'development', config => {
            config
                .plugin('ScriptExtHtmlWebpackPlugin')
                .after('html')
                .use('script-ext-html-webpack-plugin', [{
                    // `runtime` must same as runtimeChunk name. default is `runtime`
                    inline: /runtime\..*\.js$/
                }])
                .end()

            config.optimization.splitChunks({
                chunks: 'all',
                cacheGroups: {
                    libs: {
                        name: 'chunk-libs',
                        test: /[\\/]node_modules[\\/]/,
                        priority: 10,
                        chunks: 'initial' // only package third parties that are initially dependent
                    },
                    elementUI: {
                        name: 'chunk-elementUI', // split elementUI into a single package
                        test: /[\\/]node_modules[\\/]_?element-ui(.*)/, // in order to adapt to cnpm
                        priority: 20 // the weight needs to be larger than libs and app or it will be packaged into libs or app
                    },
                    commons: {
                        name: 'chunk-commons',
                        test: resolve('src/components'), // can customize your rules
                        minChunks: 3, //  minimum common number
                        priority: 5,
                        reuseExistingChunk: true
                    }
                }
            })

            config.optimization.runtimeChunk('single'), {
                from: path.resolve(__dirname, './public/robots.txt'), //防爬虫文件
                to: './' //到根目录下
            }
        })
    }
}