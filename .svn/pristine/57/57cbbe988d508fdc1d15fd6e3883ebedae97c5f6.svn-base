/**
 * 系统通用配置
 * 2017/05/12   wubiao   ZW_C_JB_00008 商城增加通货
 */
Ext.define('Common.config', {
    statics: {
        //资源地址
        RESOURCE_URL: 'http://yxbmall.5173.com',
        //RESOURCE_URL: 'http://localhost:8081/gamegold-facade-backend',
        //用户无权限的异常CODE
        USER_NOTALLOW: 'ERROR.SECURITY.NOTALLOW',
        //当前用户刷新
        UPDATE_INFO_INTERVAL: 300
    }
});

// ************************************** 处理浏览器兼容性问题 ************************************* //
/**
 * String对象的split方法在某些版本浏览器下不兼容
 */
var split;
// Avoid running twice; that would break the `nativeSplit` reference
split = split || function (undef) {

        var nativeSplit = String.prototype.split,
            compliantExecNpcg = /()??/.exec("")[1] === undef, // NPCG: nonparticipating capturing group
            self;

        self = function (str, separator, limit) {
            // If `separator` is not a regex, use `nativeSplit`
            if (Object.prototype.toString.call(separator) !== "[object RegExp]") {
                return nativeSplit.call(str, separator, limit);
            }
            var output = [],
                flags = (separator.ignoreCase ? "i" : "") +
                    (separator.multiline ? "m" : "") +
                    (separator.extended ? "x" : "") + // Proposed for ES6
                    (separator.sticky ? "y" : ""), // Firefox 3+
                lastLastIndex = 0,
            // Make `global` and avoid `lastIndex` issues by working with a copy
                separator = new RegExp(separator.source, flags + "g"),
                separator2, match, lastIndex, lastLength;
            str += ""; // Type-convert
            if (!compliantExecNpcg) {
                // Doesn't need flags gy, but they don't hurt
                separator2 = new RegExp("^" + separator.source + "$(?!\\s)", flags);
            }
            /* Values for `limit`, per the spec:
             * If undefined: 4294967295 // Math.pow(2, 32) - 1
             * If 0, Infinity, or NaN: 0
             * If positive number: limit = Math.floor(limit); if (limit > 4294967295) limit -= 4294967296;
             * If negative number: 4294967296 - Math.floor(Math.abs(limit))
             * If other: Type-convert, then use the above rules
             */
            limit = limit === undef ?
            -1 >>> 0 : // Math.pow(2, 32) - 1
            limit >>> 0; // ToUint32(limit)
            while (match = separator.exec(str)) {
                // `separator.lastIndex` is not reliable cross-browser
                lastIndex = match.index + match[0].length;
                if (lastIndex > lastLastIndex) {
                    output.push(str.slice(lastLastIndex, match.index));
                    // Fix browsers whose `exec` methods don't consistently return `undefined` for
                    // nonparticipating capturing groups
                    if (!compliantExecNpcg && match.length > 1) {
                        match[0].replace(separator2, function () {
                            for (var i = 1; i < arguments.length - 2; i++) {
                                if (arguments[i] === undef) {
                                    match[i] = undef;
                                }
                            }
                        });
                    }
                    if (match.length > 1 && match.index < str.length) {
                        Array.prototype.push.apply(output, match.slice(1));
                    }
                    lastLength = match[0].length;
                    lastLastIndex = lastIndex;
                    if (output.length >= limit) {
                        break;
                    }
                }
                if (separator.lastIndex === match.index) {
                    separator.lastIndex++; // Avoid an infinite loop
                }
            }
            if (lastLastIndex === str.length) {
                if (lastLength || !separator.test("")) {
                    output.push("");
                }
            } else {
                output.push(str.slice(lastLastIndex));
            }
            return output.length > limit ? output.slice(0, limit) : output;
        };

// For convenience
        String.prototype.split = function (separator, limit) {
            return self(this, separator, limit);
        };

        return self;
    }();

/**
 * String对象的trim方法在某些版本浏览器下不兼容
 */
if (typeof String.prototype.trim !== "function") {
    String.prototype.trim = function () {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    }
}
/**
 * Array对象的indexOf方法在某些版本浏览器下不兼容
 */
if (typeof Array.prototype.indexOf !== "function") {
    Array.prototype.indexOf = function (obj) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == obj) {
                return i;
            }
        }
        return -1;
    }
}

// **************************************** Override **************************************** //
/**
 * 设置EXTJS请求超时时间为300秒
 */
Ext.Ajax.timeout = 300000;//300秒

/**
 * 设置系统中window位置默认限制在它的父元素内
 */
Ext.override(Ext.window.Window, {
    constrain: true
});

/**
 * 设置系统中rownumberer的宽度为50
 */
Ext.override(Ext.grid.RowNumberer, {
    width: 50
});

/**
 * 设置系统中表格内的内容都可选择
 */
Ext.override(Ext.view.Table, {
    enableTextSelection: true
});

/**
 * 修改fieldcontainer及其子类的beforeLabelTextTpl，并增加allowBlank属性，用以显示必填符号
 */
Ext.override(Ext.form.FieldContainer, {
    allowBlank: true,
    beforeLabelTextTpl: new Ext.XTemplate(
        '<tpl if="!allowBlank">',
        '<font color="red">*</font>',
        '</tpl>',
        '<tpl if="allowBlank">',
        '&nbsp;&nbsp;',
        '</tpl>'
    )
});

/**
 * 修改fieldcontainer及其子类的beforeLabelTextTpl，并增加allowBlank属性，用以显示必填符号
 */
Ext.override(Ext.form.field.File, {
    allowBlank: true,
    beforeLabelTextTpl: new Ext.XTemplate(
        '<tpl if="!allowBlank">',
        '<font color="red">*</font>',
        '</tpl>',
        '<tpl if="allowBlank">',
        '&nbsp;&nbsp;',
        '</tpl>'
    )
});

/**
 * 修复grid中设置loadMask无法正常显示的bug
 */
Ext.override(Ext.view.AbstractView, {
    onRender: function () {
        var me = this;
        me.callParent();
        if (me.mask && Ext.isObject(me.store)) {
            me.setMaskBind(me.store);
        }
    }
});

/**
 * 修改textfield，使之值为空格时进行空值错误提醒
 */
Ext.override(Ext.form.field.Text, {
    getErrors: function (value) {
        var me = this,
            errors = [],
            validator = me.validator,
            emptyText = me.emptyText,
            allowBlank = me.allowBlank,
            vtype = me.vtype,
            vtypes = Ext.form.field.VTypes,
            regex = me.regex,
            format = Ext.String.format,
            msg;
        value = value || me.processRawValue(me.getRawValue());
        if (Ext.isFunction(validator)) {
            msg = validator.call(me, value);
            if (msg !== true) {
                errors.push(msg);
            }
        }
        // ******************** Start ******************** //
        var trimValue = value.toString().trim();
        if (trimValue.length < 1 || trimValue === emptyText) {
            if (!allowBlank) {
                errors.push(me.blankText);
            }
            return errors;
        }
        // ********************* End ********************* //
        if (value.length < me.minLength) {
            errors.push(format(me.minLengthText, me.minLength));
        }
        if (value.length > me.maxLength) {
            errors.push(format(me.maxLengthText, me.maxLength));
        }
        if (vtype) {
            if (!vtypes[vtype](value, me)) {
                errors.push(me.vtypeText || vtypes[vtype + 'Text']);
            }
        }
        if (regex && !regex.test(value)) {
            errors.push(me.regexText || me.invalidText);
        }
        return errors;
    }
});

/**
 * 修改Trigger及其子类使用setReadOnly时的样式，并解决设某些情况下，trigger没有隐藏显示的问题
 */
Ext.override(Ext.form.field.Trigger, {
    setReadOnly: function (readOnly) {
        var me = this,
            hideTrigger = me.hideTrigger,
            inputEl = me.inputEl;
        readOnly = !!readOnly;
        if (readOnly == me.readOnly) {
            return;
        }
        me[readOnly ? 'addCls' : 'removeCls'](me.readOnlyCls);
        // ******************** Start ******************** //
        // 解决组件默认非只读状态，在调用setReadOnly方法设置只读时，trigger没有隐藏显示的问题
        if (!hideTrigger && me.triggerEl && me.triggerEl.elements) {
            var len = me.triggerEl.elements.length,
                i, el, parentEl;
            if (readOnly) {
                for (i = 0; i < len; i++) {
                    el = me.triggerEl.elements[i];
                    parentEl = el.dom.parentElement;
                    parentEl.style.display = "none";
                    el.dom.style.display = "none";
                }
            } else {
                for (i = 0; i < len; i++) {
                    el = me.triggerEl.elements[i];
                    parentEl = el.dom.parentElement;
                    parentEl.style.display = "block";
                    el.dom.style.display = "block";
                }
            }
        }
        // ********************* End ********************* //
        if (inputEl) {
            inputEl.dom.readOnly = readOnly;
            if (readOnly) {
                inputEl.dom.style.cursor = 'default';
                if (Ext.fly(inputEl.id + '-prefix') && Ext.fly(inputEl.id + '-prefix').dom) {
                    Ext.fly(inputEl.id + '-prefix').dom.innerHTML = '&nbsp;&nbsp;';
                }
                if (Ext.fly(me.id + '-errorEl') && Ext.fly(me.id + '-errorEl').dom) {
                    Ext.fly(me.id + '-errorEl').dom.style.visibility = 'hidden';
                }
            } else {
                inputEl.dom.style.cursor = 'text';
                if (!me.allowBlank && Ext.fly(inputEl.id + '-prefix') && Ext.fly(inputEl.id + '-prefix').dom) {
                    Ext.fly(inputEl.id + '-prefix').dom.innerHTML = '*';
                }
                if (Ext.fly(me.id + '-errorEl') && Ext.fly(me.id + '-errorEl').dom) {
                    Ext.fly(me.id + '-errorEl').dom.style.visibility = 'visible';
                }
            }
        } else if (me.rendering) {
            me.setReadOnlyOnBoxReady = true;
        }
        if (readOnly != me.readOnly) {
            me.readOnly = readOnly;
            me.updateLayout();
        }
    }
});

/**
 * 修改Ext.form.field.Base及其子类的必填样式、只读样式；增加提示功能(通过配置tipConfig)
 */
Ext.override(Ext.form.field.Base, {
    msgTarget: 'side',
    tipConfig: '',
    labelableRenderProps: 'readOnly,allowBlank,labelAlign,fieldBodyCls,baseBodyCls,clearCls,labelSeparator,msgTarget',
    beforeLabelTextTpl: new Ext.XTemplate(
        '<tpl if="!allowBlank">',
        '<tpl if="!readOnly">',
        '<label id="{inputId}-prefix" style="color:red">*</label>',
        '</tpl>',
        '<tpl if="readOnly">',
        '<label id="{inputId}-prefix" style="color:red">&nbsp;&nbsp;</label>',
        '</tpl>',
        '</tpl>',
        '<tpl if="allowBlank">',
        '<label id="{inputId}-prefix" style="color:red">&nbsp;&nbsp;</label>',
        '</tpl>'
    ),
    setReadOnly: function (readOnly) {
        var me = this,
            inputEl = me.inputEl;
        readOnly = !!readOnly;
        me[readOnly ? 'addCls' : 'removeCls'](me.readOnlyCls);
        me.readOnly = readOnly;
        if (inputEl) {
            inputEl.dom.readOnly = readOnly;
            if (me.readOnly) {
                Ext.fly(inputEl.id + '-prefix').dom.innerHTML = '&nbsp;&nbsp;';
                if (Ext.fly(me.id + '-errorEl')) {
                    Ext.fly(me.id + '-errorEl').dom.style.visibility = 'hidden';
                }
            } else {
                if (!me.allowBlank) {
                    Ext.fly(inputEl.id + '-prefix').dom.innerHTML = '*';
                }
                Ext.fly(me.id + '-errorEl').dom.style.visibility = 'visible';
            }
        } else if (me.rendering) {
            me.setReadOnlyOnBoxReady = true;
        }
        me.fireEvent('writeablechange', me, readOnly);
    }
});

Ext.override(Ext.selection.RowModel, {
    onRowClick: function (view, record, item, index, e) {
        if (item.parentElement.id.search(view.id) < 0) {
            this.mousedownAction = true;
            return;
        }
        if (this.mousedownAction) {
            this.mousedownAction = false;
        } else {
            this.processSelection(view, record, item, index, e);
        }
    },
    onRowMouseDown: function (view, record, item, index, e) {
        var me = this;

        if (item.parentElement.id.search(view.id) < 0) {
            this.mousedownAction = true;
            return;
        }
        // Record index will be -1 if the clicked record is a metadata record and not selectable
        if (index !== -1) {
            if (!me.allowRightMouseSelection(e)) {
                return;
            }
            if (!me.isSelected(record)) {
                me.mousedownAction = true;
                me.processSelection(view, record, item, index, e);
            } else {
                me.mousedownAction = false;
            }
        }
    }
});

Ext.define('Error.errorMessage.Window', {
    extend: 'Ext.window.Window',
    title: "服务器异常",
    eTitle: "未知异常",
    eDetailTitle: "异常详细信息",
    resizable: false,
    modal: true,
    width: 550,
    defaults: {
        anchor: '100%',
        padding: '5 5 5 5'
    },
    message: null,
    buttons: [{
        text: '确定',
        handler: function (button, even) {
            button.up('window').destroy();
        }
    }],
    constructor: function (config) {
        var me = this,
            cfg = Ext.apply({}, config);
        me.message = config.message;
        me.stackTrace = config.stackTrace;
        me.items = [{
            title: me.eTitle,
            html: me.message
        }, {
            title: me.eDetailTitle,
            collapsible: true,
            collapsed: true,
            items: [{
                autoScroll: true,
                height: 200,
                html: me.stackTrace
            }]
        }];
        me.callParent([cfg]);
    }
});
Ext.define('Error.errorMessage.Box', {
    statics: {
        title: "系统提示",
        msg: "连接异常,稍后重试。",
        goLoginMsg: "会话失效，请重新登录！",
        showGoLoginWin: function () {
            Ext.MessageBox.show({
                title: Error.errorMessage.Box.title,
                msg: Error.errorMessage.Box.goLoginMsg,
                buttons: Ext.MessageBox.OK,
                fn: function (btn) {
                    window.location = './index.action';
                },
                icon: Ext.MessageBox.WARNING
            });
        }
    }
});

