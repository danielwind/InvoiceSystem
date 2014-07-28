package com.honeyhousebakery.invoice.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author danielwind
 */
public class InvoiceUtil {
    
    private static String LEADING_ZEROES = "";
    
    public static final int DEFAULT_ID = 291;
    
    public static String getNextSequenceId(int id) {
        
        Logger.getLogger(InvoiceUtil.class.getName()).log(Level.INFO, "ID AS PASSED FROM DOMAIN OBJ IS: {0}", id);
        
        if(id > 0){
            
            if(id < 1000){
                LEADING_ZEROES = "000";
            } else if(id >= 1000 && id < 10000) {
                LEADING_ZEROES = "00";
            } else if(id >= 10000 && id < 100000) {
                LEADING_ZEROES = "0";
            }
        } 
        
        return LEADING_ZEROES + String.valueOf(id);
    }
}
