package com.huanghe.exception;

public class FileZipException extends FileOperException {
    /**
     * 无参构造函数
     */
    public FileZipException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * @param message message
     */
    public FileZipException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code FileZipException} with the specified detail message
     * and cause.
     *
     * <p> Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated into this exception's detail
     * message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public FileZipException(String message, Throwable cause) {
        super(message, cause);
    }
}