/**
 * 重写EXTJS 4.1中的conection类中onComplete方法，使架构异常得到解决
 * 重写onUploadComplete解决文件上传时候可以以JSON返回结果
 * 增加onJsonResponse方法，进行JSON返回结果的处理
 */
Ext.override(Ext.data.Connection, {
    timeout: 300000,
    onJsonResponse: function (contentType, response, options) {
        if (contentType.toLowerCase() == 'application/json;charset=utf-8') {
            var json = Ext.decode(response.responseText);
            if (json) {
                //业务执行成功
                if (json.success && !json.isException) {
                    Ext.callback(options.success, options.scope, [response, options]);
                    //业务异常
                } else if (!json.success && !json.isException) {
                    Ext.callback(options.exception, options.scope, [response, options]);
                    //未知异常
                } else if (!json.success && json.isException) {
                    if (json.stackTrace && json.stackTrace.indexOf("UserNotLoginException") > 0) {
                        Error.errorMessage.Box.showGoLoginWin();
                        return;
                    }
                    if (json.errorCode == Common.config.USER_NOTALLOW) {
                        Ext.ux.Toast.msg('', json.message, 'error');
                    } else {
                        var ErrorMessageWindow = Ext.create('Error.errorMessage.Window', {
                            message: json.message,
                            stackTrace: json.stackTrace
                        });
                        ErrorMessageWindow.show();
                    }
                    Ext.callback(options.unknownException, options.scope, [response, options]);
                }
            }
        } else {
            if (response.responseText != null) {
                if (response.responseText.indexOf('<meta content="LOGIN_JSP">') > 0) {
                    Error.errorMessage.Box.showGoLoginWin();
                    return;
                }
            }
            Ext.callback(options.success, options.scope, [response, options]);
        }
    },
    onUploadComplete: function (frame, options) {
        var me = this,
            response = {
                responseText: '',
                responseXML: null
            }, contentType, doc, firstChild;
        try {
            doc = frame.contentWindow.document || frame.contentDocument || window.frames[frame.id].document;
            if (doc) {
                if (doc.body) {
                    //更新textarea为pre
                    if (/pre/i.test((firstChild = doc.body.firstChild || {}).tagName)) { // json response wrapped in pre
                        response.responseText = firstChild.innerHTML;
                        contentType = 'application/json;charset=utf-8';
                    } else {
                        response.responseText = doc.body.innerHTML;
                        contentType = 'text/html'
                    }
                }
                response.responseXML = doc.XMLDocument || doc;
            }
        } catch (e) {
        }
        me.fireEvent('requestcomplete', me, response, options);
        me.onJsonResponse(contentType, response, options);
        Ext.callback(options.callback, options.scope, [options, true, response]);
        setTimeout(function () {
            Ext.removeNode(frame);
        }, 100);
    },
    onComplete: function (request) {
        var me = this,
            options = request.options,
            result, success, response;
        try {
            result = me.parseStatus(request.xhr.status);
        } catch (e) {
            result = {
                success: false,
                isException: false
            };
        }
        success = result.success;
        if (success) {
            response = me.createResponse(request);
            me.fireEvent('requestcomplete', me, response, options);
            // ******************** Start ******************** //
            me.onJsonResponse(response.getResponseHeader('Content-Type'), response, options);
            // ********************* End ********************* //
        } else {
            if (result.isException || request.aborted || request.timedout) {
                response = me.createException(request);
            } else {
                response = me.createResponse(request);
            }
            me.fireEvent('requestexception', me, response, options);
            Ext.callback(options.failure, options.scope, [response, options]);
            // ******************** Start ******************** //
            //服务端异常提示
            Ext.MessageBox.show({
                title: Error.errorMessage.Box.title,
                msg: Error.errorMessage.Box.msg,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.ERROR
            });
            // ********************* End ********************* //
        }
        Ext.callback(options.callback, options.scope, [options, success, response]);
        delete me.requests[request.id];
        return response;
    }
});

/**
 * 重写EXTJS 4.1中的Action类中createCallback方法，使form submit提交的时候，如果出现架构异常，可以在页面上得到解决
 */
Ext.override(Ext.form.action.Action, {
    createCallback: function () {
        var me = this,
            undef,
            form = me.form;
        return {
            success: me.onSuccess,
            failure: me.onFailure,
            exception: me.onSuccess,
            unknownException: me.onSuccess,
            scope: me,
            timeout: (this.timeout * 1000) || (form.timeout * 1000) || 300000,
            upload: form.fileUpload ? me.onSuccess : undef
        };
    }
});
/**
 * 重写EXTJS 4.1中的Basic类中afterAction方法，使form submit提交的时候，如果出现架构异常，可以在页面上得到解决
 */
Ext.override(Ext.form.Basic, {
    afterAction: function (action, success) {
        var json = {};
        if (action.waitMsg) {
            var MessageBox = Ext.MessageBox,
                waitMsgTarget = this.waitMsgTarget;
            if (waitMsgTarget === true) {
                this.owner.el.unmask();
            } else if (waitMsgTarget) {
                waitMsgTarget.unmask();
            } else {
                MessageBox.updateProgress(1);
                MessageBox.hide();
            }
        }
        if (success) {
            if (action.reset) {
                this.reset();
            }
            if (!Ext.isEmpty(action) && !Ext.isEmpty(action.response) && !Ext.isEmpty(action.response.responseText)) {
                json = Ext.decode(action.response.responseText);
            }
            Ext.callback(action.success, action.scope || action, [this, action, json]);
            this.fireEvent('actioncomplete', this, action);
        } else {
            if (!Ext.isEmpty(action) && !Ext.isEmpty(action.response) && !Ext.isEmpty(action.response.responseText)) {
                json = Ext.decode(action.response.responseText);
                //业务异常
                if (!json.isException) {
                    Ext.callback(action.exception, action.scope || action, [this, action, json]);
                    //未知异常
                } else if (json.isException) {
                    Ext.callback(action.unknownException, action.scope || action, [this, action, json]);
                }
            } else {
                Ext.callback(action.failure, action.scope || action, [this, action]);
            }
            this.fireEvent('actionfailed', this, action);
        }
    }
});
/**
 * 解决表格头点击之后，没有获得焦点的问题
 */
Ext.override(Ext.grid.header.Container, {
    onHeaderClick: function (header, e, t) {
        header.focus();
        header.fireEvent('headerclick', this, header, e, t);
        this.fireEvent("headerclick", this, header, e, t);
    }
});

/**********************************************************************
 * 公共方法
 */
/**********************************************************************/
Ext.define('CommonFunction', {
    singleton: true,
    /**
     * 表格显示是否启用
     */
    rendererEnable: function (value) {
        /**
         * 是否已禁用，是否已删除，是否已分成
         */
        if (value === false || value === 1) {
            return '<input type="checkbox" Checked onclick="return false" />';
        } else {
            return '<input type="checkbox" unChecked onclick="return false" />';
        }
    },

    renderImg: function (value, isHelper) {
        if (Ext.isEmpty(value)) {
            return '';
        }
        else if (value === false || value === 1) {
            if (isHelper) {
                return '<img src="images/common/helper.png" />';
            }
            return '<img src="images/common/assure.png" />';
        } else {
            return '<img src="images/common/consignment.png"/>';
        }
    },

    renderOrderImg: function (isConsignment, isJsRobot, isHelper) {
        if (Ext.isEmpty(isConsignment)) {
            return '';
        }
        else if (isConsignment == false) {
            if (isHelper) {
                return '<img src="images/common/helper.png" />';
            }
            return '<img src="images/common/assure.png" />';
        } else if (isConsignment == true) {
            return '<img src="images/common/consignment.png"/>';
        }
    }
});
/**********************************************************************
 * 组件
 **********************************************************************/
Ext.apply(Ext.form.VTypes, {
    image: function (val, field) {
        var fileName = /^.*\.(jpg|gif|bmp|png)$/i;
        return fileName.test(val);
    },
    imageText: '上传的图片文件必须是.(jpg|gif|bmp|png)结尾',
    excel: function (val, field) {
        var fileName = /^.*\.(xlsx|xls)$/i;
        return fileName.test(val);
    },
    excelText: '上传的excel文件必须是.(xlsx|xls)结尾'
});

/**
 *    悬浮提示
 */
Ext.ux.Toast = function () {
    var msgCt;

    function createBox(t, s) {
        return ['<div class="msg">',
            '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
            '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
            '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
            '</div>'].join('');
    }

    return {
        msg: function (title, content, delay) {
            if (!msgCt) {
                msgCt = Ext.DomHelper.insertFirst(document.body, {id: 'msg-div'}, true);
            }
            if (Ext.isEmpty(delay)) delay = 5000;
            var m = Ext.DomHelper.append(msgCt, {html: createBox(title, content)}, true);
            m.hide();
            m.slideIn('t').ghost('t', {delay: delay, remove: true});
        }
    }

}();


Ext.define('Gamegold.form.MonthField', {
    extend: 'Ext.form.field.Picker',
    alias: 'widget.monthfield',

    format: "Y-m",

    altFormats: "m/y|m/Y|m-y|m-Y|my|mY|y/m|Y/m|y-m|Y-m|ym|Ym",

    triggerCls: Ext.baseCSSPrefix + 'form-date-trigger',

    matchFieldWidth: false,

    startDay: new Date(),

    initComponent: function () {
        var me = this;

        me.disabledDatesRE = null;

        me.callParent();
    },

    initValue: function () {
        var me = this,
            value = me.value;

        if (Ext.isString(value)) {
            me.value = Ext.Date.parse(value, this.format);
        }
        if (me.value)
            me.startDay = me.value;
        me.callParent();
    },

    rawToValue: function (rawValue) {
        return Ext.Date.parse(rawValue, this.format) || rawValue || null;
    },

    valueToRaw: function (value) {
        return this.formatDate(value);
    },

    formatDate: function (date) {
        return Ext.isDate(date) ? Ext.Date.dateFormat(date, this.format) : date;
    },
    createPicker: function () {
        var me = this,
            format = Ext.String.format;

        return Ext.create('Ext.picker.Month', {
            cls: 'c-monthpicker',
            pickerField: me,
            ownerCt: me.ownerCt,
            renderTo: document.body,
            floating: true,
            shadow: false,
            focusOnShow: true,
            listeners: {
                scope: me,
                cancelclick: me.onCancelClick,
                okclick: me.onOkClick,
                yeardblclick: me.onOkClick,
                monthdblclick: me.onOkClick
            }
        });
    },

    onExpand: function () {
        //this.picker.show();
        this.picker.setValue(this.startDay);
        //

    },

    onOkClick: function (picker, value) {
        var me = this,
            month = value[0],
            year = value[1],
            date = new Date(year, month, 1);
        me.startDay = date;
        me.setValue(date);
        this.picker.hide();
        //this.blur();
    },

    onCancelClick: function () {
        this.picker.hide();
        //this.blur();
    }

});

Ext.define('Gamegold.form.field.DateField', {
    extend: 'Ext.form.field.Date',
    alias: 'widget.gamegolddatefield',
    otherFieldName: '',
    onTriggerClick: function () {
        var me = this,
            value = me.up().getComponent(me.otherFieldName).getValue();
        if (value == null) {
            me.setMaxValue(null);
            me.setMinValue(null);
        }
        me.callParent();
    },
    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    }
});

