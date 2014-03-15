package com.honeyhousebakery.invoice.interceptors;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class acts as a global exception interceptor
 * and sends the message back to the UI in order to
 * let the user know something was screwed.
 * 
 * @author danielwind
 */
public class ExceptionInterceptor implements Thread.UncaughtExceptionHandler {

    public void uncaughtException(Thread thread, Throwable thrwbl) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Interceptor -- Exception intercepted and sending alert to UI...");
    }
    
}
