/*
 * Copyright 2016-2007 Alfonso Marin Lopez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  
 * 	  http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var debug = process.env.NODE_ENV !== "production";
var webpack = require('webpack');
var path = require('path');
var CopyWebPack = require('copy-webpack-plugin');
var ArchivePlugin = require('webpack-archive-plugin');
var configCopy = [
    {from: 'index.html'},
    {from: 'style', to: 'style'},
    {from: 'fonts', to: 'fonts'}
];
module.exports = {
    context: path.join(__dirname, "src/main"),
    devtool: 'source-map',
    entry: ['whatwg-fetch', 'babel-polyfill',  './js/main.js'],
    module:{
        loaders: [
            {
                test: /\.jsx?$/,
                exclude: /(node_modules|bower_components)/,
                loader: 'babel-loader',
                query: {
                    presets: [ 'es2015', 'stage-1', 'react'],
                    plugins: ['react-html-attrs', 'transform-decorators-legacy', 'transform-class-properties'],
                }
            },
            { test: /\.css$/, loader: "style-loader!css-loader" }
        ]
    },
    devServer: {
        proxy:{
            '/wscommunication' :{
                target: 'ws://localhost:8090/finances/',
                ws: true
            },
            '/api': {
                target: 'http://localhost:8090/finances/',
                secure: false
            },
            '/login': {
                target: 'http://localhost:8090/finances/',
                secure: false
            },
            '/metadata': {
                target: 'http://localhost:8090/finances/',
                secure: false
            },
            '/report': {
                target: 'http://localhost:8090/finances/',
                secure: false
            },
            '/samples': {
                target: 'http://localhost:8090/finances/',
                secure: false
            }
        }
        
        
    },
    output: {
        path:  path.join(__dirname, 'target', 'build'),
        filename: 'bundle.min.js'
    },
    plugins: debug? [] : [
        new CopyWebPack(configCopy),
        new ArchivePlugin({
            output: path.join(__dirname, 'target', 'frontend'),
            format:'zip'
        }),
        new webpack.optimize.DedupePlugin(),
        new webpack.optimize.OccurenceOrderPlugin(),
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                warnings: false
            },
            sourceMap: false
        }),
        new webpack.DefinePlugin({
            'process.env.NODE_ENV': JSON.stringify('production')
        })
    ]
};