Ext.define('Gamegold.form.field.RangeDateField', {
    extend: 'Ext.form.FieldContainer',
    alias: ['widget.rangeDateField', 'widget.rangedatefield'],
    alternateClassName: ['Gamegold.form.RangeDateField'],
    /**
     *    @cfg {String} dateType
     *    区间段组件的子组件的类型，默认为Extjs框架原生DateField组件。
     */
    dateType: 'gamegolddatefield',
    /**
     *    @cfg {String} fromName
     *    起始日期组件name属性。
     */
    fromName: '',
    /**
     *    @cfg {String} toName
     *    终止日期组件name属性。
     */
    toName: '',
    /**
     *    @cfg {String} fromValue
     *    起始日期组件初始值。
     */
    fromValue: '',
    /**
     *    @cfg {String} toValue
     *    终止日期组件初始值。
     */
    toValue: '',
    /**
     *    @cfg {Number} dateRange
     *    起始日期和终止日期的间隔。单位：天。
     */
    dateRange: 0,
    /**
     *    @cfg {Boolean} time
     *    是否提供时间选择功能。
     */
    time: true,
    /**
     *    @cfg {Number} increment
     *    区间段类型为TimeField时，时间的增量，以分为单位。
     */
    increment: 30,
    /**
     *    @cfg {String} fieldId
     *    当区间段类型为my97datetimefield组件时，需配置此属性。
     *
     *    此属性为my97datetimefield组件渲染的Dom ID属性，建议配置成复杂的字符串。
     */
    fieldId: '',
    /**
     *    @cfg {Boolean} allowFromBlank
     *    起始日期组件非空标识。true不显示非空标识，false显示非空标识。
     *    系统中，非空标识为组件前端的红色*号。
     */
    allowFromBlank: true,
    /**
     *    @cfg {Boolean} allowToBlank
     *    终止日期组件非空标识。true不显示非空标识，false显示非空标识。
     *    系统中，非空标识为组件前端的红色*号。
     */
    allowToBlank: true,
    /**
     *    @cfg {Boolean} disallowBlank
     *    是否允许值为空，默认允许为空。
     */
    disallowBlank: false,
    /**
     *    @cfg {Boolean} fromEditable
     *    起始日期组件是否允许编辑，默认允许编辑。
     */
    fromEditable: true,
    /**
     *    @cfg {Boolean} toEditable
     *    终止日期组件是否允许编辑，默认允许编辑。
     */
    toEditable: true,
    /**
     *    @cfg {Boolean} editable
     *    是否允许编辑，默认允许编辑。
     *    此属性使起始日期组件和终止日期组件都为可编辑的。可以被单独的可编辑配置fromEditable和toEditable覆盖。
     */
    editable: true,
    /**
     *    @cfg {String} rangeFlag
     *    起始日期组件和终止日期组件间隔Text。
     */
    rangeFlag: '&nbsp;&nbsp;至&nbsp;&nbsp;',
    /**
     * @private
     * 组件初始化方法。
     */
    initComponent: function () {
        var me = this;
        me.layout = 'column';
        me.initContainer();
        me.callParent(arguments);
    },
    /**
     * @private
     * 初始化区间容器。
     */
    initContainer: function () {
        var me = this,
            fristField, toField, secondField,
            firstDateConfig, secondDateConfig,
            hasDateRange;
        if (me.disallowBlank) {
            me.allowFromBlank = false;
            me.allowToBlank = false;
        }
        if (!me.editable) {
            me.fromEditable = false;
            me.toEditable = false;
        }
        fristField = {
            itemId: 'first',
            xtype: "gamegolddatefield",
            name: me.fromName,
            allowBlank: me.allowFromBlank,
            editable: me.fromEditable,
            columnWidth: .5,
            otherFieldName: 'second'
        };
        toField = {
            xtype: 'label',
            html: me.rangeFlag,
            style: {
                textAlign: 'center',
                marginTop: '5px'
            }
        };
        secondField = {
            itemId: 'second',
            xtype: "gamegolddatefield",
            name: me.toName,
            allowBlank: me.allowToBlank,
            editable: me.toEditable,
            columnWidth: .5,
            otherFieldName: 'first'
        };
        hasDateRange = typeof(me.dateRange) == 'number' && me.dateRange != 0;
        me.format = me.format || 'Y-m-d';
        Ext.apply(fristField, {
            format: me.format,
            value: me.fromValue,
            listeners: {
                select: {
                    fn: function (field, value) {
                        field.up().child('#second').setMinValue(value);
                        if (hasDateRange) {
                            var maxValue = Ext.Date.add(value, Ext.Date.DAY, me.dateRange);
                            field.up().child('#second').setMaxValue(maxValue);
                        }
                    }
                },
                scope: me
            }
        });
        Ext.apply(secondField, {
            format: me.format,
            value: me.toValue,
            listeners: {
                select: {
                    fn: function (field, value) {
                        field.up().child('#first').setMaxValue(value);
                        if (hasDateRange) {
                            var minValue = Ext.Date.add(value, Ext.Date.DAY, me.dateRange);
                            field.up().child('#first').setMaxValue(minValue);
                        }
                    }
                }
            }
        });
        me.items = [fristField, toField, secondField];
    }
});


Ext.define('Gamegold.grid.column.LineBreak', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.linebreakcolumn',
    alternateClassName: ['Gamegold.grid.LineBreakColumn'],
    initComponent: function () {
        var me = this,
            customerRenderer = me.renderer;
        if (customerRenderer) {
            me.renderer = function (value, metadata, record, rowIndex, columnIndex, store) {
                value = customerRenderer(value, metadata, record, rowIndex, columnIndex, store);
                value = me.defaultRenderer(value, metadata, record, rowIndex, columnIndex, store);
                return value;
            };
        }
        me.callParent(arguments);
    },
    defaultRenderer: function (value, metadata, record, rowIndex, columnIndex, store) {
        //增加word-break: break-all; CSS实现单词也实现换行
        metadata.style = 'white-space: normal; overflow: visible; word-break: break-all;';
        return value;
    }
});

Ext.define('Gamegold.grid.column.Ellipsis', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.ellipsiscolumn',
    initComponent: function () {
        var me = this,
            customerRenderer = me.renderer;
        if (customerRenderer) {
            me.renderer = function (value, metadata, record, rowIndex, columnIndex, store) {
                value = customerRenderer(value, metadata, record, rowIndex, columnIndex, store);
                value = me.defaultRenderer(value, metadata, record, rowIndex, columnIndex, store);
                return value;
            };
        }
        me.callParent(arguments);
    },

    defaultRenderer: function (value, metadata, record, rowIndex, columnIndex, store) {
        var me = this,
            metaValue = record.get(me.dataIndex);
        metadata.tdAttr = 'data-qtip="' + metaValue + '"';
        return value;
    }
});

Ext.define('Gamegold.grid.column.TipColumn', {
    extend: 'Ext.grid.column.Column',
    alias: ['widget.tipcolumn'],

    /**
     * @cfg {String} tipBodyElement
     * 设置Tip的Body中显示的组件类名
     * 为了实现tip内组件数据的动态绑定，在Grid的信息预览的数据时，一般由某一行的record对象，在执行binData方法的时候，有以下参数可以使用(和grid的Column的renderer属性的参数相同)</br>
     * a)    record 当前行的record对象</br>
     * b)    value 当前单元格的值</br>
     * c)    metadata 当前单元格的元数据</br>
     * d)    store 当前grid的store对象</br>
     * e)    view 当前view
     */
    tipBodyElement: null,

    /**
     * @cfg {Ext.tip.Tooltip} tipConfig
     * Tip的具体配置:
     * trackMouse: 是否随着鼠标移动
     * hideDelay: Tip的隐藏延迟时间(单位:ms)
     */
    tipConfig: null,

    /**
     * @private
     * @cfg tipBody
     * tip内显示的对象
     */
    tipBody: null,

    /**
     * @private
     * @cfg tip
     * tip显示框对象
     */
    tip: null,

    /**
     * @private
     * @cfg hasListener
     * 是否已经绑定事件
     */
    hasListener: false,

    columnIndexMap: new Ext.util.HashMap(),

    addMouseOverLis: function (value, metadata, record, store, view, columnIndex) {
        var me = this;
        store.each(function (record, index, length) {
            var td = Ext.get(record.internalId + '-td-' + index + columnIndex);
            if (td == null) {
                return;
            }
            td.on('mouseover', function (e, element, eOpts) {
                if (me.tipBody && (typeof me.tipBody.bindData) != 'undefined') {
                    var result = me.tipBody.bindData(record, value, metadata, store, view);
                    if (!Ext.isEmpty(result)) {
                        if (!result) {
                            return;
                        }
                    }
                }
                var x = e.browserEvent.clientX | e.browserEvent.pageX,
                    y = e.browserEvent.clientY | e.browserEvent.pageY;
                me.tip.showAt([x + 10, y + 10]);
            });
        });
    },

    initComponent: function () {
        var me = this,
            customerRenderer = me.renderer;
        if (customerRenderer) {
            me.renderer = function (value, metadata, record, rowIndex, columnIndex, store, view) {
                value = customerRenderer(value, metadata, record, rowIndex, columnIndex, store, view);
                value = me.defaultRenderer(value, metadata, record, rowIndex, columnIndex, store, view);
                return value;
            };
        }
        ;
        me.defaultRenderer = function (value, metadata, record, rowIndex, columnIndex, store, view) {
            var tdId = record.internalId + '-td-' + rowIndex + columnIndex;
            me.columnIndexMap.add(columnIndex, columnIndex);
            if (Ext.isEmpty(me.tipBody)) {
                if (!Ext.isEmpty(me.tipBodyElement)) {
                    me.tipBody = Ext.create(me.tipBodyElement);
                }
            }
            if (me.tip == null) {
                if (me.tipConfig == null) {
                    me.tipConfig = {};
                }
                if (!Ext.isEmpty(me.tipBody)) {
                    me.tipConfig.items = [me.tipBody];
                }
                me.tip = Ext.create('Ext.tip.ToolTip', me.tipConfig);
            }
            metadata.tdAttr = "id=" + '"' + tdId + '"';
            if (!me.hasListener) {
                var evenObject, evenName;
                if (store.getProxy().type == 'ajax' || store.getProxy().type == 'jsonp' || store.getProxy().type == 'direct') {
                    evenObject = store;
                    evenName = 'load';
                } else {
                    // 修改之前view的事件 'viewready' 为 'refresh'
                    evenObject = view;
                    evenName = 'refresh';
                }
                evenObject.on(evenName, function () {
                    me.addMouseOverLis(value, metadata, record, store, view, columnIndex);
                });
                view.up('grid').on('sortchange', function () {
                    me.addMouseOverLis(value, metadata, record, store, view, columnIndex);
                });
                store.on('datachanged', function () {
                    me.addMouseOverLis(value, metadata, record, store, view, columnIndex);
                });
                me.hasListener = true;
            }
            return value;
        };
        me.callParent(arguments);
    }
});

Ext.define('Gamegold.ux.ClearValuePlugin', {
    alias: 'plugin.clearvalueplugin',
    /**
     * @cfg {String} trigger1Cls
     * 清除按钮的样式。
     */
    trigger1Cls: Ext.baseCSSPrefix + 'form-clear-trigger',
    /**
     * @cfg {String} trigger2Cls
     * 查询按钮的样式。
     */
    trigger2Cls: Ext.baseCSSPrefix + 'form-search-trigger',
    /**
     * @private
     * 构造函数，初始化此插件的配置项。
     */
    constructor: function (config) {
        Ext.apply(this, config);
    },
    /**
     * @private
     * 插件初始化方法，适用于combobox组件上。在组件的initComponent方法执行完毕后调用。
     */
    init: function (combo) {
        var me = this;
        me.pluginId = 'clearvalueplugin';
        combo.hasSearch = false;
        combo.trigger1Cls = me.trigger1Cls;
        combo.trigger2Cls = me.trigger2Cls;
        combo.onTrigger1Click = me.onTrigger1Click;
        combo.onTrigger2Click = me.onTrigger2Click;
        combo.on('afterrender', me.afterRender, combo);
    },
    /**
     * @private
     * combobox组件afterrender监听事件，用于使清除按钮在渲染后处于隐藏状态。
     */
    afterRender: function () {
        this.triggerCell.item(0).setDisplayed(false);
    },
    /**
     * @protected
     * 清除按钮的点击事件。
     */
    onTrigger1Click: function () {
        var me = this;
        if (me.hasSearch) {
            me.setValue('');
            me.hasSearch = false;
            me.triggerCell.item(0).setDisplayed(false);
            me.updateLayout();
        }
    },
    /**
     * @protected
     * 查询按钮的点击事件。
     */
    onTrigger2Click: function () {
        var me = this;
        me.onTriggerClick();
        me.hasSearch = true;
        me.triggerCell.item(0).setDisplayed(true);
    }
});

