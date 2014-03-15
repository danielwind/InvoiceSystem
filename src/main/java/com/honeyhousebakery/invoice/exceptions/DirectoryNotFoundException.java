package com.honeyhousebakery.invoice.exceptions;

/**
 * @author danielwind
 */

public class DirectoryNotFoundException extends RuntimeException {

    public DirectoryNotFoundException() {
    }

    public DirectoryNotFoundException(String string) {
        super(string);
    }

    public DirectoryNotFoundException(Throwable thrwbl) {
        super(thrwbl);
    }

    public DirectoryNotFoundException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
