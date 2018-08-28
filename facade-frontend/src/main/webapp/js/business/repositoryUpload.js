$(function () {
    $('#excelUpload').fileUploader({
        autoUpload: true,
        selectFileLabel: '请选择文件...',
        allowedExtension: 'xls|xlsx',

        onFileChange: function(e, form) {
            // 判断文件大小等
            var size = e.size; // 单位：字节
            if(size >= 20*1024*1024){
                alert("上传文件不能超过20M");
                return -1;
            }
        },
        // 设置请求头
        setRequestHeader: function(request) {
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
        // 每次上传后
        afterEachUpload: function(data, status, formContainer) {

        }
    });
});