Ext.define('Gamegold.grid.plugin.RowExpander', {
    extend: 'Ext.AbstractPlugin',
    lockableScope: 'normal',

    requires: [
        'Ext.grid.feature.RowBody',
        'Ext.grid.feature.RowWrap'
    ],

    alias: 'plugin.gamegoldrowexpander',

    rowBodyTpl: null,

    rowBodyElement: null,

    elementIdMap: null,

    beforeRowIndex: null,

    rowsExpander: false,

    header: false,

    /**
     * @cfg {Boolean} expandOnEnter
     * <tt>true</tt> to toggle selected row(s) between expanded/collapsed when the enter
     * key is pressed (defaults to <tt>true</tt>).
     */
    expandOnEnter: true,

    /**
     * @cfg {Boolean} expandOnDblClick
     * <tt>true</tt> to toggle a row between expanded/collapsed when double clicked
     * (defaults to <tt>true</tt>).
     */
    expandOnDblClick: true,

    /**
     * @cfg {Boolean} selectRowOnExpand
     * <tt>true</tt> to select a row when clicking on the expander icon
     * (defaults to <tt>false</tt>).
     */
    selectRowOnExpand: false,

    rowBodyTrSelector: '.x-grid-rowbody-tr',
    rowBodyHiddenCls: 'x-grid-row-body-hidden',
    rowCollapsedCls: 'x-grid-row-collapsed',

    addCollapsedCls: {
        before: function (values, out) {
            var me = this.rowExpander;
            if (!me.recordsExpanded[values.record.internalId]) {
                values.itemClasses.push(me.rowCollapsedCls);
            }
        },
        priority: 500
    },

    /**
     * @event expandbody
     * **Fired through the grid's View**
     * @param {HTMLElement} rowNode The &lt;tr> element which owns the expanded row.
     * @param {Ext.data.Model} record The record providing the data.
     * @param {HTMLElement} expandRow The &lt;tr> element containing the expanded data.
     */
    /**
     * @event collapsebody
     * **Fired through the grid's View.**
     * @param {HTMLElement} rowNode The &lt;tr> element which owns the expanded row.
     * @param {Ext.data.Model} record The record providing the data.
     * @param {HTMLElement} expandRow The &lt;tr> element containing the expanded data.
     */

    setCmp: function (grid) {
        var me = this,
            features;

        me.callParent(arguments);

        ////
        me.elementIdMap = new Ext.util.HashMap();

        me.recordsExpanded = {};
        // <debug>
        if (!me.rowBodyTpl && !me.rowBodyElement) {
            Ext.Error.raise("'rowBodyTpl' and 'rowBodyElement' of A property must configuration.");
        }
        // </debug>
        if (me.rowBodyTpl) {
            me.rowBodyTpl = Ext.XTemplate.getTpl(me, 'rowBodyTpl');
            rowBodyTpl = me.rowBodyTpl;
            features = [{
                ftype: 'rowbody',
                lockableScope: 'normal',
                rowBodyTpl: me.rowBodyTpl,
                recordsExpanded: me.recordsExpanded,
                rowBodyHiddenCls: me.rowBodyHiddenCls,
                rowCollapsedCls: me.rowCollapsedCls,
                setupRowData: me.getRowBodyFeatureData,
                setup: me.setup,
                getRowBodyContents: function (record) {
                    return rowBodyTpl.applyTemplate(record.getData());
                }
            }, {
                ftype: 'rowwrap',
                lockableScope: 'normal'
            }];
        }
        if (me.rowBodyElement) {
            rowBodyElement = me.rowBodyElement;
            features = [{
                ftype: 'rowbody',
                lockableScope: 'normal',
                rowBodyElement: me.rowBodyElement,
                recordsExpanded: me.recordsExpanded,
                rowBodyHiddenCls: me.rowBodyHiddenCls,
                rowCollapsedCls: me.rowCollapsedCls,
                setupRowData: me.getRowBodyFeatureData,
                setup: me.setup,
                getRowBodyContents: function (data) {
                    return "<div id='" + data + "' class='x-row-embedded'></div>";
                }
            }, {
                ftype: 'rowwrap',
                lockableScope: 'normal'
            }];
        }
        if (grid.features) {
            grid.features = Ext.Array.push(features, grid.features);
        } else {
            grid.features = features;
        }
        // NOTE: features have to be added before init (before Table.initComponent)
    },

    init: function (grid) {
        var me = this,
            reconfigurable = grid,
            view, lockedView;

        me.callParent(arguments);
        me.grid = grid;
        view = me.view = grid.getView();
        // Columns have to be added in init (after columns has been used to create the headerCt).
        // Otherwise, shared column configs get corrupted, e.g., if put in the prototype.
        if (me.header) {
            me.addExpander();
        }

        // Bind to view for key and mouse events
        // Add row processor which adds collapsed class
        me.bindView(view);
        view.addRowTpl(me.addCollapsedCls).rowExpander = me;

        // If the owning grid is lockable, then disable row height syncing - we do it here.
        // Also ensure the collapsed class is applied to the locked side by adding a row processor.
        if (grid.ownerLockable) {
            // If our client grid is the normal side of a lockable grid, we listen to its lockable owner's beforereconfigure
            reconfigurable = grid.ownerLockable;
            reconfigurable.syncRowHeight = false;
            lockedView = reconfigurable.lockedGrid.getView();

            // Bind to locked view for key and mouse events
            // Add row processor which adds collapsed class
            me.bindView(lockedView);
            lockedView.addRowTpl(me.addCollapsedCls).rowExpander = me;

            // Refresh row heights of expended rows on the locked (non body containing) side upon lock & unlock.
            // The locked side's expanded rows will collapse back because there's no body there
            reconfigurable.mon(reconfigurable, 'columnschanged', me.refreshRowHeights, me);
            reconfigurable.mon(reconfigurable.store, 'datachanged', me.refreshRowHeights, me);
        }
        reconfigurable.on('beforereconfigure', me.beforeReconfigure, me);

        if (grid.ownerLockable && !grid.rowLines) {
            // grids without row lines can gain a border when focused.  When they do, the
            // stylesheet adjusts the padding of the cells so that the height of the row
            // does not change. It is necessary to refresh the row heights for lockable
            // grids on focus to keep the height of the expander cells in sync.
            view.on('rowfocus', me.refreshRowHeights, me);
        }

        if (me.rowBodyElement != null) {
            view.on('refresh', function (store, eOpts) {
                me.cleanElementIdMap();
            }, this);
        }
    },

    beforeReconfigure: function (grid, store, columns, oldStore, oldColumns) {
        var expander = this.getHeaderConfig();
        expander.locked = true;
        columns.unshift(expander);
    },

    /**
     * @private
     * Inject the expander column into the correct grid.
     *
     * If we are expanding the normal side of a lockable grid, poke the column into the locked side
     */
    addExpander: function () {
        var me = this,
            expanderGrid = me.grid,
            expanderHeader = me.getHeaderConfig();

        // If this is the normal side of a lockable grid, find the other side.
        if (expanderGrid.ownerLockable) {
            expanderGrid = expanderGrid.ownerLockable.lockedGrid;
            expanderGrid.width += expanderHeader.width;
        }
        expanderGrid.headerCt.insert(0, expanderHeader);
    },

    getRowBodyFeatureData: function (record, idx, rowValues) {
        var me = this
        me.self.prototype.setupRowData.apply(me, arguments);

        if (me.rowBodyTpl) {
            rowValues.rowBody = me.getRowBodyContents(record);
        }
        if (me.rowBodyElement) {
            rowValues.rowBody = me.getRowBodyContents(record.internalId + "-rowbody");
        }
        rowValues.rowBodyCls = me.recordsExpanded[record.internalId] ? '' : me.rowBodyHiddenCls;
    },

    setup: function (rows, rowValues) {
        var me = this;
        me.self.prototype.setup.apply(me, arguments);
        // If we are lockable, the expander column is moved into the locked side, so we don't have to span it
        if (!me.grid.ownerLockable) {
            rowValues.rowBodyColspan -= 1;
        }
        ////ztjie 增加头的话增加一列
        rowValues.rowBodyColspan += 1;
    },

    bindView: function (view) {
        if (this.expandOnEnter) {
            view.on('itemkeydown', this.onKeyDown, this);
        }
        if (this.expandOnDblClick) {
            view.on('itemdblclick', this.onDblClick, this);
        }
    },

    onKeyDown: function (view, record, row, rowIdx, e) {
        if (e.getKey() == e.ENTER) {
            var ds = view.store,
                sels = view.getSelectionModel().getSelection(),
                ln = sels.length,
                i = 0;

            for (; i < ln; i++) {
                rowIdx = ds.indexOf(sels[i]);
                this.toggleRow(rowIdx, sels[i]);
            }
        }
    },

    onDblClick: function (view, record, row, rowIdx, e) {
        if (row.parentElement.id.search(view.id) < 0) {
            return;
        }
        this.toggleRow(rowIdx, record);
    },

    toggleRow: function (rowIdx, record) {
        var me = this,
            view = me.view,
            rowNode = view.getNode(rowIdx),
            row = Ext.fly(rowNode, '_rowExpander'),
            nextBd = row.down(me.rowBodyTrSelector, true),
            isCollapsed = row.hasCls(me.rowCollapsedCls),
            addOrRemoveCls = isCollapsed ? 'removeCls' : 'addCls',
            ownerLock, rowHeight, fireView;

        // Suspend layouts because of possible TWO views having their height change
        Ext.suspendLayouts();
        row[addOrRemoveCls](me.rowCollapsedCls);
        Ext.fly(nextBd)[addOrRemoveCls](me.rowBodyHiddenCls);
        me.recordsExpanded[record.internalId] = isCollapsed;

        if (isCollapsed && me.rowBodyElement != null) {
            rowBodyId = record.internalId + '-rowbody',
                rowBodyElementId = record.internalId + '-rowbody-element';
            if (me.rowBodyElement && !me.elementIdMap.containsKey(rowBodyElementId)) {
                var rowBodyElement = Ext.create(me.rowBodyElement, {
                    id: rowBodyElementId
                });
                rowBodyElement.on('boxready', function () {
                    view.refreshSize();
                });
                rowBodyElement.render(rowBodyId);
                this.elementIdMap.add(rowBodyElement.id, rowBodyElement);
            }
            var rowBodyElement = this.elementIdMap.get(rowBodyElementId);
            //在定义行体的时候，装入的对象要如果要进行数据的bind，可以通过实现这个方法，进行实现
            if (rowBodyElement && (typeof rowBodyElement.bindData) != 'undefined') {
                rowBodyElement.bindData(record, me.grid, rowBodyElement);
            }
        }
        view.refreshSize();
        if (!me.rowsExpander) {
            if (me.beforeRowIndex != null && me.beforeRowIndex != rowIdx) {
                Ext.resumeLayouts(true);
                var beforeRowNode = view.getNode(me.beforeRowIndex),
                    beforeRecord = view.getRecord(beforeRowNode),
                    beforeRow = Ext.fly(beforeRowNode, '_rowExpander'),
                    beforeNextBd = row.down(me.rowBodyTrSelector, true);
                Ext.suspendLayouts();
                row['addCls'](me.rowCollapsedCls);
                Ext.fly(beforeNextBd)['addCls'](me.rowBodyHiddenCls);
                me.recordsExpanded[beforeRecord.internalId] = false;
                view.refreshSize();
            }
            me.beforeRowIndex = rowIdx;
        }

        // Sync the height and class of the row on the locked side
        if (me.grid.ownerLockable) {
            ownerLock = me.grid.ownerLockable;
            fireView = ownerLock.getView();
            view = ownerLock.lockedGrid.view;
            rowHeight = row.getHeight();
            row = Ext.fly(view.getNode(rowIdx), '_rowExpander');
            row.setHeight(rowHeight);
            row[addOrRemoveCls](me.rowCollapsedCls);
            view.refreshSize();
        } else {
            fireView = view;
        }
        fireView.fireEvent(isCollapsed ? 'expandbody' : 'collapsebody', row.dom, record, nextBd);
        // Coalesce laying out due to view size changes
        Ext.resumeLayouts(true);
    },

    cleanElementIdMap: function () {
        var me = this,
            view = me.view,
            rowNodes = view.getNodes(),
            elementIdMap = me.elementIdMap;
        if (elementIdMap == null) {
            return;
        }
        Ext.Array.each(rowNodes, function (rowNode, index, rowNodes) {
            var record = view.getRecord(rowNode),
                row = Ext.fly(rowNode, '_rowExpander'),
                nextBd = row.down(me.rowBodyTrSelector, true);
            if (row != null && !row.hasCls(me.rowCollapsedCls)) {
                Ext.suspendLayouts();
                row['addCls'](me.rowCollapsedCls);
                if (nextBd != null) {
                    Ext.fly(nextBd)['addCls'](me.rowBodyHiddenCls);
                }
                me.recordsExpanded[record.internalId] = false;
                Ext.resumeLayouts(true);
            }
        });
        for (var i = 0; i < elementIdMap.getKeys().length; i++) {
            me.cleanComponent(elementIdMap.getValues()[i]);
            var header = Ext.ComponentManager.get(elementIdMap.getKeys()[i] + '_header');
            this.cleanComponent(header);
            Ext.ComponentManager.unregister(header);
            header = null;
        }
        elementIdMap.clear();
        me.beforeRowIndex = null;
    },

    cleanComponent: function (cmp) {
        if (typeof cmp == "undefined") {
            return;
        }
        if (typeof cmp == "string") {
            cmp = Ext.ComponentManager.get(cmp);
            if (!cmp) {
                //如果在DOM树上有该节点，进行DOM树节点的删除
                if (cmp != null) {
                    var cmpDom = Ext.getDom(cmp);
                    Ext.removeNode(cmpDom);
                }
                return;
            }
        }
        //清空ComponentManager内的注册项
        var cmpArray = cmp.removeAll();
        //清空store
        for (var i = 0; i < cmpArray.length; i++) {
            if (cmpArray[i].store) {
                cmpArray[i].store.removeAll();
                Ext.data.StoreManager.unregister(cmpArray[i].store);
            }
        }
        cmpArray = null;
        Ext.ComponentManager.unregister(cmp);
        //如果在DOM树上有该节点，进行DOM树节点的删除
        if (cmp.id != null) {
            var cmpDom = Ext.getDom(cmp.id);
            Ext.removeNode(cmpDom);
        }
    },

    // refreshRowHeights often gets called in the middle of some complex processing.
    // For example, it's called on the store's datachanged event, but it must execute
    // *after* other objects interested in datachanged have done their job.
    // Or it's called on column lock/unlock, but that could be just the start of a cross-container
    // drag/drop of column headers which then moves the column into its final place.
    // So this throws execution forwards until the idle event.
    refreshRowHeights: function () {
        Ext.globalEvents.on({
            idle: this.doRefreshRowHeights,
            scope: this,
            single: true
        });
    },

    doRefreshRowHeights: function () {
        var me = this,
            recordsExpanded = me.recordsExpanded,
            key, record,
            lockedView = me.grid.ownerLockable.lockedGrid.view,
            normalView = me.grid.ownerLockable.normalGrid.view,
            normalRow,
            lockedRow,
            lockedHeight,
            normalHeight;

        for (key in recordsExpanded) {
            if (recordsExpanded.hasOwnProperty(key)) {
                record = this.view.store.data.get(key);
                lockedRow = lockedView.getNode(record, false);
                normalRow = normalView.getNode(record, false);
                lockedRow.style.height = normalRow.style.height = '';
                lockedHeight = lockedRow.offsetHeight;
                normalHeight = normalRow.offsetHeight;
                if (normalHeight > lockedHeight) {
                    lockedRow.style.height = normalHeight + 'px';
                } else if (lockedHeight > normalHeight) {
                    normalRow.style.height = lockedHeight + 'px';
                }
            }
        }
    },

    getHeaderConfig: function () {
        var me = this;

        return {
            width: 24,
            lockable: false,
            sortable: false,
            resizable: false,
            draggable: false,
            hideable: false,
            menuDisabled: true,
            tdCls: Ext.baseCSSPrefix + 'grid-cell-special',
            innerCls: Ext.baseCSSPrefix + 'grid-cell-inner-row-expander',
            renderer: function (value, metadata) {
                // Only has to span 2 rows if it is not in a lockable grid.
                if (!me.grid.ownerLockable) {
                    metadata.tdAttr += ' rowspan="2"';
                }
                return '<div class="' + Ext.baseCSSPrefix + 'grid-row-expander"></div>';
            },
            processEvent: function (type, view, cell, rowIndex, cellIndex, e, record) {
                if (type == "mousedown" && e.getTarget('.x-grid-row-expander')) {
                    me.toggleRow(rowIndex, record);
                    return me.selectRowOnExpand;
                }
            }
        };
    }
});

if (Gamegold === undefined) {
    Ext.ns('Gamegold');
}
;
Gamegold.JsLoader = function () {
    this.load = function (url) {
        var me = this,
            scripts = document.getElementsByTagName("script"),
            len = scripts.length,
            i, script, head;
        for (i = 0; i < len; i++) {
            if (scripts[i].src && scripts[i].src.indexOf(url) != -1) {
                me.onSuccess();
                return;
            }
        }
        script = document.createElement("script");
        script.type = "text/javascript";
        script.src = url;
        head = document.getElementsByTagName("head")[0];
        try {
            head.appendChild(script);
            script.onload = script.onreadystatechange = function () {
                if (script.readyState && script.readyState != 'loaded' && script.readyState != 'complete') return;
                script.onreadystatechange = script.onload = null;
                me.onSuccess();
            }
        } catch (e) {
            if (typeof(me.onFailure) == 'function') {
                me.onFailure();
            } else {
                throw "Failed to load javaScript file dynamical!";
            }
        }
    }
};

