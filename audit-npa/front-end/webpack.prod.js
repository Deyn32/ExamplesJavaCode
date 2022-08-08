'use strict';

const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = function (env) {
    const moduleName = env && env.moduleName ? env.moduleName : 'no_module';
    const src = './src/';
    const dest = '../src/main/resources/META-INF/resources/dist';

    return {

        entry: {

            polyfills: src + 'polyfills',
            // vendors: src + 'vendors',
            styles:src + 'styles',
            main: src + 'main'
        },

        output: {
            path: path.resolve(__dirname, dest),
            filename: './modules/' + moduleName + '/js/[name].js'
        },

        resolve: {
            extensions: ['.ts', '.js']
        },

        module: {
            rules: [
                {
                    test: /\.ts$/,
                    exclude: /node_modules/,
                    use: [
                        { loader: 'awesome-typescript-loader' },
                        { loader: 'angular2-template-loader' }
                    ]
                },
                {
                    test: /\.html$/,
                    loader: 'html-loader'
                },
                {
                    test: /\.(png|jpe?g|gif|ico)$/,
                    loader: 'file-loader?name=img/[name].[ext]'
                },
                {
                    test: /\.css$/,
                    include: path.resolve(__dirname, src + '/styles/'),
                    exclude: path.resolve(__dirname, src + '/modules/core/'),
                    loader: 'raw-loader'
                },
                {
                    test: /\.css$/,
                    include: [
                        path.resolve(__dirname, src + '/styles/'),
                        path.resolve(__dirname, src + '/modules/css/')
                    ],
                    loader: ExtractTextPlugin.extract({
                        fallback: 'style-loader',
                        use: 'css-loader'
                    })
                },
                {
                    test: /\.(woff|woff2|ttf|eot|svg)$/,
                    use: 'file-loader?name=fonts/[name].[ext]'
                }
            ]
        },

        plugins: [
            new webpack.NoEmitOnErrorsPlugin(),
            new ExtractTextPlugin( "[name].css" ),
            new webpack.optimize.UglifyJsPlugin({
                beautify: false,
                mangle: {
                    keep_fnames: true
                },
                comments: false
            }),

            // Workaround for angular/angular#11580
            new webpack.ContextReplacementPlugin(
                /angular(\\|\/)core(\\|\/)@angular/,
                path.resolve(__dirname, 'project'),
                {}
            ),

            new webpack.optimize.CommonsChunkPlugin({
                name: 'vendor',
                minChunks: function (module) {
                    if (module.resource && (/^.*\.(css|scss)$/).test(module.resource)) {
                        return false;
                    }
                    // this assumes your vendor imports exist in the node_modules directory
                    return module.context && module.context.indexOf('node_modules') !== -1;
                }
            }),
            /*
             new webpack.optimize.CommonsChunkPlugin({
             name: ['main', 'styles', 'polyfills']
             }),*/

            new HtmlWebpackPlugin({
                inject:  false,
                template: 'index.ejs',
                filename: './' + moduleName + '.jsp',
                // Optional
                appMountId: 'app',

                chunksSortMode: function (chunk1, chunk2) {
                    const orders = ['polyfills', 'styles', 'main'];
                    return orders.indexOf(chunk1.names[0]) - orders.indexOf(chunk2.names[0]);
                },

                meta: [
                    {
                        name: '_csrf',
                        content: '${_csrf.token}'
                    },
                    {
                        name: '_csrf_header',
                        content: '${_csrf.headerName}'
                    },
                    {
                        name: '_csrf_parameter',
                        content: '${_csrf.parameterName}'
                    }
                ],
                mobile: true,
                links: [
                    {
                        href: './js/favicon.ico',
                        rel: 'icon',
                        type: 'image/x-icon'
                    },
                ],
                inlineManifestWebpackName: 'webpackManifest',
                scripts: [
                    {
                        type: 'module'
                    }
                ],
                title: 'Базовая страница',
                jspIsElIgnored: '<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>',
                jspTagLib: '<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>',
                contextPath: '${pageContext.request.contextPath}',
                version: '${version}',
                lastModified: '${lastModified}',
            }),

            new ExtractTextPlugin('cstyles.css'),

            new webpack.DefinePlugin({
                'process.env': {
                    'ENV': JSON.stringify('production')
                }
            })

        ],

        devServer: {
            port: 8888,

            historyApiFallback: {
                index: 'audit-npa.html',
                rewrites: [
                    { from: /\/audit-npa/, to: '/audit-npa.html'}
                ]
            },

            proxy: {
                '/api': 'http://localhost:8080',
                '/audit/spr': 'http://localhost:8080'
            },

            watchOptions: {
                aggregateTimeout: 300,
                poll: 1000
            },

            stats: {
                assets: false,
                colors: true,
                version: false,
                timings: false,
                chunks: false,
                chunkModules: false
            }
        },
        devtool: 'source-map',
        //devtool: true
    };
}