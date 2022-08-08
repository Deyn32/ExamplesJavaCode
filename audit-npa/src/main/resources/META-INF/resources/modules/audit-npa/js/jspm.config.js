SystemJS.config({
    paths: {
        "github:": "js/audit-npa/jspm_packages/github/",
        "npm:": "js/audit-npa/jspm_packages/npm/",
        "npm:@": "js/audit-npa/jspm_packages/npm/",
        "gitoffice:": "js/audit-npa/jspm_packages/gitoffice/",
        "app/": "js/audit-npa/"
    },
    devConfig: {
        "map": {
            "plugin-typescript": "github:frankwallis/plugin-typescript@4.0.16/plugin.js"
        }
    },
    transpiler: "plugin-typescript",
    babelOptions: {},
    typescriptOptions: {
        "module": "system",
        "noImplicitAny": false,
        "experimentalDecorators": true,
        "emitDecoratorMetadata": true,
        "inlineSourceMap": true,
        "traceResolution": true,
        "diagnostics": true
    },
    packages: {
        "app": {
            "defaultExtension": "js",
            "main": "main.js",
            "meta": {
                "*.ts": {
                    "loader": "plugin-typescript"
                }
            }
        },
        "jspm_packages": {
            "defaultExtension": "js",
            "meta": {
                "*.ts": {
                    "loader": "plugin-typescript"
                }
            }
        },
        "github:twbs/bootstrap@3.3.6": {
            "map": {
                "jquery": "npm:jquery@2.2.4"
            }
        },
        "github:frankwallis/plugin-typescript@4.0.16": {
            "map": {
                "typescript": "npm:typescript@1.8.10"
            }
        }
    },
    map: {
        "gwt-events-helper": "js/utils/GwtEventsHelper.js",
        "underscore": "npm:lodash@4.17.4",
        "moment-locale-ru": "npm:moment@2.16.0/locale/ru.js",
        "module": "npm:jspm-nodelibs-module@0.2.0",
        "bootstrap": "github:twbs/bootstrap@3.3.6",
        "ts": "github:frankwallis/plugin-typescript@4.0.16"
    },
    meta: {
        "moment-locale-ru": {
            "deps": [
                "moment"
            ]
        },
        "underscore": {
            "format": "cjs"
        }
    }
});

