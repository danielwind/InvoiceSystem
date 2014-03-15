package com.honeyhousebakery.invoice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author danielwind
 */
public class DateUtil {
    
    public static String invoiceFormattedDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    
    public static String invoiceGUIFormattedDate() {
        return new SimpleDateFormat("EEEE MMM dd, yyyy").format(new Date());
    }
}
