/**
 * Created by dmihaylov on 17.05.2018.
 */
module.exports = (config) => { config.set({
    frameworks: ["jasmine"],
    plugins: [require("karma-jasmine"), require("karma-chrome-launcher")],
    files: [
        //System.js для загрузки основного модуля тестирования
        { pattern: "node_modules/systemjs/dist/system.src.js",included: true, watched: false },
        //Polyfills
        { pattern: "node_modules/core-js/client/shim.js", included: true, watched: false },
        { pattern: "node_modules/reflect-metadata/Reflect.js", included: true, watched: false },
        //Zone.js
        { pattern: "node_modules/zone.js/dist/zone.js", included: true, watched: false },
        { pattern: "node_modules/zone.js/dist/proxy.js",  included: true, watched: false },
        { pattern: "node_modules/zone.js/dist/sync-test.js",  included: true, watched: false },
        { pattern: "node_modules/zone.js/dist/jasmine-patch.js", included: true, watched: false },
        { pattern: "node_modules/zone.js/dist/async-test.js", included: true, watched: false },
        { pattern: "node_modules/zone.js/dist/fake-async-test.js", included: true, watched: false },
        { pattern: "node_modules/rxjs/**/*.js", included: false, watched: false },
        { pattern: "node_modules/@angular/**/*.js", included: false, watched: false },

        { pattern: "src/audit-violation-npa/**/*.js", included: false, watched: true },
        { pattern: "src/audit-violation-npa/**/*.html", included: false, watched: true },
        { pattern: "src/styles/**/*.css", included: false, watched: true },
        { pattern: "tests/*.js", included: false, watched: true },
        { pattern: "karma-test-shim.js", included: true, watched: true },
    ],
    reporters: ["progress"],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ["Chrome"],
    singleRun: false
})}