Ext.define('Gamegold.ux.ZeroClipboardPlugin', {
    alias: 'plugin.zeroclipboardplugin',
    /**
     * @cfg {Oject} targetCmpName
     * 如果复制内容为表单域的value，且绑定ZeroClipboard的元素位于form内，则配置此属性为表单域的name属性即可。
     */
    targetCmpName: null,
    /**
     * @cfg {Function} targetFun
     * 如果复制内容为function返回的值，则配置此属性为function对象。
     */
    targetFun: null,
    /**
     * @cfg {String} targetValue
     * 如果复制内容为指定文本，则配置此属性为指定文本。
     */
    targetValue: null,
    /**
     * @private
     * 构造函数，初始化此插件的配置项。
     */
    constructor: function (config) {
        var me = this;
        Ext.apply(me, config);
    },

    /**
     * @private
     * 插件初始化方法，适用于Button组件上。在组件的initComponent方法执行完毕后调用。
     */
    init: function (component) {
        var me = this;
        me.component = component;
        me.component.on('boxready', function () {
            me.initZeroClipboard(component);
        });
    },

    /**
     * @method
     * 初始化ZeroClipboard。
     * @return {Object} ZeroClipboard对象。
     */
    initZeroClipboard: function (component) {
        var me = this;
        me.clip = new ZeroClipboard(component.getEl().dom, {
            moviePath: "./lib/components/zeroclipboard/ZeroClipboard.swf",
            cacheBust: false
        });
        me.clip.setHandCursor(true);
        me.clip.addEventListener("mouseOver", Ext.bind(me.onMouseOver, me));
        me.clip.addEventListener('complete', function (client, text) {
            Ext.ux.Toast.msg("提示", "内容已经复制至剪切板");
        });
    },

    /**
     * @method
     * 绑定ZeroClipboard于组件的Element对象上。
     * @return {Object} ZeroClipboard对象。
     */
    /*glueZeroClipboard: function() {
     var me = this;
     if(me.clip) {
     me.clip.destroy();
     me.clip.clip(me.component.getEl().dom);
     }
     return me.clip;
     },*/
    /**
     * @private
     * ZeroClipboard对象的mouseOver监听事件。鼠标移动时初始化ZeroClipboard对象的复制文本。
     */
    onMouseOver: function () {
        var me = this,
            value;
        if (me.targetCmpName) {
            var targetCmp = me.component.up('form').getForm().findField(me.targetCmpName);
            value = targetCmp.getValue();
        } else if (me.targetFun) {
            value = me.targetFun(me.component);
        } else if (me.targetValue) {
            value = me.targetValue;
        }
        me.clip.setText(value);
    },
    /**
     * @private
     * 销毁插件相关的属性和对象。该方法会在组件销毁的时候自动调用。
     */
    destroy: function () {
        var me = this;
        me.component.clearListeners();
        me.clip.destroy();

        delete me.component;
        delete me.clip;
    }
});

/**
 *    @docauthor ztjie
 *
 *    此插件使用于按钮上，使按钮在点击后一定时间内处于不可用状态（disabled），从而防止用户连续点击按钮。
 *
 *    通常可以用来防止用户连续提交而造成的服务器性能问题。
 *
 *    此插件的功能和设计参考12306订票查询按钮。
 *
 *    # 插件示例
 *    <pre><code>
 *    @example
 *    Ext.create('Ext.button.Button', {
 *		frame: true,
 *		text: '阻止按钮连续点击插件',
 *		maxWidth: 200,
 *		height: 30,
 *		//插件配置代码
 *		plugins: {
 *			ptype: 'buttondisabledplugin',
 *			seconds: 5
 *		},
 *		renderTo : Ext.getBody()
 *	});
 *    </code></pre>
 */
Ext.define('Gamegold.button.ButtonDisabledPlugin', {
    alias: ['plugin.buttonlimitingplugin', 'plugin.buttondisabledplugin'],
    /**
     * @cfg {Number} seconds
     * 设置按钮不可用状态持续的时间（单位：秒）。
     */
    seconds: 2,
    /**
     * @private
     * 构造函数，初始化此插件的配置项。
     */
    constructor: function (config) {
        Ext.apply(this, config);
    },
    /**
     * @private
     * 插件初始化方法，适用于Button组件上。在组件的initComponent方法执行完毕后调用。
     */
    init: function (button) {
        var me = this;
        me.button = button;
        me.getDelayedTask();
        button.on('click', me.onClickButton, me);
    },
    /**
     * @private
     * 按钮上的click监听事件，点击后按钮变成不可用状态，利用延迟任务组件，一定时间后恢复可用状态。
     * @return {Boolean} 返回true，继续执行其他的click监听事件。
     */
    onClickButton: function () {
        var me = this,
            button = me.button;
        button.setDisabled(true);
        me.task.delay(me.seconds * 1000);
        return true;
    },
    /**
     * @method
     * 设置按钮disabled状态。
     * @param {Boolean} 传入false设置按钮可用，传入true设置按钮不可用。
     * @return {Ext.button.Button} 设置插件的按钮对象。
     */
    setButtonDisabled: function (disabled) {
        var me = this,
            button = me.button;
        disabled = disabled || false;
        button.setDisabled(disabled);
        return button;
    },
    /**
     * @method
     * 获取插件的任务延迟对象。该方法在插件初始化时调用。
     * @return {Ext.util.DelayedTask} 插件的任务延迟（Ext.util.DelayedTask）对象。
     */
    getDelayedTask: function () {
        var me = this;
        if (!me.task) {
            me.task = new Ext.util.DelayedTask(me.setButtonDisabled, me);
        }
        return me.task;
    },
    /**
     * @private
     * 销毁插件相关的属性和对象。该方法会在组件销毁的时候自动调用。
     */
    destory: function () {
        var me = this,
            button = me.button;
        Ext.destroy(me.task);
        delete me.button;
        delete me.task;
    }
});


/**
 *    @docauthor ztjie
 *
 *    单选选择器，公共选择器之一。
 *
 *    封装combobox，提供触发按钮查询、回车查询、分页栏、条目模板自定义、查询参数定义等配置功能。
 *
 */
Ext.define('Gamegold.selector.SingleSelector', {
    extend: 'Ext.form.ComboBox',
    alias: ['widget.singleselector', 'widget.dynamiccomboselector'],
    alternateClassName: ['Gamegold.commonselector.DynamicComboSelector'],
    /**
     * @cfg {String} triggerCls
     * 触发按钮样式。
     */
    triggerCls: Ext.baseCSSPrefix + 'form-search-trigger',
    /**
     * @cfg {Number} listWidth
     * 下拉列表宽度。
     */
    listWidth: this.width,
    /**
     * @cfg {Boolean} multiSelect
     * 是否支持多选。
     */
    multiSelect: false,
    /**
     * @cfg {Boolean} isPaging
     * 是否支持分页。
     */
    isPaging: false,
    /**
     * @cfg {Boolean} isEnterQuery
     * 是否支持回车查询。
     */
    isEnterQuery: false,
    /**
     * @cfg {String} displayField
     * 显示的字段。
     */
    displayField: null,
    /**
     * @cfg {String} valueField
     * 显示字段对应的关键字。
     */
    valueField: null,
    /**
     * @cfg {String} showContent
     * 下拉条目的显示模板。
     */
    showContent: null,
    /**
     * @cfg {Object} queryParam
     * 查询参数。
     */
    queryParam: null,
    /**
     * @cfg {String} triggerAction
     * 触发动作。
     */
    triggerAction: 'query',
    /**
     * @cfg {Number} minChars
     * 查询条件显示结果的最小字符数。
     */
    minChars: 0,
    /**
     * @cfg {Boolean} hideTrigger
     * 是否隐藏触发按钮。
     */
    hideTrigger: false,
    /**
     * @cfg {Object/String} plugins
     * 插件对象或者ptype。
     */
    plugins: 'clearvalueplugin',
    /**
     * @cfg {Boolean} queryAllFlag
     * 是否支持查询全部。
     */
    queryAllFlag: true,
    /**
     * @cfg {Number} validValueLength
     * 查询条件的最小字符串长度。
     */
    validValueLength: 0,
    /**
     * @cfg {Object} listConfig
     * 下拉列表的配置对象。
     *   - {@link Ext.view.BoundList#cls} - 默认为空。
     *   - {@link Ext.view.BoundList#emptyText} - 默认为空字符串。
     *   - {@link Ext.view.BoundList#itemSelector} - 默认为BoundList定义的字符串。
     *   - {@link Ext.view.BoundList#loadingText} - 默认为 "Loading..."
     *   - {@link Ext.view.BoundList#minWidth} - 默认为 70
     *   - {@link Ext.view.BoundList#maxWidth} - 默认为 `undefined`
     *   - {@link Ext.view.BoundList#maxHeight} - 默认为 `300`
     *   - {@link Ext.view.BoundList#resizable} - 默认为 `false`
     *   - {@link Ext.view.BoundList#shadow} - 默认为 `'sides'`
     *   - {@link Ext.view.BoundList#width} - 默认为 `undefined` （自动匹配宽度，如果combobox的{@link #matchFieldWidth}属性设置为true）
     */
    listConfig: {
        getInnerTpl: function () {
            var comboObj = this.up('combo'),
                content = comboObj.showContent,
                keyArr = comboObj.getKeyWords(content);
            if (keyArr.length == 2) {
                return Ext.String.format('<div class="common-combo-item"><span>{{1}}</span><span>{{0}}</span></div>', keyArr[0], keyArr[1]);
            } else {
                return this.up('combo').showContent;
            }
        }
    },
    /**
     * @method
     * 获取下拉条目模板中的关键字。
     * @param {String} 下拉条目模板。
     * @return {Array} 关键字数组。
     */
    getKeyWords: function (content) {
        var conArr = content.split("}"),
            conEle, result = [],
            conIndex;
        for (var i = 0, len = conArr.length; i < len; i++) {
            conEle = conArr[i];
            conIndex = conEle.indexOf("{");
            if (conIndex != -1) {
                result.push(conEle.substring(conEle.indexOf("{") + 1, conEle.length));
            }
        }
        return result;
    },
    getValueModel: function () {
        var models = this.valueModels;
        if (Ext.isEmpty(models) && models.length > 0) {
            return models[0];
        } else {
            return null;
        }
    },
    /**
     * @private
     * combobox的change监听事件。
     */
    getChange: function (combo, newValue, oldValue) {
        if (combo.isExpanded == true) {
            combo.collapse();
        }
    },
    /**
     * @private
     * combobox的keypress监听事件。
     */
    getKeyPress: function (combo, event, eOpts) {
        var me = this;
        if (event.getKey() == event.ENTER) {
            if (me.queryAllFlag) {
                me.enterQueryAction();
            } else {
                if (Ext.isEmpty(me.getValue())) {
                    me.setActiveErrors(["请输入查询条件!"]);
                    this.doComponentLayout();
                } else {
                    me.enterQueryAction();
                }
            }
        }
    },
    /**
     * @private
     * combobox的store对象的beforeLoad监听事件。
     */
    getBeforeLoad: function (st, operation, e) {
        var me = this,
            searchParams = operation.params;
        if (Ext.isEmpty(searchParams)) {
            searchParams = {};
            Ext.apply(operation, {
                params: searchParams
            });
        }
        searchParams[me.queryParam] = me.rawValue;
    },
    /**
     * @protected
     * 初始化监听事件方法。
     */
    initEvents: function () {
        var me = this;
        me.callParent(arguments);
        if (me.isEnterQuery == true) {
            me.mon(me, 'change', me.getChange, me);
            me.mon(me, 'keypress', me.getKeyPress, me);
        }
        me.mon(me.store, 'beforeLoad', me.getBeforeLoad, me);
    },
    /**
     * @private
     * 组件初始化方法。
     */
    initComponent: function () {
        var me = this;
        me.on('expand', function (combo, eOpts) {
            combo.getPicker().minWidth = me.listWidth;
            combo.getPicker().setWidth(me.listWidth);
        });
        if (me.isPaging == true) {
            me.pageSize = me.store.pageSize;
        }
        if (me.showContent == null) {
            me.showContent = '{' + me.displayField + '}';
        }
        if (me.isEnterQuery == true) {
            me.enableKeyEvents = true;
            me.queryDelay = 1000000;
        }
        this.callParent(arguments);
    },
    /**
     * @method
     * combobox触发按钮的绑定事件。
     */
    onTriggerClick: function () {
        var me = this;
        if (me.queryAllFlag) {
            me.queryAction();
        } else {
            if (Ext.isEmpty(me.getValue())) {
                me.setActiveErrors(["请输入查询条件!"]);
                this.doComponentLayout();
            } else {
                me.queryAction();
            }
        }
    },
    /**
     * @method
     * combobox回车键的监听事件。
     * @param {Ext.form.ComboBox} combobox组件。
     */
    enterQueryAction: function () {
        var me = this,
            clearvalueplugin = me.getPlugin('clearvalueplugin'),
            value = me.getValue();
        if (me.validValueLength != 0 && (!Ext.isEmpty(value) && value.length < me.validValueLength)) {
            me.setActiveErrors(["请输入至少" + me.validValueLength + "个字符!"]);
            me.doComponentLayout();
            return;
        }
        me.store.loadPage(1, {
            scope: this,
            callback: function (records, operation, success) {
                if (records.length > 0) {
                    me.expand();
                } else {
                    operation.params[me.queryParam] = "";
                    me.lastQuery = undefined;
                }
            }
        });
        if (!Ext.isEmpty(clearvalueplugin)) {
            me.hasSearch = true;
            me.triggerCell.item(0).setDisplayed(true);
        }
    },
    /**
     * @method
     * combobox查询事件。
     */
    queryAction: function () {
        var me = this,
            value;
        if (!me.readOnly && !me.disabled) {
            if (me.isExpanded) {
                me.collapse();
            } else {
                me.onFocus({});
                value = me.getValue();
                if (me.validValueLength != 0 && (!Ext.isEmpty(value) && value.length < me.validValueLength)) {
                    me.setActiveErrors(["请输入至少" + me.validValueLength + "个字符!"]);
                    me.doComponentLayout();
                    return;
                }
                if (me.triggerAction === 'all') {
                    me.doQuery(me.allQuery, true);
                } else {
                    me.doQuery(me.getRawValue(), false, true);
                }
            }
            me.inputEl.focus();
        }
    }
});

