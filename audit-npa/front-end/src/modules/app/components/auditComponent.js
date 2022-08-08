"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var core_1 = require('@angular/core');
require('rxjs/add/operator/catch');
var util_1 = require('util');
var error_1 = require("../classes/error");
var modelTree_1 = require('../classes/modelTree');
var modelFormDocument_1 = require('../classes/modelFormDocument');
var modelFormRevision_1 = require('../classes/modelFormRevision');
var documentNPARequests_1 = require('../requests/documentNPARequests');
var calendarService_1 = require("../services/calendarService");
var referenceRequest_1 = require('../requests/referenceRequest');
var revisionRequest_1 = require('../requests/revisionRequest');
var treeRequests_1 = require('../requests/treeRequests');
var dateConvertService_1 = require('../services/dateConvertService');
var createHeaderService_1 = require('../services/createHeaderService');
var AuditComponent = (function () {
    function AuditComponent(ru, docreq, refserv, revisserv, treeserv, dateconv, head, error) {
        this.ru = ru;
        this.docreq = docreq;
        this.refserv = refserv;
        this.revisserv = revisserv;
        this.treeserv = treeserv;
        this.dateconv = dateconv;
        this.head = head;
        this.error = error;
        this.lengthRev = 0;
        this.lengthDoc = 0;
        this.lengthDocFilter = 0;
        this.flagDocEdit = false;
        this.flagRevEdit = false;
        this.displayPaste = false;
        this.displayTree = false;
        this.displayTreeFile = false;
        this.displayLoading = false;
        this.displaySelTree = false;
        this.displayAddRevis = false;
        this.displayDelResponse = false;
        this.displayDelDecResponse = false;
        this.displayHTML = false;
        this.displayCompareRev = false;
        this.displayDoca = false;
        this.delFlag = true;
        this.editFlag = true;
        this.disEditRev = true;
        this.disDelRev = true;
        this.disViewStruct = true;
        this.disViewImage = true;
        this.disFullViewing = true;
        this.mergeDisabled = true;
        this.selectedEditingDrop = "";
        this.selectedTypesDoc = "1";
        this.tableFlag = false;
        this.suseMess = [];
        this.msgs = [];
        this.tableRevValue = [];
        this.modelFormDocs = new modelFormDocument_1.ModelFormDocument();
        this.modelFormRevision = new modelFormRevision_1.ModelFormRevision();
        this.modelTreeFile = new modelTree_1.ModelTreeFile();
        this.selectedTree = new modelTree_1.ModelTree();
    }
    /**Начальная инициализация */
    AuditComponent.prototype.ngOnInit = function () {
        this.docEditing();
        this.docDropDown();
        this.ru.ruLocale();
        this.rus = this.ru.ru;
        this.tableView();
    };
    /**Заполнение дропбокса*/
    AuditComponent.prototype.docDropDown = function () {
        var _this = this;
        this.arrTypes = [];
        this.refserv.getDataType().subscribe(function (type) {
            _this.formDocType = type.json();
            for (var i = 0; i < _this.formDocType.length; i++) {
                _this.textType = _this.formDocType[i].name;
                _this.idType = _this.formDocType[i].id;
                _this.arrTypes.push({ label: "" + _this.textType, value: "" + _this.idType });
            }
        });
    };
    AuditComponent.prototype.showDelDocResponse = function () {
        this.displayDelDecResponse = true;
    };
    AuditComponent.prototype.hideDelDocResponse = function () {
        this.displayDelDecResponse = false;
    };
    AuditComponent.prototype.docEditing = function () {
        this.ddEditing = [];
        this.ddEditing.push({ label: "\u0412\u0441\u0435", value: "" });
        this.ddEditing.push({ label: "\u0418\u0437\u043C.", value: "true" });
        this.ddEditing.push({ label: "\u041E\u0441\u043D.", value: "false" });
    };
    AuditComponent.prototype.showProgress = function () {
        this.displayLoading = true;
    };
    AuditComponent.prototype.hideProgress = function () {
        this.displayLoading = false;
    };
    AuditComponent.prototype.errorServer = function (e) {
        this.errorHttp = e.status.toString();
        this.res = "Ошибка сервера: " + this.errorHttp;
        if (!util_1.isUndefined(e.statusText) || !e.statusText.isEmpty()) {
            this.res += "\n " + e.statusText;
        }
        this.showError();
    };
    //Окно ошибки при запросе к БД
    AuditComponent.prototype.showError = function () {
        this.msgs = [];
        this.msgs.push({ severity: 'error', summary: 'Ошибка', detail: "" + this.res });
    };
    AuditComponent.prototype.tableView = function () {
        var _this = this;
        this.showProgress();
        this.editFlag = true;
        this.delFlag = true;
        this.docreq.getDataFindList().subscribe(function (d) {
            _this.documentsForTable = d.json();
            if (_this.documentsForTable.length == 0 || util_1.isUndefined(_this.documentsForTable)) {
                _this.res = _this.error.noDocuments();
                _this.showError();
            }
            else {
                for (var p = 0; p < _this.documentsForTable.length; p++) {
                    _this.documentsForTable[p].dt = _this.dateconv.InUniversalDate(_this.documentsForTable[p].dt);
                }
            }
            _this.editFlag = true;
            _this.delFlag = true;
            _this.lengthDoc = _this.documentsForTable.length;
            _this.lengthDocFilter = _this.documentsForTable.length;
            _this.hideProgress();
        }, function (err) {
            var error = err.status.toString();
        });
    };
    /**Событие свертывания строки таблицы документов НПА */
    AuditComponent.prototype.onRowCollapse = function (event) {
        this.delFlag = true;
        this.editFlag = true;
        this.mergeDisabled = true;
        this.valuesForMerge.length = 0;
    };
    /**Событие развертывания строки таблицы документов НПА */
    AuditComponent.prototype.onRowExpand = function (event) {
        if (!util_1.isUndefined(this.valuesForMerge))
            this.valuesForMerge.length = 0;
        this.selectedDocList = event.data;
        this.mergeDisabled = true;
        this.delFlag = false;
        this.editFlag = false;
        this.revView();
    };
    /**Событие при фильтрации документов НПА */
    AuditComponent.prototype.onFilterDoc = function (event) {
        this.lengthDocFilter = event.filteredValue.length;
    };
    /**Кнопка добавления документа НПА */
    AuditComponent.prototype.btnPaste = function () {
        this.modelFormDocs = new modelFormDocument_1.ModelFormDocument();
        this.flagDocEdit = false;
        this.getDropDown();
    };
    /**Кнопка редактирования НПА */
    AuditComponent.prototype.btnEdit = function () {
        this.editNPA();
    };
    AuditComponent.prototype.editNPA = function () {
        var _this = this;
        this.flagDocEdit = true;
        var own = true;
        this.docreq.getCheckIsDeletedDoc(this.selectedDocList.id).subscribe(function (get) {
            own = get.json();
            if (!own) {
                //Поднимаем из таблицы данные
                _this.getDropDown();
                _this.modelFormDocs = new modelFormDocument_1.ModelFormDocument();
                _this.modelFormDocs.initOfField(_this.selectedDocList.id, _this.selectedDocList.name, _this.selectedDocList.dt, _this.selectedDocList.num, _this.selectedDocList.docType['id'], _this.selectedDocList.editing);
            }
            else {
                _this.res = _this.error.documentWasDeleted();
                _this.showError();
            }
        });
    };
    AuditComponent.prototype.getDropDown = function () {
        this.showEdit();
    };
    AuditComponent.prototype.showEdit = function () {
        this.displayPaste = true;
    };
    /**Кнопка удаления НПА */
    AuditComponent.prototype.btnDelete = function () {
        this.showDelDocResponse();
    };
    /**Кнопка Да в диалоге удаления документа */
    AuditComponent.prototype.btnDelDocAnswerYes = function () {
        this.delNPA();
    };
    AuditComponent.prototype.delNPA = function () {
        var _this = this;
        var id = this.selectedDocList.id;
        this.docreq.deleteAddNPA(id).subscribe(function (del) {
            _this.tableView();
            _this.editFlag = true;
            _this.delFlag = true;
            _this.hideDelDocResponse();
        }, function (err) {
            var error = err.status.toString();
            _this.hideDelDocResponse();
        });
    };
    AuditComponent.prototype.revView = function () {
        var _this = this;
        this.showProgress();
        this.initVariables();
        var id = this.selectedDocList.id;
        var dateLabel;
        for (var j = 0; j < this.documentsForTable.length; j++) {
            if (this.documentsForTable[j].editing) {
                dateLabel = this.dateconv.InRussianDate(this.documentsForTable[j].dt);
                this.idNPARev.push({ label: "" + (this.documentsForTable[j].num + " от " + dateLabel), value: "" + this.documentsForTable[j].id });
            }
        }
        this.revisserv.getRevList(id).subscribe(function (rev) {
            var obj = rev.json();
            _this.tableRevValue = rev.json();
            _this.initRevView(obj);
            _this.lengthRev = obj.length;
            _this.hideProgress();
        });
    };
    /** Инициализация переменных для редакций */
    AuditComponent.prototype.initVariables = function () {
        this.docNum = this.selectedDocList.num;
        this.docDate = this.selectedDocList.dt;
        this.idNPARev = [];
        this.idNPARev.push({ label: "\u0411\u0435\u0437 \u0434\u043E\u043A\u0443\u043C\u0435\u043D\u0442\u0430", value: "" });
        this.modelFormRevision = new modelFormRevision_1.ModelFormRevision();
    };
    /**Заполнение основной переменной редакций */
    AuditComponent.prototype.initRevView = function (obj) {
        var _this = this;
        var _loop_1 = function(i) {
            this_1.tableRevValue[i].dtBegin = this_1.dateconv.InUniversalDate(obj[i].rev.dtBegin);
            if (obj[i].rev.dtEnd == null)
                this_1.tableRevValue[i].dtEnd == null;
            else
                this_1.tableRevValue[i].dtEnd = this_1.dateconv.InUniversalDate(obj[i].rev.dtEnd);
            this_1.tableRevValue[i].dtRev = this_1.dateconv.InUniversalDate(obj[i].rev.dtRev);
            this_1.tableRevValue[i].idRev = obj[i].rev.id;
            this_1.tableRevValue[i].idDoc = obj[i].rev.id_NPA;
            this_1.tableRevValue[i].loaded = obj[i].loaded;
            this_1.tableRevValue[i].id_NPA_Revision = obj[i].rev.id_NPA_Revision;
            if (this_1.tableRevValue[i].id_NPA_Revision != null)
                this_1.tableRevValue[i].npaRevDoc = this_1.documentsForTable.find(function (d) { return d.id == _this.tableRevValue[i].id_NPA_Revision; });
            else
                this_1.tableRevValue[i].npaRevDoc = null;
        };
        var this_1 = this;
        for (var i = 0; i < obj.length; i++) {
            _loop_1(i);
        }
    };
    /**Кнопка просмотра структуры редакции */
    //Todo Переписать бы, наверное
    AuditComponent.prototype.btnViewStruct = function (row) {
        var _this = this;
        this.revisserv.getCheckIsDeletedRev(row.idRev).subscribe(function (get) {
            var own = get.json();
            if (!own) {
                if (_this.selectedDocList != null) {
                    _this.modelFormRevision = row;
                    _this.disFullViewing = true;
                    _this.showProgress();
                    _this.dateRedaction = _this.modelFormRevision.dtRev;
                    var id = _this.selectedDocList.id;
                    var idRev = _this.modelFormRevision.idRev;
                    _this.treeserv.getDataTree(id, idRev).subscribe(function (tr) {
                        _this.modelTree = tr.json();
                        _this.dateParseTree(_this.modelTree);
                        _this.displayTree = true;
                        _this.hideProgress();
                    }, function (err) {
                        var error = err.status.toString();
                    });
                }
                else {
                    _this.res = _this.error.notSelectedDocument();
                    _this.showError();
                }
            }
            else {
                _this.res = _this.error.revisionWasDeleted();
                _this.showError();
            }
        });
    };
    /**Кнопка добавления редакции */
    AuditComponent.prototype.btnAddRev = function () {
        if (this.selectedDocList != null) {
            this.modelFormRevision = new modelFormRevision_1.ModelFormRevision();
            this.idNPARev;
            this.flagRevEdit = false;
            this.showAddRevis();
        }
        else {
            this.res = this.error.notSelectedDocument();
            this.showError();
        }
    };
    AuditComponent.prototype.showAddRevis = function () {
        this.displayAddRevis = true;
    };
    AuditComponent.prototype.showDelResponse = function () {
        this.displayDelResponse = true;
    };
    AuditComponent.prototype.hideDelResponse = function () {
        this.displayDelResponse = false;
    };
    /**Кнопка обновления таблицы */
    AuditComponent.prototype.btnRefresh = function () {
        this.tableView();
    };
    /**Событие выбора строки в таблице редакций */
    AuditComponent.prototype.onRowRevSelect = function (event) {
        this.modelFormRevision = event.data;
    };
    /**Кнопка удаления редакции */
    AuditComponent.prototype.btnDelRev = function (row) {
        this.modelFormRevision = row;
        this.showDelResponse();
    };
    AuditComponent.prototype.btnDelAnswerYes = function () {
        var _this = this;
        var idRev = this.modelFormRevision.idRev;
        this.revisserv.getDeleteRev(idRev).subscribe(function (del) {
            _this.revView();
            _this.hideDelResponse();
        }, function (err) {
            var error = err.status.toString();
            _this.hideDelResponse();
        });
    };
    /**Кнопка редактирования редакции */
    AuditComponent.prototype.btnEditRev = function (row) {
        if (this.selectedDocList != null) {
            this.modelFormRevision = row;
            this.editRev();
        }
        else {
            this.res = this.error.notSelectedDocument();
            this.showError();
        }
    };
    AuditComponent.prototype.editRev = function () {
        var _this = this;
        this.revisserv.getCheckIsDeletedRev(this.modelFormRevision.idRev).subscribe(function (get) {
            var own = get.json();
            if (!own) {
                _this.flagRevEdit = true;
                _this.modelFormRevision.idRev = _this.modelFormRevision.idRev;
                _this.modelFormRevision.dtBegin = _this.modelFormRevision.dtBegin;
                _this.modelFormRevision.dtRev = _this.modelFormRevision.dtRev;
                _this.modelFormRevision.dtEnd = _this.modelFormRevision.dtEnd;
                _this.modelFormRevision.id_NPA_Revision = _this.modelFormRevision.id_NPA_Revision;
                _this.showAddRevis();
            }
            else {
                _this.res = _this.error.revisionWasDeleted();
                _this.showError();
            }
        });
    };
    AuditComponent.prototype.docSubmit = function () {
        var form = this.form['form'];
        var obj = form._value;
        this.submitDoc(obj);
    };
    AuditComponent.prototype.submitDoc = function (obj) {
        if (util_1.isUndefined(obj.date) || obj.date == null) {
            this.res = this.error.enteredWrongDate();
            this.showError();
        }
        else {
            for (var i = 0; i < this.formDocType.length; i++) {
                if (obj.type == this.formDocType[i].id || obj.type == this.formDocType[i].name) {
                    obj.type = this.formDocType[i];
                    break;
                }
            }
            obj.date = this.dateconv.InDBDate(obj.date);
            if (util_1.isUndefined(obj.number) || obj.number == null || obj.number == "") {
                this.res = this.error.notAllData();
                this.showError();
            }
            else {
                this.selectMetodDoc(obj);
            }
        }
    };
    AuditComponent.prototype.selectMetodDoc = function (obj) {
        var _this = this;
        if (this.flagDocEdit) {
            var id = this.selectedDocList.id;
            this.docreq.putAddNPA(obj, id).subscribe(function (put) {
                _this.displayPaste = false;
                _this.tableView();
            }, function (err) {
                var error = err.status.toString();
            });
        }
        else {
            this.docreq.getDataAddNPA(obj).subscribe(function (get) {
                _this.displayPaste = false;
                _this.tableView();
            }, function (err) {
                var error = err.status.toString();
            });
        }
    };
    //Todo Переписать условия
    AuditComponent.prototype.revSubmit = function () {
        var _this = this;
        var obj = this.getObjectRevSubmit();
        if (obj.dtRev != null) {
            if (obj.dtBegin != null) {
                //Флаг для идентификации того, редактируем или добавляем
                if (this.flagRevEdit) {
                    var idRev = this.modelFormRevision.idRev;
                    this.revisserv.getPutRev(idRev, obj).subscribe(function (put) {
                        _this.displayAddRevis = false;
                        _this.revView();
                    });
                }
                else {
                    this.revisserv.getPostRev(obj).subscribe(function (post) {
                        _this.displayAddRevis = false;
                        _this.revView();
                    }, function (err) {
                        var error = err.status.toString();
                    });
                }
            }
            else {
                this.res = "Не введена дата начала редакции!";
                this.showError();
            }
        }
        else {
            this.res = "Не введена дата редакции!";
            this.showError();
        }
    };
    AuditComponent.prototype.getObjectRevSubmit = function () {
        var obj = {
            id_NPA: 0,
            dtBegin: null,
            dtRev: null,
            dtEnd: null,
            id_NPA_Revision: 0
        };
        var form = this.RevForm['form'];
        obj.id_NPA = this.selectedDocList.id;
        obj.dtBegin = form._value.dtBegin;
        obj.dtEnd = form._value.dtEnd;
        obj.dtRev = form._value.dtRev;
        obj.id_NPA_Revision = form._value.id_NPA_Revision;
        obj.dtBegin = this.dateconv.InDBDate(obj.dtBegin);
        obj.dtRev = this.dateconv.InDBDate(obj.dtRev);
        obj.dtEnd = this.dateconv.InDBDate(obj.dtEnd);
        return obj;
    };
    AuditComponent.prototype.showSelTree = function () {
        this.displaySelTree = true;
    };
    AuditComponent.prototype.hideSelTree = function () {
        this.displaySelTree = false;
    };
    AuditComponent.prototype.strSubmit = function () {
        var _this = this;
        this.showProgress();
        var obj = {
            id: "",
            label: "",
            dtBegin: null,
            dtEnd: null
        };
        var idStr = this.selectedTree.data.id;
        var idDoc = this.modelFormRevision.idDoc;
        var idRev = this.modelFormRevision.idRev;
        obj.label = this.tabRevfull[0].label;
        obj.id = this.selectedTree.data.id;
        var form = this.strForm['form'];
        if (util_1.isUndefined(form._value.dtBegin) || form._value.dtBegin == null) {
            obj.dtBegin = null;
        }
        else {
            obj.dtBegin = this.dateconv.InUniversalDate(form._value.dtBegin);
            obj.dtBegin = this.dateconv.InDBDate(obj.dtBegin);
        }
        if (util_1.isUndefined(form._value.dtEnd) || form._value.dtEnd == null) {
            obj.dtEnd = null;
        }
        else {
            obj.dtEnd = this.dateconv.InUniversalDate(form._value.dtEnd);
            obj.dtEnd = this.dateconv.InDBDate(obj.dtEnd);
        }
        this.treeserv.postDocStrDates(obj, idStr, idDoc, idRev).subscribe(function (post) {
            _this.hideProgress();
            _this.hideSelTree();
            _this.refreshTree(_this.modelFormRevision);
        }, function (err) {
            var error = err.status.toString();
        });
    };
    AuditComponent.prototype.onNodeTreeSelect = function (event) {
        this.disFullViewing = false;
    };
    AuditComponent.prototype.refreshTree = function (model) {
        var _this = this;
        this.showProgress();
        var id = model.idDoc;
        var idRev = model.idRev;
        this.treeserv.getDataTree(id, idRev).subscribe(function (tr) {
            _this.modelTree = tr.json();
            _this.dateParseTree(_this.modelTree);
            _this.hideProgress();
        }, function (err) {
            var error = err.status.toString();
        });
    };
    AuditComponent.prototype.btnFullViewing = function () {
        var _this = this;
        this.showProgress();
        var idStr = this.selectedTree.data.id;
        var idDoc = this.selectedDocList.id;
        var idRev = this.modelFormRevision.idRev;
        this.treeserv.getDocStrText(idStr, idDoc, idRev).subscribe(function (get) {
            _this.tabRevfull = [];
            _this.tabRevfull[0] = _this.srtVal(get.text());
            _this.showSelTree();
            _this.hideProgress();
        }, function (err) {
            var error = err.status.toString();
        });
    };
    AuditComponent.prototype.srtVal = function (text) {
        var srt = {
            label: "",
            dtBegin: null,
            dtEnd: null,
            path: ""
        };
        srt.label = text;
        srt.dtBegin = this.selectedTree.data.dtBegin;
        srt.dtEnd = this.selectedTree.data.dtEnd;
        srt.path = this.selectedTree.data.path;
        return srt;
    };
    AuditComponent.prototype.dateParseTree = function (model) {
        for (var i = 0; i < model.length; i++) {
            model[i].data.dtBegin = this.dateconv.InUniversalDate(model[i].data.dtBegin);
            model[i].data.dtEnd = this.dateconv.InUniversalDate(model[i].data.dtEnd);
            if (model[i].children.length != 0) {
                this.dateParseTree(model[i].children);
            }
        }
    };
    AuditComponent.prototype.onBeforeSendUploadFile = function (event) {
        this.showProgress();
        var obj = this.treeserv.createHeader(this.head.createCsrfHeader());
        event.xhr.setRequestHeader(obj['key'][0], obj['value'][0]);
    };
    AuditComponent.prototype.onSelectUploadFile = function (event) {
        this.fubFile['url'] = this.treeserv.formattedURL();
    };
    AuditComponent.prototype.onUploadFile = function (event) {
        this.uploadFile(event);
    };
    AuditComponent.prototype.uploadFile = function (event) {
        if (!util_1.isUndefined(this.selectedDocList)) {
            if (!util_1.isUndefined((this.modelFormRevision.idRev)) || this.modelFormRevision.idRev != null) {
                this.fileName = event.files[0].name;
                if (event.xhr.status == 200) {
                    this.modelTreeFile = JSON.parse(event.xhr.response);
                    this.showDisplayTreeFile();
                }
                else {
                    this.res = "Ошибка сервера: " + event.xhr.status + "\n " + event.xhr.statusText;
                    this.showError();
                }
                this.hideProgress();
            }
            else {
                this.res = this.error.notSelectedRevision();
                this.showError();
            }
        }
        else {
            this.res = this.error.notSelectedDocument();
            this.showError();
        }
        this.hideProgress();
    };
    AuditComponent.prototype.showDisplayTreeFile = function () {
        this.displayTreeFile = true;
    };
    AuditComponent.prototype.hideDisplayTreeFile = function () {
        this.displayTreeFile = false;
    };
    AuditComponent.prototype.btnSaveLoadFile = function () {
        var _this = this;
        this.showProgress();
        var id;
        id = this.selectedDocList.id;
        var idRev = this.modelFormRevision.idRev;
        this.treeserv.getDocConvert(id, idRev).subscribe(function (get) {
            _this.hideProgress();
            _this.hideDisplayTreeFile();
            _this.revView();
        }, function (err) {
            _this.errorServer(err);
            _this.hideProgress();
        });
    };
    AuditComponent.prototype.btnViewImage = function (row) {
        var _this = this;
        this.revisserv.getCheckIsDeletedRev(row.idRev).subscribe(function (get) {
            var own = get.json();
            if (!own) {
                if (_this.selectedDocList != null) {
                    _this.showProgress();
                    _this.modelFormRevision = row;
                    _this.displayHTML = true;
                    _this.dateRedaction = _this.modelFormRevision.dtRev;
                    var id = _this.selectedDocList.id;
                    var idRev = _this.modelFormRevision.idRev;
                    _this.revisserv.getViewHTML(id, idRev).subscribe(function (get) {
                        _this.divHtml.nativeElement.innerHTML = get.text();
                        _this.hideProgress();
                    }, function (err) {
                        _this.hideProgress();
                    });
                }
                else {
                    _this.res = _this.error.notSelectedDocument();
                    _this.showError();
                }
            }
            else {
                _this.res = _this.error.revisionWasDeleted();
                _this.showError();
            }
        });
    };
    AuditComponent.prototype.mergeAndCompare = function () {
        var _this = this;
        var doc = this.selectedDocList.id;
        var drevs = this.valuesForMerge;
        this.revisserv.compareRevisions(+doc, +drevs[0], +drevs[1]).subscribe(function (res) {
            _this.divCompareRevsHtml.nativeElement.innerHTML = res.text();
            _this.displayCompareRev = true;
        });
    };
    /*Доступ к кнопке сравнения*/
    AuditComponent.prototype.onMergeSelected = function () {
        this.mergeDisabled = this.valuesForMerge.length != 2;
    };
    __decorate([
        core_1.ViewChild('addForm')
    ], AuditComponent.prototype, "form");
    __decorate([
        core_1.ViewChild('RevForm')
    ], AuditComponent.prototype, "RevForm");
    __decorate([
        core_1.ViewChild('StrForm')
    ], AuditComponent.prototype, "strForm");
    __decorate([
        core_1.ViewChild('fubfile')
    ], AuditComponent.prototype, "fubFile");
    __decorate([
        core_1.ViewChild('divHtml')
    ], AuditComponent.prototype, "divHtml");
    __decorate([
        core_1.ViewChild('divCompareRevsHtml')
    ], AuditComponent.prototype, "divCompareRevsHtml");
    __decorate([
        core_1.ViewChild('gb')
    ], AuditComponent.prototype, "gb");
    AuditComponent = __decorate([
        core_1.Component({
            selector: 'app',
            templateUrl: './appComp.html'
        }),
        core_1.Injectable(),
        __param(0, core_1.Inject(calendarService_1.CalendarService)),
        __param(1, core_1.Inject(documentNPARequests_1.DocumentNPARequests)),
        __param(2, core_1.Inject(referenceRequest_1.Reference)),
        __param(3, core_1.Inject(revisionRequest_1.RevisService)),
        __param(4, core_1.Inject(treeRequests_1.TreesRequests)),
        __param(5, core_1.Inject(dateConvertService_1.DateConvertService)),
        __param(6, core_1.Inject(createHeaderService_1.Head)),
        __param(7, core_1.Inject(error_1.Error))
    ], AuditComponent);
    return AuditComponent;
}());
exports.AuditComponent = AuditComponent;
//# sourceMappingURL=auditComponent.js.map