package com.fengkuang.author.enums;

public enum ResultCode {
    CODE_0000("0000", "成功"),
    CODE_0001("0001", "参数不完整！！！"),
    CODE_0002("0002", "文章标题不能为空！！！"),
    CODE_0003("0003", "文章标题字数超限！！！"),
    CODE_0004("0004", "文章内容不能为空！！！"),
    CODE_0005("0005", "文章频道不能为空！！！"),
    CODE_0006("0006", "文章收费类型错误！！！"),
    CODE_0007("0007", "用户不是入驻作者！！！"),
    CODE_0008("0008", "作者状态异常，请联系客服！！！"),
    CODE_0009("0009", "您的文章已有人订阅，请联系客服编辑/删除！！！"),
    CODE_0010("0010", "用户不存在或密码错误！！！"),
    CODE_0011("0011", "验证码错误或已失效！！！"),
    CODE_0012("0012", "已经发起过作者申请！！！"),
    CODE_0013("0013", "已经通过作者审核！！！"),
    CODE_0014("0014", "作者名称已存在，再换一个吧！！！"),
    // 不跳转
    CODE_9998("9998", "保存文章异常，请款系客服！！！"),
    CODE_9999("9999", "系统异常，请联系客服！！！");
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