Ext.define('Gamegold.selector.LinkedSelector', {
    extend: 'Gamegold.selector.SingleSelector',
    alias: ['widget.linkedselector'],
    /**
     * @cfg {Object} parentParamsAndItemIds
     * 参数和项目Id。
     */
    parentParamsAndItemIds: null,
    /**
     * @cfg {Array} eventType
     * 事件类型。
     */
    eventType: ['callparent'],
    /**
     * @method
     * 获取焦点并加载数据源。
     */
    getFocus: function () {
        var me = this;
        me.on('focus', function () {
            me.setValue(null);
            me.store.loadPage(1, {
                scope: this,
                callback: function (records, operation, success) {
                    if (records.length > 0) {
                        me.expand();
                    }
                }
            });
        });
    },
    /**
     * @method
     * 依据父类选择器数据加载数据源。
     */
    getCallParent: function () {
        var me = this,
            cmp;
        if (!Ext.isEmpty(me.parentParamsAndItemIds)) {
            Ext.Object.each(me.parentParamsAndItemIds, function (queryParam, itemId, parentParamsAndItemIds) {
                cmp = me.up().items.get(itemId);
                if (!cmp.hasListener('select')) {
                    cmp.on('select', function (combo) {
                        me.setValue(null);
                        me.store.loadPage(1, {
                            scope: this,
                            callback: function (records, operation, success) {
                                me.focus(false, 100);
                                me.expand();
                            }
                        });
                    });
                }
            });
        }
    },
    /**
     * @private
     * 组件初始化方法。
     */
    initComponent: function () {
        var me = this;
        me.on('boxready', function () {
            var callparent = null;
            for (var i = 0; i < me.eventType.length; i++) {
                if (me.eventType[i] == 'focus') {
                    me.getFocus();
                } else if (me.eventType[i] == 'callparent') {
                    callparent = 'callparent';
                    me.getCallParent();
                }
            }
            if (callparent == 'callparent') {
                me.un('focus');
            }
        });
        this.callParent(arguments);
        me.store.on('beforeLoad', function (st, operation, e) {
            var cmp = null,
                searchParams = operation.params;
            if (Ext.isEmpty(searchParams)) {
                searchParams = {};
            }
            searchParams[me.queryParam] = me.rawValue;
            if (!Ext.isEmpty(me.parentParamsAndItemIds)) {
                Ext.Object.each(me.parentParamsAndItemIds, function (queryParam, itemId, parentParamsAndItemIds) {
                    cmp = me.up().items.get(itemId);
                    searchParams[queryParam] = cmp.getValue();
                });
            }
            Ext.apply(operation, {
                params: searchParams
            });
        });
    }
});

Ext.define('Gamegold.selector.LinkedContainer', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.linkedcontainer',

    /**
     * @cfg {String} triggerCls
     * 定义级联选择器内的选择器类型，默认类型是linkedselector。
     */
    defaultType: 'linkedselector',

    /**
     * @method
     * 通过itemId得到级联选择器内的选择器的值。
     * @param {String} itemId
     * @return {Object} itemValue
     */
    getItemValue: function (itemId) {
        var me = this,
            item = me.items.get(itemId);
        if (item != null) {
            return item.getValue();
        }
        return null;
    },

    /**
     * @method
     * 通过级联选择器内的所有选择器的值，并以数组的方式返回。
     * @param {String} itemId
     * @return {Array} itemValues
     */
    getValue: function () {
        var me = this,
            values = new Array();
        for (var i = 0; i < me.items.length; i++) {
            values[i] = me.getItemValue(me.items.items[i].itemId);
        }
        return values;
    }
});

//数据字典
/**********************************************************************
 * 业务字典提供方法
 */
/**********************************************************************/
var dataDictionary = new Ext.util.HashMap();
dataDictionary.add('reasonType', [
    {value: 100, display: '交易完成'},
    {value: 101, display: '更新收货数量'},
    {value: 102, display: '卖家申请部分完单'},
    {value: 200, display: '需人工介入'},
    {value: 300, display: '其他情况'},
    {value: 301, display: '交易超时'},
    {value: 302, display: '背包已满'},
    {value: 303, display: '不是附魔师'},
    {value: 304, display: '账号密码错误'},
    {value: 305, display: '动态令牌'},
    {value: 306, display: '不在交易地点'},
    {value: 307, display: '出战状态'},
    {value: 350, display: '交易超时撤单'},
    {value: 351, display: '交易超时人工介入'},
    {value: 400, display: '收货商没来,等待过久'},
    {value: 401, display: '订单下错区服'},
    {value: 402, display: '自动收货机器故障(总是登陆异常)'},
    {value: 403, display: '收货角色名/等级不正确'},
    {value: 404, display: '收货角色库存上限'},
    {value: 405, display: '收货角色安全模式'},
    {value: 406, display: '收货角色频繁更改/上下号'},
    {value: 407, display: '价格变动不想出了'},
    {value: 408, display: '其他原因'},
    {value: 501, display: '出货商30分钟内未进行交易'},
    {value: 502, display: '排队超时'},

]);
dataDictionary.add('roleStatus', [
    {value: '1', display: '空闲'},
    {value: '2', display: '收货中'},
    {value: '3', display: '分仓中'},
    {value: '4', display: '已下架'}
]);
dataDictionary.add('isShRoleType', [
    {value: true, display: '是'},
    {value: false, display: '否'}
]);
dataDictionary.add('isPackFullType', [
    {value: true, display: '是'},
    {value: false, display: '否'}
]);
dataDictionary.add('check', [
    {value: true, display: '是'},
    {value: false, display: '否'}
]);
dataDictionary.add('isEnabled', [
    {value: true, display: '是'},
    {value: false, display: '否'}
]);
dataDictionary.add('isSplit', [
    {value: true, display: '是'},
    {value: false, display: '否'}
]);
dataDictionary.add('enabled', [
    {value: true, display: '是'},
    {value: false, display: '否'}
]);
dataDictionary.add('fieldType', [
    // {value: '单选框', display: '单选框'},
    // {value: '多选框', display: '多选框'},
    {value: '下拉选', display: '下拉选'},
    {value: '文本输入框', display: '文本输入框'},
    {value: '数字输入框', display: '数字输入框'},
    // {value: '文本数字输入框', display: '文本数字输入框'},
    // {value: '复选框', display: '复选框'}
]);
dataDictionary.add('goodsType', [
    {value: '游戏币', display: '游戏币'},
    {value: '挑战书', display: '挑战书'}
]);
dataDictionary.add('field', [
    {value: 'SCF01', display: 'SCF01'},
    {value: 'SCF02', display: 'SCF02'},
    {value: 'SCF03', display: 'SCF03'},
    {value: 'SCF04', display: 'SCF04'},
    {value: 'SCF05', display: 'SCF05'}
]);
dataDictionary.add('ableDelivery', [
    {value: true, display: '开启收货'},
    {value: false, display: '关闭收货'}
]);
dataDictionary.add('ableSell', [
    {value: true, display: '开启出售'},
    {value: false, display: '停止出售'}
]);
dataDictionary.add('enableRobot', [
    {value: true, display: '是'},
    {value: false, display: '否'}
]);
dataDictionary.add('isOnline', [
    {value: "-1", display: '全部'},
    {value: "true", display: '是'},
    {value: "false", display: '否'}
]);
dataDictionary.add('sex', [
    {value: '1', display: '男'},
    {value: '0', display: '女'}
]);
dataDictionary.add('userType', [
    {value: 1, display: '担保客服'},
    {value: 7, display: '入驻客服'},
    {value: 2, display: '管理员'},
    {value: 3, display: '系统管理员'},
    {value: 8, display: '寄售客服'}
]);
dataDictionary.add('userType2', [
    {value: -1, display: '系统'},
    {value: 1, display: '接单客服'},
    {value: 2, display: '管理员'},
    {value: 3, display: '系统管理员'},
    {value: 4, display: '5173注册用户'},
    {value: 5, display: '招商客服'},
    {value: 6, display: '做单客服'},
    {value: 7, display: '入驻客服'},
    {value: 8, display: '寄售客服'}
]);
dataDictionary.add('sellerState', [
    {value: 0, display: '待审核'},
    {value: 1, display: '审核通过'},
    {value: 2, display: '审核不通过'}
]);
dataDictionary.add('openShState', [
    {value: 0, display: '待审核'},
    {value: 1, display: '已开通'},
    {value: 2, display: '已关闭'}
]);
dataDictionary.add('tradeType', [
    {value: 1, display: '当面交易'},
    {value: 2, display: '游戏内邮寄'},
    {value: 3, display: '公会交易'}
]);
dataDictionary.add('goodsCat', [
    {value: 1, display: '1'},
    {value: 2, display: '2'},
    {value: 3, display: '3'}
]);
dataDictionary.add('orderState', [
    {value: 1, display: '待付款'},
    {value: 2, display: '已付款'},
    {value: 3, display: '待发货'},
    {value: 4, display: '已发货'},
    {value: 5, display: '结单'},
    {value: 6, display: '已退款'},
    {value: 7, display: '已取消'}
]);
dataDictionary.add('refererTypeEnum', [
    {value: 1, display: '待付便民中心款'},
    {value: 2, display: '网吧联盟'},
    {value: 3, display: '金币商城内部'},
    {value: 4, display: 'M站'},
    {value: 5, display: 'app订单'}
]);
dataDictionary.add('evaluateState', [
    {value: 0, display: '全部'},
    {value: 1, display: '非常不满意'},
    {value: 2, display: '不满意'},
    {value: 3, display: '一般'},
    {value: 4, display: '满意'},
    {value: 5, display: '非常满意'}
]);
dataDictionary.add('couponState', [
    {value: 0, display: '全部'},
    {value: 1, display: '红包'},
    {value: 2, display: '店铺券'}
]);
dataDictionary.add('couponType', [
    {value: 0, display: '请选择'},
    {value: 1, display: '红包'},
    {value: 2, display: '店铺券'}
]);
dataDictionary.add('couponPrice', [
    {value: 0, display: '请选择'},
    {value: 2, display: '2元'},
    {value: 5, display: '5元'},
    {value: 10, display: '10元'}
]);
dataDictionary.add('orderState2', [
    {value: 4, display: '已发货'},
    {value: 5, display: '已结单'}
]);
dataDictionary.add('refererType', [
    {value: 1, display: '便民中心'},
    {value: 2, display: '网吧联盟'}
]);
dataDictionary.add('refererTypeName', [
    {value: 3, display: '金币商城内部'},
    {value: 4, display: 'M站'},
    {value: 5, display: 'app订单'}
]);


/**
 * 订单类型
 */
dataDictionary.add('orderType', [
    {value: 1, display: '担保'},
    {value: 2, display: '寄售物服'},
    {value: 3, display: '寄售机器人'},
]);

/**
 * 订单来源
 */
