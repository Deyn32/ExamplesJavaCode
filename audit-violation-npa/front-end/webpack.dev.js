'use strict';

const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = function (env) {

    const moduleName = env && env.moduleName ? env.moduleName : 'no_module';
    const template = env && env.template ? env.template : './src/index.html';
    const src = './src/';
    const dest = '../src/main/resources/META-INF/resources/';
    console.log('путь к шаблону = ' + template);

    return {
        // context: path.resolve(__dirname),
        entry: {

            polyfills: src + 'polyfills',
            styles: src + 'styles',
            main: src + 'main'
        },

        output: {
            publicPath: '',
            path: path.resolve(__dirname, dest),
            filename: './modules/' + moduleName + '/js/[name].js'
        },

        resolve: {
            extensions: ['.ts', '.js'],
        },

        module: {
            rules: [
                {
                    test: /\.(ts)$/,
                    include: [
                        path.resolve(__dirname, "node_modules/bis-angular-core/"),
                        path.resolve(__dirname, src),

                    ],
                    use: [
                        {loader: 'angular2-template-loader'},
                        {loader: 'awesome-typescript-loader'}
                    ]
                },
                {
                    test: /\.html$/,
                    loader: 'html-loader'
                },
                {
                    test: /\.css$/,
                    include: path.resolve(__dirname, src + '/modules/'),
                    exclude: path.resolve(__dirname, src + '/modules/core/'),
                    loader: 'raw-loader'
                },
                {
                    test: /\.css$/,
                    include: [
                        path.resolve(__dirname, src + '/styles/'),
                        path.resolve(__dirname, src + '/modules/core/')
                    ],
                    loader: ExtractTextPlugin.extract({
                        fallback: 'style-loader',
                        use: 'css-loader'
                    })
                }
                ,
                {
                    test: /\.(png|jpe?g|gif|ico)$/,
                    use: 'file-loader?name=[name].[ext]&publicPath=../images/&outputPath=modules/' + moduleName +'/images/'
                },
                {
                    test: /\.(woff|woff2|ttf|eot|svg)$/,
                    use: 'file-loader?name=fonts/[name].[ext]'
                }
            ],
            noParse:
                [
                     //'/jquery\/dist\/jquery.js/',
                    '/lodash\/lodash.js/'
                ]
        },

        plugins: [

            // Workaround for angular/angular#11580
            new webpack.ContextReplacementPlugin(
                /angular(\\|\/)core(\\|\/)@angular/,
                path.resolve(__dirname),
                {}
            ),

            new HtmlWebpackPlugin({
                inject:  false,
                template: 'index.ejs',
                filename: './jsp/' + moduleName + '.jsp',
                // Optional
                appMountId: 'app',
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
                        href: './js/audit-violation-npa/favicon.ico',
                        rel: 'icon',
                        type: 'image/x-icon'
                    },
                    'modules/audit-violation-npa/css/core.css'

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

                chunksSortMode: function (chunk1, chunk2) {
                    const orders = ['styles', 'vendor', 'polyfills', 'main'];
                    return orders.indexOf(chunk1.names[0]) - orders.indexOf(chunk2.names[0]);
                }
            }),
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
            //CommonChunksPlugin will now extract all the common modules from vendor and main bundles
            new webpack.optimize.CommonsChunkPlugin({
                name: 'manifest' //But since there are no more common modules between them we end up with just the runtime code included in the manifest file
            }),

            new ExtractTextPlugin('styles.css'),

            new webpack.ProvidePlugin({
                '$': 'jquery',
                'jQuery': 'jquery',
                'window.$': 'jquery',
                'window.jQuery': 'jquery',
                'window._': 'lodash'
            })
        ],

        devtool: 'source-map',
        // cache: false,
        watchOptions: {
            aggregateTimeout: 200,
            ignored: /node_modules/,
            poll: 1000
        }
    };
}

