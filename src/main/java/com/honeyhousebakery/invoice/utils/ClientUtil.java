package com.honeyhousebakery.invoice.utils;

import com.honeyhousebakery.invoice.domain.Client;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dwind
 */
public class ClientUtil {

    public static List<Client> getDefaultClientsList() {
        
        List<Client> defaultClients = new ArrayList<Client>();
        
        defaultClients.add(new Client("JW Marriott Marquis Miami", "255 Biscayne Boulevard Way", "33131", "Miami, FL", "1"));
        defaultClients.add(new Client("JW Marriott Miami", "1109 Brickell Ave", "33131", "Miami, FL", "2"));
        defaultClients.add(new Client("Miami Marriott Dadeland", "9090 S. Dadeland Blvd.", "33156", "Miami, FL", "3"));
        defaultClients.add(new Client("Intercontinental Hotel", "2505 NW. 87th ave Doral", "33172", "Miami, FL", "4"));
        defaultClients.add(new Client("Braza Viva Doral", "7910 NW. 25 St.", "33172", "Miami, FL", "5"));
        defaultClients.add(new Client("Braza Viva Sunrise", "14301 W. Sunrise Blvd", "33323", "Miami, FL", "6"));
        defaultClients.add(new Client("Shiraz Catering FL INC", "7515 SW 17th ave suite #2", "33135", "Miami, FL", "7"));
        defaultClients.add(new Client("Morning Star", "124 SW. 73th ave.", "33135", "Miami, FL", "8"));
        
        return defaultClients;
    }
}