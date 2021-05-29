const path = require('path');

module.exports = {
    mode: 'development',
    entry: './src/index.tsx',
    output: {
        filename: 'main.js',
        path: path.resolve(__dirname, 'dist'),
    },
    devServer: {
        contentBase: './dist',
        historyApiFallback: true,
        proxy: {
            '/api': {
                target: 'http://localhost:8000',
                pathRewrite: { '^/api': '/api'},
                changeOrigin: true,
                onProxyRes(proxyRes, req, res) {
                    delete proxyRes.headers['www-authenticate']
                }
            }
        }
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js'],
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
        ],
    },
}