dataDictionary.add('refererType', [
    {value: 1, display: '便民中心'},
    {value: 2, display: '网吧联盟'},
    {value: 3, display: '金币商城内部'},
    {value: 4, display: 'M站'},
    {value: 5, display: 'app订单'},
]);
dataDictionary.add('cancleOrderState', [
    {value: 6, display: '已退款'},
    {value: 7, display: '已取消'}
]);
//dataDictionary.add('refundReason', [
//   		{value: 1,display:'由于买家选错游戏区服阵营，当前交易被迫终止'},
//   		{value: 2,display:'由于买家的游戏角色名或数字ID错误，当前交易被迫终止'},
//   		{value: 3,display:'卖家无货少货'},
//   		{value: 4,display:'其他原因'}
//   	]);
dataDictionary.add('refundReason', [
    {value: 101, display: '由于买家选错游戏区服，当前交易被迫终止'},
    {value: 102, display: '由于买家选错游戏阵营，当前交易被迫终止'},
    {value: 103, display: '由于买家的游戏角色名或数字ID错误，当前交易被迫终止'},
    {value: 104, display: '由于买家的游戏角色名等级不够或未到达游戏运营商的最低交易等级，无法到指定地点 交易，当前交易被迫终止'},
    {value: 105, display: '由于买家通过QQ或者电话要求客服终止当前交易'},
    {value: 106, display: '由于买家未及时交易，且客服通过QQ或电话均无法与买家取得联系，当前交易被迫终止'},
    {value: 201, display: '卖家游戏角色无货或少货'},
    {value: 202, display: '卖家游戏角色数据异常，客服无法登录游戏'},
    {value: 203, display: '卖家游戏内当日发送邮件次数已达上限'},
    {value: 204, display: '卖家游戏角色等级今日已达发货上限，无法发货，交易取消'},
    {value: 301, display: '游戏运营商服务器维护（包括：服务器繁忙等）'},
    {value: 302, display: '游戏运营商原因，导致卖家游戏帐号无法登录游戏，交易取消'},
    {value: 401, display: '其他原因'}
]);
dataDictionary.add('module', [
    {value: "订单", display: "订单"},
    {value: "用户", display: '用户'},
    {value: "商品", display: '商品'},
    {value: "库存", display: '库存'},
    {value: "资金", display: '资金'}
]);
dataDictionary.add('subUserType', [
    {value: 5, display: "招商"},
    {value: 6, display: "做单"}
]);
dataDictionary.add('serviceType', [
    {value: 1, display: "担保"},
    {value: 2, display: "寄售"}
]);
dataDictionary.add('sellerType', [
    {value: 0, display: ""},
    {value: 1, display: "个人玩家"},
    {value: 2, display: "工作室"}
]);
dataDictionary.add('SgameName', [
    {value: "地下城与勇士", display: "地下城与勇士"},
    {value: "剑灵", display: "剑灵"},
    {value: "魔兽世界(国服)", display: "魔兽世界(国服)"},
    {value: "QQ三国", display: "QQ三国"},
    {value: "剑侠情缘Ⅲ", display: "剑侠情缘Ⅲ"},
    {value: "疾风之刃", display: "疾风之刃"},
    {value: "天涯明月刀", display: "天涯明月刀"},
    {value: "龙之谷", display: "龙之谷"},
    {value: "新寻仙", display: "新寻仙"},
    {value: "斗战神", display: "斗战神"},
    {value: "上古世纪", display: "上古世纪"},
    {value: "怪物猎人OL", display: "怪物猎人OL"},
    {value: "江湖", display: "江湖"}
]);
dataDictionary.add('orderLogOperations', [
    {value: "ORDER_CREATE", display: "下单"},
    {value: "ORDER_PAID", display: "付款"},
    {value: "ORDER_DISTRIBUTION", display: "配单"},
    {value: "ORDER_DELIVERY", display: "发货"},
    {value: "ORDER_STATEMENT", display: "结单"},
    {value: "ORDER_REFUND", display: "退款"},
    {value: "ORDER_CANCEL", display: "取消订单"},
    {value: "ORDER_RECEIVER", display: "已收货"},
    {value: "ORDER_OTHER", display: "其它"}
]);
dataDictionary.add('repositoryLogOperations', [
    {value: "REPOSITORY_ADD", display: "添加库存"},
    {value: "REPOSITORY_MODIFY", display: "修改库存"},
    {value: "REPOSITORY_REMOVE", display: "删除库存"}
]);
dataDictionary.add('goodsLogOperations', [
    {value: "GOODS_ADD", display: "添加商品"},
    {value: "GOODS_MODIFY", display: "修改商品"},
    {value: "GOODS_ENABLE", display: "启用商品"},
    {value: "GOODS_DISABLE", display: "禁用商品"},
    {value: "GOODS_REMOVE", display: "删除商品"},
    {value: "GOODS_OTHER", display: "其它"}
]);
dataDictionary.add('textMessageCycle', [
    {value: "30", display: "0.5"},
    {value: "60", display: "1"},
    {value: "120", display: "2"},
    {value: "180", display: "3"},
    {value: "240", display: "4"},
    {value: "300", display: "5"},
    {value: "600", display: "10"}
]);
dataDictionary.add('syncState', [
    {value: "1", display: "成功"},
    {value: "0", display: "失败"}
]);
dataDictionary.add('shOrderType', [
    {value: 1, display: '等待交易'},
    {value: 2, display: '待分配角色'},
    {value: 3, display: '交易中'},
    {value: 4, display: '交易完成'},
    {value: 5, display: '部分完单'},
    {value: 6, display: '撤单'},
    {value: 7, display: '需人工介入'},
    {value: 8, display: '申请部分完单中'},
    {value: 9, display: '待发货'},
    {value: 10, display: '已发货'},
    {value: 11, display: '待确认收货'}
]);
dataDictionary.add('refundOrderStatus', [
    {value: "1", display: "待审核"},
    {value: "2", display: "审核不通过"},
    {value: "3", display: "已退款"}
]);
dataDictionary.add('theDeliveryType', [
    {value: 0, display: "全部"},
    {value: 1, display: "机器收货"},
    {value: 2, display: "手工收货"},
    {value: 3, display: "暂停收货"}
]);
dataDictionary.add('shMode', [
    {value: 1, display: "机器收货"},
    {value: 2, display: "手工收货"}
]);
dataDictionary.add('theDeliveryTypeForShPurchaseGameOnly', [
    {value: "0", display: "全部"},
    {value: "1", display: "机器收货"},
    {value: "2", display: "手工收货"},
    {value: "3", display: "关闭收货"}
]);
dataDictionary.add('timeoutstatus', [
    {value: "-1", display: '全部'},
    {value: "true", display: '已超时'},
    {value: "false", display: '未超时'}
]);
dataDictionary.add('timeOutSub', [
    {value: true, display: '已超时'},
    {value: false, display: '未超时'}
]);
dataDictionary.add('machineArtificialType', [
    {value: 1, display: '已转人工'},
    {value: 2, display: '转人工失败'}
]);
dataDictionary.add('fundDetailType', [
    {value: 0, display: "全部"},
    {value: 1, display: "充值"},
    {value: 2, display: "付款"},
    {value: 3, display: "退款"},
    {value: 4, display: "冻结资金"},
    {value: 5, display: "解冻资金"},
    {value: 6, display: "转账"},
    {value: 7, display: "7Bao手动充值单"},
    {value: 8, display: "7Bao自动充值单"},
    {value: 9, display: "金币资金转7bao"}
]);
dataDictionary.add('fundType', [
    {value: "1", display: "1"},
    {value: "2", display: "2"},
    {value: "3", display: "3"},
    {value: "4", display: "4"},
    {value: "5", display: "5"},
    {value: "6", display: "6"},
    {value: "7", display: "7"},
    {value: "8", display: "8"},
    {value: "9", display: "9"},
    {value: "10", display: "10"},
    {value: "11", display: "11"},
    {value: "12", display: "12"},
    {value: "13", display: "13"},
    {value: "14", display: "14"},
    {value: "15", display: "15"},
    {value: "16", display: "16"},
    {value: "17", display: "17"},
    {value: "18", display: "18"},
    {value: "19", display: "19"},
    {value: "20", display: "20"},
    {value: "21", display: "21"},
    {value: "22", display: "22"},
    {value: "23", display: "23"},
    {value: "24", display: "24"}

]);

dataDictionary.add('splitRepositoryRequestStatus', [
    {value: -2, display: "全部"},
    {value: -1, display: "未推送"},
    {value: 0, display: "待处理"},
    {value: 1, display: "分仓中"},
    {value: 2, display: "分仓成功"},
    {value: 3, display: "分仓失败"},
    {value: 4, display: "部分分仓"}
]);
dataDictionary.add('SplitRepositoryLogType', [
    {value: 1, display: "分仓所得"},
    {value: 2, display: "收货所得"},
    {value: 3, display: "分仓支出"},
    {value: 4, display: "销售支出"}

]);
dataDictionary.add('IncomeType', [
    {value: 1, display: "收入"},
    {value: 2, display: "支出"}

]);
dataDictionary.add('statusOfPayOrder', [
    {value: -1, display: "全部"},
    {value: 0, display: "待支付"},
    {value: 1, display: "已支付"},
    {value: 2, display: "申请退款中"},
    {value: 3, display: "已退款"},
    {value: 4, display: "自动充值单"}
]);
dataDictionary.add('transferStatus', [
    {value: 0, display: "待初始化"},
    {value: 1, display: "待转账"},
    {value: 2, display: "已转账"}
]);
/**********************************************************************/
//业务字典信息类
Ext.define('DataDictionary', {
    singleton: true,


    /**
     * 获取所有交易类型
     * @returns 返回所有交易类型
     */
    loadDictDataByTradeType: function () {
        Ext.Ajax.request({
            url:'./delivery/queryTradeList.action',
            method: 'POST',
            async: false,
            data: JSON,
            success: function (response) {
                var json = Ext.decode(response.responseText),
                    tradeList = json.tradeList,
                    dictionaryList = [];
                for(var i=0;i<tradeList.length;i++) {
                    var dictionaryEO = {value: tradeList[i].id,
                        display: tradeList[i].name};
                    dictionaryList[i] = dictionaryEO;
                }
                console.log("dictionaryList")
                console.log(dictionaryList)
                dataDictionary.add("tradeTypes", dictionaryList);
            }
        });
    },
    /**
     * 通过词条代码获得业务字典数据
     * @param termsCode 词条代码
     * @param valueCodes 条目编码数组
     * @returns 业务字典数据
     */
    getDataByTermsCode: function (termsCode, valueCodes) {
        if (dataDictionary != null && termsCode != null) {
            var data = Ext.clone(dataDictionary.get(termsCode));
            if (!Ext.isEmpty(valueCodes)) {
                var reslutArray = new Array();
                if (Ext.isArray(valueCodes)) {
                    for (var i = 0; i < data.length; i++) {
                        if (Ext.Array.contains(valueCodes, data[i].value)) {
                            reslutArray.push(data[i]);
                        }
                    }
                } else {
                    for (var i = 0; i < data.length; i++) {
                        if (valueCodes == data[i].value) {
                            reslutArray.push(data[i]);
                        }
                    }
                }
                //此处当valueCodes为数组，但是内容无法识别(undefined)时，返回全部数据
                if (reslutArray != null && reslutArray.length > 0) {
                    return reslutArray;
                } else {
                    return data;
                }
                //return reslutArray;
            }
            return data;
        }
        return null;
    },
    /**
     * 通过多个词条代码获得业务字典数据数组
     * @param termsCodes 词条代码数组
     * @returns 业务字典数据数组
     */
    getDataByTermsCodes: function (termsCodes) {
        if (termsCodes == null) {
            return null;
        }
        var data = new Array();
        for (var i = 0; i < termsCodes.length; i++) {
            data.push(DataDictionary.getDataByTermsCode(termsCodes[i]));
        }
        return data;
    },
    /**
     * 根据数据字典名称获取对应的store,如果没有则返回[],不影响整个页面的渲染
     * @param termsCode 词条代码
     * @param id 如果想要通过store id 查询store的话就传id,否则可以不用传
     * @param anyRecords 如果想增加一些记录到这个数据字典中，可以通过这个参数传入，
     *                     些参数可以是一个数据数组，也可以是一个数据
     * @param valueCodes 条目编码数组
     * @returns
     */
    getDataDictionaryStore: function (termsCode, id, anyRecords, valueCodes) {
        var data = DataDictionary.getDataByTermsCode(termsCode, valueCodes);
        if (!Ext.isEmpty(data)) {
            if (!Ext.isEmpty(anyRecords)) {
                if (Ext.isArray(anyRecords)) {
                    for (var i = 0; i < anyRecords.length; i++) {
                        data.unshift(anyRecords[i]);
                    }
                } else {
                    data.unshift(anyRecords);
                }
            }
            var json = {
                fields: ['value', 'display'],
                data: data
            };
            if (!Ext.isEmpty(id)) {
                json["id"] = id;
            }
            return Ext.create('Ext.data.Store', json);
        } else {
            return [];
        }
    },
    /**
     * 根据数据字典名称获取对应的combo组件
     * @param termsCode 词条代码
     * @param config combo的一些配置信息
     * @param anyRecords 如果想增加一些记录到这个数据字典中，可以通过这个参数传入，
     *                     些参数可以是一个数据数组，也可以是一个数据
     * @param id 如果想要通过store id 查询store的话就传id,否则可以不用传
     * @returns
     */
    getDataDictionaryCombo: function (termsCode, config, anyRecords, id, valueCodes) {
        if (Ext.isEmpty(config)) {
            config = {};
        }
        var store = DataDictionary.getDataDictionaryStore(termsCode, id, anyRecords, valueCodes),
            cfg = Ext.apply({
                store: store,
                displayField: 'display',
                valueField: 'value'
            }, config);
        return Ext.create('Ext.form.ComboBox', cfg);

    },
    getDataDictionaryCheckboxGroup: function (termsCode, config, anyRecords,
                                              id, valueCodes) {
        if (Ext.isEmpty(config)) {
            config = {};
        }

        var data = DataDictionary.getDataByTermsCode(termsCode,
            valueCodes);
        if (!Ext.isEmpty(data)) {
            if (!Ext.isEmpty(anyRecords)) {
                if (Ext.isArray(anyRecords)) {
                    for (var i = 0; i < anyRecords.length; i++) {
                        data.unshift(anyRecords[i]);
                    }
                } else {
                    data.unshift(anyRecords);
                }
            }
        }
        var myCheckboxItems = [];
        for (var i = 0; i < data.length; i++) {
            var boxLabel = data[i].display;
            var name = data[i].value;
            myCheckboxItems.push({
                boxLabel : boxLabel,
                name : name
            });
        }

        var cfg = Ext.apply({
            items:myCheckboxItems
        }, config);
        var checkboxGroup = Ext.create('Ext.form.CheckboxGroup', cfg);
        return checkboxGroup;

    },
    /**
     *将业务字典的displayValue（数据字典显示值）转换成描述submitValue（提交值）
     * 使用方法rendererDictionary(displayValue,’abc’);
     * @param value  所要转换的值
     * @param termsCode 词条代码
     */
    rendererDisplayToSubmit: function (displayValue, termsCode) {
        var data = DataDictionary.getDataByTermsCode(termsCode);
        if (!Ext.isEmpty(displayValue) && !Ext.isEmpty(data)) {
            for (var i = 0; i < data.length; i++) {
                if (displayValue == data[i].display) {
                    return data[i].value;
                }
            }
        }
        return displayValue;
    },
    /**
     *将业务字典的submitValue（提交值）转换成描述displayValue（数据字典显示值）
     * 使用方法rendererDictionary(value,’abc’);
     * @param value  所要转换的值
     * @param termsCode 词条代码
     */
    rendererSubmitToDisplay: function (submitValue, termsCode) {
        var data = DataDictionary.getDataByTermsCode(termsCode);
        if (!Ext.isEmpty(submitValue) && !Ext.isEmpty(data)) {
            for (var i = 0; i < data.length; i++) {
                if (submitValue == data[i].value) {
                    return data[i].display;
                }
            }
        }
        return submitValue;
    }
});
/**********************************************************************/

/**
 * 游戏信息数据级联组件
 */
Ext.define('Gamegold.selector.GameSelectorModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'name', type: 'string'}
    ]
});

Ext.define('Gamegold.selector.GameCompanyStore', {
    extend: 'Ext.data.Store',
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'Gamegold.selector.GameSelectorModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './common/queryGameCompanyList.action',
                reader: {
                    type: 'json',
                    root: 'gameCompanyNameList'
                }
            }
        }, cfg)]);
    }
});

Ext.define('Gamegold.selector.GameStore', {
    extend: 'Ext.data.Store',
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'Gamegold.selector.GameSelectorModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './common/queryGameInfoList.action',
                reader: {
                    type: 'json',
                    root: 'gameInfoList'
                }
            }
        }, cfg)]);
    }
});

Ext.define('Gamegold.selector.GameAreaSelectorModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'spell', type: 'string'},
        {name: 'gameServerList'}
    ]
});

Ext.define('Gamegold.selector.GameAreaStore', {
    extend: 'Ext.data.Store',
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'Gamegold.selector.GameAreaSelectorModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './common/queryGameAreaList.action',
                reader: {
                    type: 'json',
                    root: 'gameAreaList'
                }
            }
        }, cfg)]);
    }
});

