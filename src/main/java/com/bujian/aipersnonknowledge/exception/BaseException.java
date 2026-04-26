//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bujian.aipersnonknowledge.exception;

public class BaseException extends RuntimeException {
    private static final String ERROR_MSG = "服务器处理失败!";
    private static final long serialVersionUID = 2808361012552245040L;

    public BaseException() {
        super("服务器处理失败!");
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable error) {
        super("服务器处理失败!", error);
    }

    public BaseException(String message, Throwable error) {
        super(message, error);
    }
}
