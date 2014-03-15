package com.honeyhousebakery.invoice.data;

import org.hsqldb.Server;

/**
 * @author danielwind
 */
public class DatabaseManager {
    
    private static Server dbServer = null;
    
    public static void start() {
    
        dbServer = new Server();
        
    }
    
}
