/* 上传控件 */
function Uploader(options) {
    var defoptions = {
        cssType: 1
    }
    this.options = $.extend({}, defoptions, options);

}
Uploader.prototype = {
    init: function () {
        this.variable()
    },
    variable: function () {
        var str = "";
        var self = this;
        var $ = this.options.$ || jQuery;
        self.checkSuppose($);
        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: self.options.auto || false,

            // swf文件路径
            swf: self.options.swf || 'uploader.swf',
            formData:{
              orderId:deliveryOrder.orderId
            },
            method:"POST",
            // 文件接收服务端。
            server: baseServiceUrl+'services/manualShOrder/uploadImage',

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: {id:'#imgPicker',multiple: false},

            // 只允许选择图片文件。
            accept: self.options.accept || {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/gif,image/jpg,image/bmp,image/png,image/jpeg'
            },
            disableGlobalDnd: true,
            paste: document.body,
            chunked: true,
            compress: false,
            //图片限制
            fileNumLimit: self.options.fileNumLimit || 20,
            fileSizeLimit: self.options.fileSizeLimit || 40 * 1024 * 1024,    // 50 M
            fileSingleSizeLimit: self.options.fileSingleSizeLimit || 2 * 1024 * 1024    // 3 M
        });
        $("#imgUpload").click(function(){
          uploader.upload()
        })

        // 当有文件添加进来的时候
        uploader.on('fileQueued', function (file) {
            console.log("-------------------------",file);
            str += file.name + ";"
            $(".router-box").html(str)
            if (self.options.fileQueued) {
                self.options.fileQueued.call(null, file, response);
                return
            }
            var $list = $("#fileList"),
                $li = $(
                    '<div id="' + file.id + '" class="file-item thumbnail photo-show-box" style="display:inline-block">' +
                    '<img>' +
                    '<i class="del-photo-btn"></i>' +
                    '</div>'
                ),
                $img = $li.find('img'),
                $deletebtn = $li.find('.del-photo-btn');
            // $list为容器jQuery实例
            $list.append($li);
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            if (file.getStatus() === 'invalid') {
                self.showError(file.statusText, uploader);
            } else {
                uploader.makeThumb(file, function (error, src) {
                    //console.log(file)
                    if (error) {
                        $img.replaceWith('<span style="display:block;text-align:center">' + file.name + '</span>');
                        return;
                    }

                    $img.attr('src', src);
                }, 66, 66);
            }

            $deletebtn.on('click', function () {
                uploader.removeFile(file)
            })

        });
        //图片删除
        uploader.on('fileDequeued', function (file) {
            var $li = $('#' + file.id);
            $li.off().find('.del-photo-btn').off().end().remove();
        });
        // 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id),
                $percent = $li.find('.progress_up span');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<p class="progress_up"><span></span></p>')
                    .appendTo($li)
                    .find('span');
            }
            $percent.css('width', percentage * 100 + '%');
        });

        // 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on('uploadSuccess', function (file) {
            $('#' + file.id).addClass('upload-state-done');
            //			    $( '#'+file.id ).append('<span class="up_success">'+
            //									    	'<img src="/src/images/success.png">'+
            //									    '</span>')
        });

        // 文件上传失败，显示上传出错。
        uploader.on('uploadError', function (file) {
            var $li = $('#' + file.id),
                $error = $li.find('div.error');

            // 避免重复创建
            if (!$error.length) {
                $error = $('<div class="up_error"></div>').appendTo($li);
            }

            $error.text('上传失败');
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress_up').remove();
        });


        uploader.on('uploadAccept', function (file, response) {
            if (self.options.uploadAccept) {
                self.options.uploadAccept.call(null, file, response)
            }
        });
        uploader.onError = function (code) {
            self.showError(code, uploader)
        };
    },
    checkSuppose: function ($) {
        if (!WebUploader.Uploader.support()) {
            alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
            throw new Error('WebUploader does not support the browser you are using.');
        }
    },
    instance: function ($) {

    },
    showError: function (code, uploader) {
        var uploader = uploader.options,
            text;
        switch (code) {
            case 'F_EXCEED_SIZE':
                var size_max = uploader.fileSingleSizeLimit / 1024 / 1024 + 'M';
                text = '单张图片不能超过' + size_max;
                break;

            case 'Q_EXCEED_NUM_LIMIT':
                text = '超出上传数量，最多上传' + uploader.fileNumLimit + '张';
                break;
            case 'F_DUPLICATE':
                text = '不能上传重复文件';
                break;
            case 'Q_TYPE_DENIED':
                text = '请上传图片文件或者聊天记录文件';
                break;
            case 'exceed_size':
                text = '文件大小超出';
                break;
            case 'interrupt':
                text = '上传暂停';
                break;
            default:
                text = '';
                break;
        }
        if (text) {
            alert(text)
        }
    },

}
