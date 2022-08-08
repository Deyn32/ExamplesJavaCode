const prod = process.argv.indexOf('-p') !== -1;

if( prod ) {
    module.exports = require('./webpack.prod');
}
else {
    module.exports = require('./webpack.dev');
}