package com.honeyhousebakery.invoice.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author danielwind
 */
public class InvoiceUtil {
    
    private static int INVOICE_ID = 100;
    private static String LEADING_ZEROES = ""; 
    
    public static String getNextInvoiceID() {
        
        INVOICE_ID++;
        
        if(INVOICE_ID < 1000){
            LEADING_ZEROES = "000";
        } else if(INVOICE_ID >= 1000 && INVOICE_ID < 10000) {
            LEADING_ZEROES = "00";
        } else if(INVOICE_ID >= 10000 && INVOICE_ID < 100000) {
            LEADING_ZEROES = "0";
        }
        
        return LEADING_ZEROES + String.valueOf(INVOICE_ID);
    }
    
    public static void updateInitialInvoiceID(int id) {
        Logger.getLogger(InvoiceUtil.class.getName()).log(Level.INFO, "ID AS RETRIEVED FROM DATABASE IS: {0}", id);
        
        if(id > 0){
            INVOICE_ID = id;
        }   
    }
}
