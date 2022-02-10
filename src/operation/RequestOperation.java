package operation;

public enum RequestOperation {

    LOGIN_OPERATION("登录"),
    SELF_MOD_OPERATION("自我修改"),
    LIST_ALL_USER_OPERATION("用户列表"),
    LIST_ALL_DOC_OPERATION("文件列表"),
    USER_DELETE_OPERATION("删除用户"),
    USER_ADD_OPERATION("添加用户"),
    USER_UPDATE_OPERATION("修改用户"),
    UPLOAD_OPERATION("上传文件"),
    DELETE_DOC_OPERATION("删除文件"),
    DOWNLOAD_OPERATION("下载文件"),


    UNKNOWN_MESSAGE_OPERATION("未知的格式"),
    WRONG_REQUEST_OPERATION("请求格式错误");

    private final String operation;
    private RequestOperation(String operation){
        this.operation = operation;
    }

    public String getOperation(){
        return this.operation;
    }
}