SystemJS.config({
    packageConfigPaths: [
        "github:*/*.json",
        "npm:@*/*.json",
        "npm:*.json",
        "gitoffice:*/*.json"
    ],
    map: {
        "typescript": "npm:typescript@1.8.10",
        "@angular/upgrade": "npm:@angular/upgrade@2.4.6",
        "angular": "npm:angular@1.6.4",
        "angular-activerecord": "npm:angular-activerecord@0.3.0",
        "angular-animate": "npm:angular-animate@1.6.4",
        "angular-bootstrap-grid-tree": "npm:angular-bootstrap-grid-tree@0.4.0",
        "angular-busy": "npm:angular-busy@4.1.4",
        "angular-cookies": "npm:angular-cookies@1.6.4",
        "angular-growl-notifications": "npm:angular-growl-notifications@2.6.0",
        "angular-i18n": "npm:angular-i18n@1.6.4",
        "angular-loading-bar": "npm:angular-loading-bar@0.9.0",
        "angular-material": "npm:angular-material@1.1.3",
        "angular-motion": "npm:angular-motion@0.4.4",
        "angular-nvd3": "npm:angular-nvd3@1.0.9",
        "angular-sanitize": "npm:angular-sanitize@1.6.4",
        "angular-strap": "npm:angular-strap@2.3.12",
        "angular-touch": "npm:angular-touch@1.6.4",
        "angular-ui-grid": "npm:angular-ui-grid@4.0.4",
        "angular-ui-router": "npm:angular-ui-router@0.4.2",
        "bcrypt-pbkdf": "npm:bcrypt-pbkdf@1.0.1",
        "d3": "npm:d3@3.5.17",
        "danialfarid/ng-file-upload-shim-bower": "github:danialfarid/ng-file-upload-shim-bower@12.0.1",
        "dgram": "npm:jspm-nodelibs-dgram@0.2.1",
        "dns": "npm:jspm-nodelibs-dns@0.2.1",
        "ecc-jsbn": "npm:ecc-jsbn@0.1.1",
        "eligrey/FileSaver.js": "github:eligrey/FileSaver.js@1.3.3",
        "http": "npm:jspm-nodelibs-http@0.2.0",
        "https": "npm:jspm-nodelibs-https@0.2.2",
        "jodid25519": "npm:jodid25519@1.0.2",
        "jsbn": "npm:jsbn@0.1.1",
        "jspm-nodelibs-os": "npm:jspm-nodelibs-os@0.2.0",
        "ng-file-upload": "npm:ng-file-upload@12.2.13",
        "nvd3": "npm:nvd3@1.8.5",
        "plugin-babel": "npm:systemjs-plugin-babel@0.0.12",
        "querystring": "npm:jspm-nodelibs-querystring@0.2.2",
        "requirejs": "npm:requirejs@2.1.20",
        "restangular": "npm:restangular@1.6.1",
        "tls": "npm:jspm-nodelibs-tls@0.2.1",
        "tty": "npm:jspm-nodelibs-tty@0.2.1",
        "twbs/bootstrap": "github:twbs/bootstrap@3.3.7",
        "tweetnacl": "npm:tweetnacl@0.14.5",
        "ui-router-ng1-to-ng2": "npm:ui-router-ng1-to-ng2@1.0.6",
        "ui-router-ng2": "npm:ui-router-ng2@1.0.0-alpha.5",
        "ui-select": "npm:ui-select@0.19.6",
        "net": "npm:jspm-nodelibs-net@0.2.0",
        "@angular/router": "npm:@angular/router@3.4.6",
        "@angular/http": "npm:@angular/http@2.4.6",
        "@angular/platform-browser-dynamic": "npm:@angular/platform-browser-dynamic@2.4.6",
        "@angular/common": "npm:@angular/common@2.4.6",
        "@angular/compiler": "npm:@angular/compiler@2.4.6",
        "@angular/core": "npm:@angular/core@2.4.6",
        "@angular/platform-browser": "npm:@angular/platform-browser@2.4.6",
        "@angular/forms": "npm:@angular/forms@2.4.6",
        "assert": "npm:jspm-nodelibs-assert@0.2.0",
        "buffer": "npm:jspm-nodelibs-buffer@0.2.0",
        "constants": "npm:jspm-nodelibs-constants@0.2.0",
        "crypto": "npm:jspm-nodelibs-crypto@0.2.0",
        "es6-shim": "github:es-shims/es6-shim@0.35.1",
        "events": "npm:jspm-nodelibs-events@0.2.0",
        "os": "npm:jspm-nodelibs-os@0.2.0",
        "primeng": "npm:primeng@2.0.0",
        "stream": "npm:jspm-nodelibs-stream@0.2.0",
        "string_decoder": "npm:jspm-nodelibs-string_decoder@0.2.0",
        "timers": "npm:jspm-nodelibs-timers@0.2.0",
        "font-awesome": "npm:font-awesome@4.7.0",
        "child_process": "npm:jspm-nodelibs-child_process@0.2.0",
        "path": "npm:jspm-nodelibs-path@0.2.1",
        "fs": "npm:jspm-nodelibs-fs@0.2.0",
        "lodash": "npm:lodash@4.17.4",
        "moment": "npm:moment@2.16.0",
        "jquery": "npm:jquery@2.2.4",
        "process": "npm:jspm-nodelibs-process@0.2.0",
        "reflect-metadata": "npm:reflect-metadata@0.1.3",
        "rxjs": "npm:rxjs@5.1.0",
        "url": "npm:jspm-nodelibs-url@0.2.1",
        "util": "npm:jspm-nodelibs-util@0.2.0",
        "vm": "npm:jspm-nodelibs-vm@0.2.0",
        "zlib": "npm:jspm-nodelibs-zlib@0.2.3",
        "zone.js": "npm:zone.js@0.7.6"
    },
    packages: {
        "npm:typescript@1.8.10": {
            "map": {
                "source-map-support": "npm:source-map-support@0.4.9"
            }
        },
        "npm:source-map-support@0.4.9": {
            "map": {
                "source-map": "npm:source-map@0.5.6"
            }
        },
        "npm:font-awesome@4.7.0": {
            "map": {
                "css": "github:systemjs/plugin-css@0.1.23"
            }
        },
        "npm:crypto-browserify@3.11.0": {
            "map": {
                "create-ecdh": "npm:create-ecdh@4.0.0",
                "create-hmac": "npm:create-hmac@1.1.4",
                "create-hash": "npm:create-hash@1.1.2",
                "browserify-cipher": "npm:browserify-cipher@1.0.0",
                "inherits": "npm:inherits@2.0.3",
                "pbkdf2": "npm:pbkdf2@3.0.9",
                "browserify-sign": "npm:browserify-sign@4.0.0",
                "public-encrypt": "npm:public-encrypt@4.0.0",
                "diffie-hellman": "npm:diffie-hellman@5.0.2",
                "randombytes": "npm:randombytes@2.0.3"
            }
        },
        "npm:create-hmac@1.1.4": {
            "map": {
                "inherits": "npm:inherits@2.0.3",
                "create-hash": "npm:create-hash@1.1.2"
            }
        },
        "npm:create-hash@1.1.2": {
            "map": {
                "inherits": "npm:inherits@2.0.3",
                "ripemd160": "npm:ripemd160@1.0.1",
                "sha.js": "npm:sha.js@2.4.8",
                "cipher-base": "npm:cipher-base@1.0.3"
            }
        },
        "npm:timers-browserify@1.4.2": {
            "map": {
                "process": "npm:process@0.11.9"
            }
        },
        "npm:browserify-sign@4.0.0": {
            "map": {
                "create-hash": "npm:create-hash@1.1.2",
                "create-hmac": "npm:create-hmac@1.1.4",
                "inherits": "npm:inherits@2.0.3",
                "elliptic": "npm:elliptic@6.3.2",
                "browserify-rsa": "npm:browserify-rsa@4.0.1",
                "bn.js": "npm:bn.js@4.11.6",
                "parse-asn1": "npm:parse-asn1@5.0.0"
            }
        },
        "npm:public-encrypt@4.0.0": {
            "map": {
                "create-hash": "npm:create-hash@1.1.2",
                "randombytes": "npm:randombytes@2.0.3",
                "browserify-rsa": "npm:browserify-rsa@4.0.1",
                "bn.js": "npm:bn.js@4.11.6",
                "parse-asn1": "npm:parse-asn1@5.0.0"
            }
        },
        "npm:diffie-hellman@5.0.2": {
            "map": {
                "randombytes": "npm:randombytes@2.0.3",
                "bn.js": "npm:bn.js@4.11.6",
                "miller-rabin": "npm:miller-rabin@4.0.0"
            }
        },
        "npm:create-ecdh@4.0.0": {
            "map": {
                "elliptic": "npm:elliptic@6.3.2",
                "bn.js": "npm:bn.js@4.11.6"
            }
        },
        "npm:browserify-cipher@1.0.0": {
            "map": {
                "browserify-des": "npm:browserify-des@1.0.0",
                "evp_bytestokey": "npm:evp_bytestokey@1.0.0",
                "browserify-aes": "npm:browserify-aes@1.0.6"
            }
        },
        "npm:elliptic@6.3.2": {
            "map": {
                "inherits": "npm:inherits@2.0.3",
                "bn.js": "npm:bn.js@4.11.6",
                "brorand": "npm:brorand@1.0.6",
                "hash.js": "npm:hash.js@1.0.3"
            }
        },
        "npm:cipher-base@1.0.3": {
            "map": {
                "inherits": "npm:inherits@2.0.3"
            }
        },
        "npm:browserify-des@1.0.0": {
            "map": {
                "cipher-base": "npm:cipher-base@1.0.3",
                "inherits": "npm:inherits@2.0.3",
                "des.js": "npm:des.js@1.0.0"
            }
        },
        "npm:evp_bytestokey@1.0.0": {
            "map": {
                "create-hash": "npm:create-hash@1.1.2"
            }
        },
        "npm:browserify-rsa@4.0.1": {
            "map": {
                "bn.js": "npm:bn.js@4.11.6",
                "randombytes": "npm:randombytes@2.0.3"
            }
        },
        "npm:browserify-aes@1.0.6": {
            "map": {
                "cipher-base": "npm:cipher-base@1.0.3",
                "create-hash": "npm:create-hash@1.1.2",
                "evp_bytestokey": "npm:evp_bytestokey@1.0.0",
                "inherits": "npm:inherits@2.0.3",
                "buffer-xor": "npm:buffer-xor@1.0.3"
            }
        },
        "npm:parse-asn1@5.0.0": {
            "map": {
                "browserify-aes": "npm:browserify-aes@1.0.6",
                "create-hash": "npm:create-hash@1.1.2",
                "evp_bytestokey": "npm:evp_bytestokey@1.0.0",
                "pbkdf2": "npm:pbkdf2@3.0.9",
                "asn1.js": "npm:asn1.js@4.9.1"
            }
        },
        "npm:miller-rabin@4.0.0": {
            "map": {
                "bn.js": "npm:bn.js@4.11.6",
                "brorand": "npm:brorand@1.0.6"
            }
        },
        "npm:hash.js@1.0.3": {
            "map": {
                "inherits": "npm:inherits@2.0.3"
            }
        },
        "npm:des.js@1.0.0": {
            "map": {
                "inherits": "npm:inherits@2.0.3",
                "minimalistic-assert": "npm:minimalistic-assert@1.0.0"
            }
        },
        "npm:stream-browserify@2.0.1": {
            "map": {
                "inherits": "npm:inherits@2.0.3",
                "readable-stream": "npm:readable-stream@2.2.2"
            }
        },
        "npm:buffer@4.9.1": {
            "map": {
                "isarray": "npm:isarray@1.0.0",
                "base64-js": "npm:base64-js@1.2.0",
                "ieee754": "npm:ieee754@1.1.8"
            }
        },
        "npm:jspm-nodelibs-buffer@0.2.0": {
            "map": {
                "buffer-browserify": "npm:buffer@4.9.1"
            }
        },
        "npm:jspm-nodelibs-crypto@0.2.0": {
            "map": {
                "crypto-browserify": "npm:crypto-browserify@3.11.0"
            }
        },
        "npm:jspm-nodelibs-string_decoder@0.2.0": {
            "map": {
                "string_decoder-browserify": "npm:string_decoder@0.10.31"
            }
        },
        "npm:jspm-nodelibs-stream@0.2.0": {
            "map": {
                "stream-browserify": "npm:stream-browserify@2.0.1"
            }
        },
        "npm:jspm-nodelibs-timers@0.2.0": {
            "map": {
                "timers-browserify": "npm:timers-browserify@1.4.2"
            }
        },
        "npm:jspm-nodelibs-os@0.2.0": {
            "map": {
                "os-browserify": "npm:os-browserify@0.2.1"
            }
        },
        "npm:pbkdf2@3.0.9": {
            "map": {
                "create-hmac": "npm:create-hmac@1.1.4"
            }
        },
        "npm:sha.js@2.4.8": {
            "map": {
                "inherits": "npm:inherits@2.0.3"
            }
        },
        "npm:asn1.js@4.9.1": {
            "map": {
                "bn.js": "npm:bn.js@4.11.6",
                "minimalistic-assert": "npm:minimalistic-assert@1.0.0",
                "inherits": "npm:inherits@2.0.3"
            }
        },
        "npm:readable-stream@2.2.2": {
            "map": {
                "isarray": "npm:isarray@1.0.0",
                "inherits": "npm:inherits@2.0.3",
                "string_decoder": "npm:string_decoder@0.10.31",
                "core-util-is": "npm:core-util-is@1.0.2",
                "buffer-shims": "npm:buffer-shims@1.0.0",
                "process-nextick-args": "npm:process-nextick-args@1.0.7",
                "util-deprecate": "npm:util-deprecate@1.0.2"
            }
        },
        "npm:rxjs@5.1.0": {
            "map": {
                "symbol-observable": "npm:symbol-observable@1.0.4"
            }
        },
        "npm:angular-ui-grid@4.0.4": {
            "map": {
                "angular": "npm:angular@1.5.11"
            }
        },
        "npm:bcrypt-pbkdf@1.0.1": {
            "map": {
                "tweetnacl": "npm:tweetnacl@0.14.5"
            }
        },
        "npm:angular-growl-notifications@2.6.0": {
            "map": {
                "angular": "npm:angular@1.6.4"
            }
        },
        "npm:restangular@1.6.1": {
            "map": {
                "lodash": "npm:lodash@4.17.4"
            }
        },
        "npm:ecc-jsbn@0.1.1": {
            "map": {
                "jsbn": "npm:jsbn@0.1.1"
            }
        },
        "npm:jodid25519@1.0.2": {
            "map": {
                "jsbn": "npm:jsbn@0.1.1"
            }
        },
        "npm:angular-nvd3@1.0.9": {
            "map": {
                "angular": "npm:angular@1.6.4",
                "d3": "npm:d3@3.5.17",
                "nvd3": "npm:nvd3@1.8.5"
            }
        },
        "npm:angular-ui-router@0.4.2": {
            "map": {
                "angular": "npm:angular@1.6.4"
            }
        },
        "npm:angular-material@1.1.3": {
            "map": {
                "css": "github:systemjs/plugin-css@0.1.23",
                "angular-aria": "github:angular/bower-angular-aria@1.6.4",
                "angular-animate": "github:angular/bower-angular-animate@1.6.4",
                "angular": "github:angular/bower-angular@1.6.4",
                "angular-messages": "github:angular/bower-angular-messages@1.6.4"
            }
        },
        "npm:jspm-nodelibs-url@0.2.1": {
            "map": {
                "url": "npm:url@0.11.0"
            }
        },
        "npm:jspm-nodelibs-http@0.2.0": {
            "map": {
                "http-browserify": "npm:stream-http@2.7.0"
            }
        },
        "npm:stream-http@2.7.0": {
            "map": {
                "inherits": "npm:inherits@2.0.3",
                "builtin-status-codes": "npm:builtin-status-codes@3.0.0",
                "to-arraybuffer": "npm:to-arraybuffer@1.0.1",
                "xtend": "npm:xtend@4.0.1",
                "readable-stream": "npm:readable-stream@2.2.7"
            }
        },
        "npm:readable-stream@2.2.7": {
            "map": {
                "buffer-shims": "npm:buffer-shims@1.0.0",
                "core-util-is": "npm:core-util-is@1.0.2",
                "isarray": "npm:isarray@1.0.0",
                "inherits": "npm:inherits@2.0.3",
                "process-nextick-args": "npm:process-nextick-args@1.0.7",
                "util-deprecate": "npm:util-deprecate@1.0.2",
                "string_decoder": "npm:string_decoder@1.0.0"
            }
        },
        "npm:jspm-nodelibs-zlib@0.2.3": {
            "map": {
                "browserify-zlib": "npm:browserify-zlib@0.1.4"
            }
        },
        "npm:string_decoder@1.0.0": {
            "map": {
                "buffer-shims": "npm:buffer-shims@1.0.0"
            }
        },
        "npm:browserify-zlib@0.1.4": {
            "map": {
                "readable-stream": "npm:readable-stream@2.2.7",
                "pako": "npm:pako@0.2.9"
            }
        },
        "npm:url@0.11.0": {
            "map": {
                "punycode": "npm:punycode@1.3.2",
                "querystring": "npm:querystring@0.2.0"
            }
        },
        "github:angular/bower-angular-aria@1.6.4": {
            "map": {
                "angular": "github:angular/bower-angular@1.6.4"
            }
        },
        "github:angular/bower-angular-animate@1.6.4": {
            "map": {
                "angular": "github:angular/bower-angular@1.6.4"
            }
        },
        "github:angular/bower-angular-messages@1.6.4": {
            "map": {
                "angular": "github:angular/bower-angular@1.6.4"
            }
        },
        "github:twbs/bootstrap@3.3.7": {
            "map": {
                "jquery": "npm:jquery@2.2.4"
            }
        }
    }
});
