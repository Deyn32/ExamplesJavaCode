/**
 * Created by dmihaylov on 17.04.2017.
 * Модель для формы правовых документов
 */
"use strict";
var ModelFormDocument = (function () {
    function ModelFormDocument() {
        this.editing = false;
        this.type = "1";
    }
    ModelFormDocument.prototype.initOfField = function (id, name, dt, num, type, editing) {
        this.id = id;
        this.name = name;
        this.dt = dt;
        this.num = num;
        this.type = type;
        this.editing = editing;
    };
    return ModelFormDocument;
}());
exports.ModelFormDocument = ModelFormDocument;
//# sourceMappingURL=modelFormDocument.js.map