Ext.define('Gamegold.selector.ServerStore', {
    extend: 'Ext.data.Store',
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'Gamegold.selector.GameSelectorModel',
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});

Ext.define('Gamegold.selector.GameRaceStore', {
    extend: 'Ext.data.Store',
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'Gamegold.selector.GameSelectorModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './common/queryGameRaceList.action',
                reader: {
                    type: 'json',
                    root: 'gameRaceList'
                }
            }
        }, cfg)]);
    }
});

Ext.define('Gamegold.selector.DeliveryTypeStore', {
    extend: 'Ext.data.Store',
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'Gamegold.selector.DeliveryTypeSelectorModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './deliveryType/queryDeliveryTypeList.action',
                reader: {
                    type: 'json',
                    root: 'deliveryTypeList'
                }
            }
        }, cfg)]);
    }
});

Ext.define('Gamegold.selector.tradeTypeStore', {
    extend: 'Ext.data.Store',
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'Gamegold.selector.DeliveryTypeSelectorModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/selectTradeByGameGoodsTypeAndShMode.action',
                reader: {
                    type: 'json',
                    root: 'tradeList'
                }
            }
        }, cfg)]);
    }
});

/**
 * 游戏信息数据级联组件
 */
Ext.define('Gamegold.selector.DeliveryTypeSelectorModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'name', type: 'string'}
    ]
});

/** 级联下拉容器 */
Ext.define('Gamegold.selector.GameLinkedContainer', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.gamelinkselector',
    //width: 300,
    layout: 'column',
    company: null,
    getCompany: getCompany,
    game: null,
    getGame: getGame,
    area: null,
    getArea: getArea,
    server: null,
    getServer: getServer,
    race: null,
    getRace: getRace,
    initComponent: function () {
        var me = this;
        me.items = [me.getCompany(), me.getGame(), me.getArea(), me.getServer(), me.getRace()];
        me.callParent();
    }
});

/** 级联下拉容器 */
Ext.define('Gamegold.selector.GameLinkedContainerSellerSetting', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.gamelinkselectorsellersetting',
    //width: 300,
    layout: 'column',
    company: null,
    getCompany: getCompany,
    game: null,
    getGame: getGame,
    area: null,
    getArea: getArea,
    initComponent: function () {
        var me = this;
        me.items = [me.getCompany(), me.getGame(), me.getArea()];
        me.callParent();
    }
});

/** 级联下拉容器 */
Ext.define('Gamegold.selector.GameContainerSellerSetting', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.gameselectorsellersetting',
    //width: 300,
    layout: 'column',
    company: null,
    getCompany: getCompany,
    game: null,
    getGame: getGame,
    initComponent: function () {
        var me = this;
        me.items = [me.getCompany(), me.getGame()];
        me.callParent();
    }
});

/** 收货模式 */
Ext.define('Gamegold.selector..deliveryConfigContainer', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.deliveryConfigSelector',
    //width: 300,
    layout: 'column',
    company: null,
    getCompany: getCompany,
    deliveryGame: null,
    getDeliveryGame: getDeliveryGame,
    deliveryType: null,
    getDeliveryType: getDeliveryType,
    goodsType: null,
    getGoodsType: getGoodsType,
    tradeType: null,
    getTradeType: getTradeType,
    initComponent: function () {
        var me = this;
        me.items = [me.getCompany(), me.getDeliveryGame(), me.getDeliveryType(), me.getGoodsType(), me.getTradeType()];
        me.callParent();
    }
});

function getDeliveryType() {
    var me = this;
    if (Ext.isEmpty(me.deliveryType)) {
        me.deliveryType = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_DeliveryType_ID',
            parentParamsAndItemIds: {
                'deliveryType': 'Gamegold_selector_Game_ID'
            },
            store: Ext.create('Gamegold.selector.DeliveryTypeStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'name', // 显示名称
            valueField: 'id', // 值
            name: 'deliveryTypeId', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            allowBlank: me.allowBlank,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                },
                'change': function (ths, the, eOpts) {
                    if (me.gameChanged) {
                        me.gameChanged(ths, the, eOpts);
                    }
                }
            }
        });
    }
    return me.deliveryType;
}

function getTradeType() {
    var me = this;
    if (Ext.isEmpty(me.tradeType)) {
        me.tradeType = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_TradeType_ID',
            parentParamsAndItemIds: {
                'shMode': 'Gamegold_selector_DeliveryType_ID',
                'gameName': 'Gamegold_selector_Game_ID',
                'goodsTypeId': 'Gamegold_selector_GoodyType_ID'
            },
            store: Ext.create('Gamegold.selector.tradeTypeStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'name', // 显示名称
            valueField: 'id', // 值
            name: 'tradeTypeId', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            allowBlank: me.allowBlank,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                },
                'change': function (ths, the, eOpts) {
                    if (me.gameChanged) {
                        me.gameChanged(ths, the, eOpts);
                    }
                }
            }
        });
    }
    return me.tradeType;
}

Ext.define('MyApp.model.ShGameConfigCommonModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',//
        type: 'int'
    }, {
        name: 'gameName'//游戏名称
    }, {
        name: 'unitName'//单位
    }, {
        name: 'isEnabled',// 是否启用
        type: 'boolean'
    }, {
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }, {
        name: 'updateTime',//最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }, {
        name: 'tradeType'//当前游戏支持的交易方式
    }, {
        name: 'tradeTypeId'//当前游戏支持的交易方式ID
    }, {
        name: 'goodsTypeId',//游戏类目ID
        type: 'int'
    }, {
        name: 'goodsTypeName'//游戏类目
    }, {
        name: 'minBuyAmount',//最低购买金额 ADD 20170606
        type: 'float'
    }, {
        name: 'enableRobot',//是否支持机器收货
        type: 'boolean'
    }, {
        name: 'enableMall',//商城是否启用 add by lcs
        type: 'boolean'
    }, {
        name: 'isSplit',//商城采购商是否启用分仓开关
        type: 'boolean'
    }]
});

Ext.define('MyApp.store.GameNameAndShModeStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShGameConfigCommonModel'
    ],
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShGameConfigCommonModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/selectGoodsTypeByGameAndShMode.action',
                reader: {
                    type: 'json',
                    root: 'gameConfigList',
                    totalProperty: 'totalCount'
                }
            }
        }, cfg)]);
    }
});

function getGoodsType() {
    var me = this;
    if (Ext.isEmpty(me.goodsType)) {
        me.goodsType = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_GoodyType_ID',
            parentParamsAndItemIds: {
                'gameName': 'Gamegold_selector_Game_ID',
                'shMode':'Gamegold_selector_DeliveryType_ID'
            },
            store: Ext.create('MyApp.store.GameNameAndShModeStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'goodsTypeName', // 显示名称
            valueField: 'goodsTypeId', // 值
            name: 'goodsTypeId', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            allowBlank: me.allowBlank,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                },
                'change': function (ths, the, eOpts) {
                    if (me.gameChanged) {
                        me.gameChanged(ths, the, eOpts);
                    }
                }
            }
        });
    }
    return me.goodsType;
}

function getDeliveryGame() {
    var me = this;
    if (Ext.isEmpty(me.game)) {
        me.game = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_Game_ID',
            parentParamsAndItemIds: {
                'gameCompanyName': 'Gamegold_selector_Company_ID'
            },
            store: Ext.create('Gamegold.selector.GameStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'name', // 显示名称
            valueField: 'name', // 值
            name: 'gameName', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            allowBlank: me.allowBlank,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                },
                /**ZW_C_JB_00008_20170512 START ADD **/
                'change': function (ths, the, eOpts) {
                    if (me.gameChanged) {
                        me.gameChanged(ths, the, eOpts);
                    }
                }
                /**ZW_C_JB_00008_20170512 START ADD **/
            }
        });
    }
    return me.game;
}

function getCompany() {
    var me = this;
    if (Ext.isEmpty(me.company)) {
        me.company = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_Company_ID',
            store: Ext.create('Gamegold.selector.GameCompanyStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'name',// 显示名称
            valueField: 'name', // 值
            name: 'company', // 表单name
            hideTrigger: true,
            columnWidth: .333,
            readOnly: me.readOnly,
            editable: false,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                }
            }
        });
    }
    return me.company;
}

function getGame() {
    var me = this;
    if (Ext.isEmpty(me.game)) {
        me.game = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_Game_ID',
            parentParamsAndItemIds: {
                'gameCompanyName': 'Gamegold_selector_Company_ID'
            },
            store: Ext.create('Gamegold.selector.GameStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'name', // 显示名称
            valueField: 'id', // 值
            name: 'gameName', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            allowBlank: me.allowBlank,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                },
                /**ZW_C_JB_00008_20170512 START ADD **/
                'change': function (ths, the, eOpts) {
                    if (me.gameChanged) {
                        me.gameChanged(ths, the, eOpts);
                    }
                }
                /**ZW_C_JB_00008_20170512 START ADD **/
            }
        });
    }
    return me.game;
}

function getArea() {
    var me = this;
    if (Ext.isEmpty(me.area)) {
        me.area = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_Area_ID',
            parentParamsAndItemIds: {
                'gameId': 'Gamegold_selector_Game_ID'
            },
            store: Ext.create('Gamegold.selector.GameAreaStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'name',// 显示名称
            valueField: 'name', // 值
            name: 'region', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            allowBlank: me.allowBlank,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                }
            }
        });
    }
    return me.area;
}

function getServer() {
    var me = this;
    if (Ext.isEmpty(me.server)) {
        me.server = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_Server_ID',
            parentParamsAndItemIds: {
                'areaId': 'Gamegold_selector_Area_ID'
            },
            store: Ext.create('Gamegold.selector.ServerStore', {
                listeners: {
                    'load': function () {
                        var server = me.getServer(), area = me.getArea(),
                            valueModel = Ext.isEmpty(area.valueModels) ? null : area.valueModels[0];
                        if (!Ext.isEmpty(valueModel)) {
                            server.getStore().loadData(valueModel.get('gameServerList'));
                        }
                    }
                }
            }),
            queryParam: 'name', // 查询参数名称
            displayField: 'name',// 显示名称
            valueField: 'name', // 值
            name: 'server', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            allowBlank: me.allowBlank,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                }
            }
        });
    }
    return me.server;
}

function getRace() {
    var me = this;
    if (Ext.isEmpty(me.race)) {
        me.race = Ext.widget('linkedselector', {
            itemId: 'Gamegold_selector_Race_ID',
            parentParamsAndItemIds: {
                'gameId': 'Gamegold_selector_Game_ID',
                'serverId': 'Gamegold_selector_Server_ID'
            },
            store: Ext.create('Gamegold.selector.GameRaceStore'),
            queryParam: 'name', // 查询参数名称
            displayField: 'name',// 显示名称
            valueField: 'name', // 值
            name: 'gameRace', // 表单name
            columnWidth: .333,
            hideTrigger: true,
            editable: false,
            readOnly: me.readOnly,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        ths.setRawValue(null);
                    }
                }
            }
        });
    }
    return me.race;
}

//下拉单选框
Ext.define('Gamegold.commonSelector.CommonCombSelector', {
    extend: 'Gamegold.selector.SingleSelector',
    minChars: 0,
    isPaging: true,// 分页
    isEnterQuery: true,// 回车查询
    listWidth: 330,// 设置下拉框宽度
    record: null,
    displayField: null,
    valueField: null,
    displayField: null,// 显示名称
    valueField: null,// 值
    queryParam: null,// 查询参数
    setCombValue: function (displayText, valueText) {
        var me = this, key = me.displayField + '', value = me.valueField
            + '';
        var m = Ext.create(me.store.model.modelName);
        m.set(key, displayText);
        m.set(value, valueText);
        me.store.loadRecords([m]);
        me.setValue(valueText);
    },
    getBlur: function (ths, the, eOpts) {
        if (Ext.isEmpty(ths.value)) {
            ths.setRawValue(null);
        }
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.active = config.active;
        me.store.addListener('select', function (comb, records, obs) {
            me.record = records[0];
        });
        me.callParent([cfg]);
        me.on('blur', me.getBlur, me);
    },
    getRecord: function () {
        var me = this;
        return me.record;
    }
});

/*
 * 卖家信息选择器
 */
Ext.define('Gamegold.commonSelector.CommonSellerSelector', {
    extend: 'Gamegold.commonSelector.CommonCombSelector',
    alias: 'widget.commonsellerselector',
    listWidth: 400,
    displayField: 'name',// 显示名称
    valueField: 'loginAccount',// 值
    queryParam: 'seller.name',// 查询参数
    showContent: '{name}&nbsp;&nbsp;&nbsp;{loginAccount}',// 显示表格列
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.store = Ext.create('MyApp.store.SellerStore', {
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    Ext.apply(operation, {
                        params: {
                            'seller.checkState': 1
                        }
                    });
                }
            },
            pageSize: 10
        });
        me.callParent([cfg]);
    }
});

/*
 * 游戏名称信息选择器
 */
Ext.define('Gamegold.commonSelector.CommonGameSelector', {
    extend: 'Gamegold.commonSelector.CommonCombSelector',
    alias: 'widget.commongameselector',
    listWidth: 400,
    isPaging: false,
    displayField: 'name',// 显示名称
    valueField: 'name',// 值
    queryParam: 'name',// 查询参数
    showContent: '{name}',// 显示表格列
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.store = Ext.create('Gamegold.selector.GameStore');
        me.callParent([cfg]);
    }
});

/*
 * 用户信息选择器
 */
Ext.define('Gamegold.commonSelector.CommonUserSelector', {
    extend: 'Gamegold.commonSelector.CommonCombSelector',
    alias: 'widget.commonuserselector',
    listWidth: 400,
    displayField: 'loginAccount',// 显示名称
    valueField: 'id',// 值
    queryParam: 'user.loginAccount',// 查询参数
    showContent: '{loginAccount}&nbsp;&nbsp;&nbsp;{realName}',// 显示表格列
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.store = Ext.create('MyApp.store.UserStore');
        me.callParent([cfg]);
    }
});