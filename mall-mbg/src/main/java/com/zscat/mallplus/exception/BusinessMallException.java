//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.exception;

public class BusinessMallException extends RuntimeException {
    private static final long serialVersionUID = 2874510430549463213L;
    private int returnCode;

    public BusinessMallException() {
    }

    public BusinessMallException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessMallException(String message) {
        super(message);
    }

    public BusinessMallException(Throwable cause) {
        super(cause);
    }

    public BusinessMallException(int returnCode) {
        this.returnCode = returnCode;
    }

    public BusinessMallException(Exception e, int returnCode) {
        super(e);
        this.returnCode = returnCode;
    }

    public BusinessMallException(String message, int returnCode) {
        super(message);
        this.returnCode = returnCode;
    }

    public BusinessMallException(String message, Exception e, int returnCode) {
        super(message, e);
        this.returnCode = returnCode;
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }
}
