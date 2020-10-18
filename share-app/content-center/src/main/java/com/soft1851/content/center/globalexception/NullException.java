package com.soft1851.content.center.globalexception;

/**
 * @author xunmi
 * @ClassName NullException
 * @Description TODO
 * @Date 2020/10/15
 * @Version 1.0
 **/
public class NullException extends Throwable {

    private String msg;

    public NullException() {
        super();
    }

    public NullException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

}
