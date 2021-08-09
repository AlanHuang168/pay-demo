package com.puer.pay.vo;

/**
 * @author zhudehong
 */

import com.alibaba.fastjson.JSON;
import java.io.Serializable;

public class ResponseCode<T> implements Serializable {
    public static final ResponseCode<?> SUCCESS = new ResponseCode<>(0, "成功");
    public static final ResponseCode<?> SYSTEM_ERROR = new ResponseCode<>(100001, "系统异常");
    public static final ResponseCode<?> SYSTEM_BUSY = new ResponseCode<>(-1, "系统繁忙");
    public static final ResponseCode<?> INVALID_PARAM = new ResponseCode<>(100002, "参数不合法");
    public static final ResponseCode<?> TIME_OUT = new ResponseCode<>(100003, "系统内部访问超时");
    public static final ResponseCode<?> INVALID_JSON_STRING = new ResponseCode<>(100004, "不合法的json串");
    public static final ResponseCode<?> RESOURCE_EXISTS = new ResponseCode<>(100005, "资源已经存在");
    public static final ResponseCode<?> RESOURCE_NOT_EXISTS = new ResponseCode<>(100006, "资源不存在");
    public static final ResponseCode<?> NON_SUPPORTED_OPER = new ResponseCode<>(100007, "不支持的操作");
    public static final ResponseCode<?> NO_PERMISSION = new ResponseCode<>(100008, "没有权限操作");
    public static final ResponseCode<?> SESSION_EXPIRED = new ResponseCode<>(100009, "会话已过期，请重新登陆");
    public static final ResponseCode<?> REQUEST_METHOD_NOT_SUPPORTED = new ResponseCode<>(100010, "不支持的请求方式");
    public static final ResponseCode<?> MEDIA_TYPE_NOT_SUPPORTED = new ResponseCode<>(100011, "不支持的数据类型");
    public static final ResponseCode<?> REQUEST_FREQUENCY_TOO_FAST = new ResponseCode<>(100012, "访问频繁");
    public static final ResponseCode<?> LACK_NECESSARY_REQUEST_HEADER = new ResponseCode<>(100013, "缺失必要的请求头");
    public static final ResponseCode<?> NOT_YET_LOGIN = new ResponseCode<>(1000014, "登录已失效，请重新登录");
    public static final ResponseCode<?> REQUEST_URL_NOT_FOUND = new ResponseCode<>(1000015, "请求接口不存在");
    public static final ResponseCode<?> REQUEST_DATA_NOT_VALID = new ResponseCode<>(1000016, "请求数据不合法");
    public static final ResponseCode<?> REQUEST_SOURCE_NOT_ALLOWED = new ResponseCode<>(1000017, "请求来源不合法");
    public static final ResponseCode<?> DATA_EXPIRED_NEED_REFRESH = new ResponseCode<>(100018, "数据状态已过期，需要重新刷新");
    public static final ResponseCode<?> REQUEST_EXPIRED = new ResponseCode<>(100019, "请求已过期");
    public static final ResponseCode<?> SIGN_INVALID = new ResponseCode<>(100020, "签名不合法");
    public static final ResponseCode<?> TOO_MANY_USER = new ResponseCode<>(100021, "当前此业务用户过多，稍后重试");
    public static final ResponseCode<?> SEND_MESSAGE_IS_FAIL = new ResponseCode<>(100022, "短信发送失败");
    public static final ResponseCode<?> MESSAGE_CODE_ERROR = new ResponseCode<>(100023, "验证码错误");
    public static final ResponseCode<?> MESSAGE_CODE_INVALID = new ResponseCode<>(100024, "验证码失效");
    public static final ResponseCode<?> AUTH_CODE_EXIST = new ResponseCode<>(100025, "权限码已经存在");
    public static final ResponseCode<?> PARENT_ID_NOT_EXIST = new ResponseCode<>(100026, "父级id不存在");
    public static final ResponseCode<?> ACCOUNT_NOT_EXIST = new ResponseCode<>(100027, "账号或密码不存在");
    public static final ResponseCode<?> WECHART_REMOTE_BUSY = new ResponseCode<>(100028, "微信系统繁忙，请稍后重试");
    public static final ResponseCode<?> WECHART_INVALID_CODE = new ResponseCode<>(100029, "微信登录CODE己失效，请重试");
    public static final ResponseCode<?> WECHART_USER_LIMIT = new ResponseCode<>(100030, "频率限制，每个用户每分钟100次");
    public static final ResponseCode<?> PARENT_NOT_EXIST = new ResponseCode<>(100031, "父类不存在");
    public static final ResponseCode<?> GOODS_CLASS_NOT_EXIST = new ResponseCode<>(100032, "分类不存在");
    public static final ResponseCode<?> GREAT_THAN_MAX_LEVEL = new ResponseCode<>(100033, "分类超过最大等级");
    public static final ResponseCode<?> NAME_EXIST = new ResponseCode<>(100034, "同级分类中,该名称已使用");
    public static final ResponseCode<?> GOODS_EXIST_OF_PARENT = new ResponseCode<>(100035, "上级或与上级同级的分类存在商品");
    public static final ResponseCode<?> GOODS_EXIST_OF_CHILD = new ResponseCode<>(100036, "子类存在商品");
    public static final ResponseCode<?> GOODS_EXIST_OF_ME = new ResponseCode<>(100036, "该分类存在商品");
    public static final ResponseCode<?> PARENT_WITH_ME_IS_SAME_ONE = new ResponseCode<>(100037, "父级分类是自己");
    public static final ResponseCode<?> CREATE_OSS_FILE = new ResponseCode<>(100038, "创建阿里云oss失败");

    /***********************************************************/
    public static final ResponseCode<?> PRE_ORDER_FAIL = new ResponseCode<>(100061, "预下单接口调用异常");
    public static final ResponseCode<?> ORDER_CONFIRM_FAIL = new ResponseCode<>(100062, "订单已经支付");

    private int code;
    private String msg;
    private T data;

    public ResponseCode() {
    }

    public ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseCode(ResponseCode<T> responseCode, String msg) {
        this.code = responseCode.getCode();
        this.msg = msg;
    }

    public static <T> ResponseCode<T> buildResponse(T data) {
        ResponseCode<T> responseCode = new ResponseCode<>(SUCCESS.getCode(), "请求成功");
        responseCode.setData(data);
        return responseCode;
    }

    public boolean isSuccess() {
        return this.code == SUCCESS.getCode();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

