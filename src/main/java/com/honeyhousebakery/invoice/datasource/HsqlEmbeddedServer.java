package com.honeyhousebakery.invoice.datasource;

import com.honeyhousebakery.invoice.domain.Client;
import com.honeyhousebakery.invoice.domain.Invoice;
import com.honeyhousebakery.invoice.domain.Order;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HsqlEmbeddedServer {
	
	private static final String DATABASE_PATH = System.getProperty("user.dir") + "/db;shutdown=true";
	private static final String HSQLDB_JDBC_DRIVER = "org.hsqldb.jdbcDriver";
	
        private Connection connection;
	
	public HsqlEmbeddedServer() {
		
		Logger.getLogger(HsqlEmbeddedServer.class.getName()).log(Level.INFO, "--- INITIALIZING HSQLDB EMBEDDED ENGINE ---");
	}
	
	/**
	 * Connects to the HSQLDB Embedded Server
	 */
	public void connect() {
		
		connection = null;
		
		try {
			
			Class.forName(HSQLDB_JDBC_DRIVER);
                        
			connection = DriverManager.getConnection("jdbc:hsqldb:" + DATABASE_PATH,"sa","");
			
			Logger.getLogger(HsqlEmbeddedServer.class.getName()).log(Level.INFO, "--- HSQLDB ENGINE STARTED SUCCESSFULLY PER CONNECT() REQUEST ---");
			
		} catch (ClassNotFoundException e) {
			Logger.getLogger(HsqlEmbeddedServer.class.getName()).log(Level.SEVERE, "** THERE WAS A PROBLEM LOADING THE JDBC DRIVER **");
		
		} catch (SQLException e) {
                        Logger.getLogger(HsqlEmbeddedServer.class.getName()).log(Level.SEVERE, "** THERE WAS A PROBLEM WITH THE SQL CONNECTION: {0}", e.getMessage().toLowerCase());
		
		}
                
	}
        
        //use for SQL commands CREATE, DROP, INSERT and UPDATE
        public synchronized void saveOrder(Order order) throws SQLException {
            
            String date = order.getDate(); 
            String client = order.getClient().getName();
            String orderTotalAmount = order.getOrderFullTotalAmount().toPlainString();
            String orderTotalWithDiscount = order.getOrderTotalAmount().toPlainString();
            
            Logger.getLogger(HsqlEmbeddedServer.class.getName()).log(Level.INFO, "SAVING ORDER IN DATABASE: [{0}, {1}, {2}, {3}]", new String[]{date, client, orderTotalAmount, orderTotalWithDiscount});
            
            connection.prepareStatement("INSERT INTO ORDERS(DATE, CLIENT, ORDER_TOTAL ,ORDER_WITH_DISCOUNT) " 
                                        + "VALUES ('"+ date + "', '" + client + "', '" + orderTotalAmount + "','" + orderTotalWithDiscount + "');").execute();
        } 
        
        //Retrieve Order Last ID ONLY!
        public synchronized int queryForLastUpdatedOrderId() throws SQLException {   
            
            // run the query
            ResultSet rs = connection.prepareStatement("SELECT TOP 1 ID FROM ORDERS ORDER BY ID DESC").executeQuery();
            
            int lastOrderId = 0;
            
            if(rs.next()) {
                lastOrderId = rs.getInt(1);
                Logger.getLogger(HsqlEmbeddedServer.class.getName()).log(Level.INFO, "The LAST INSERTED ORDER ID: {0}", lastOrderId);
            }
            
            return lastOrderId;
        }
        
        public synchronized List<Invoice> queryForOrders() throws SQLException {   
            
            List<Invoice> invoices = new ArrayList<Invoice>();
            
            // run the query
            ResultSet rs = connection.prepareStatement("SELECT * FROM ORDERS").executeQuery();
            
            while(rs.next()) {

                int invoiceId = rs.getInt("ID");
                String date = rs.getString("DATE");
                String client = rs.getString("CLIENT");
                String orderTotal = rs.getString("ORDER_TOTAL");
                String orderWithDiscount = rs.getString("ORDER_WITH_DISCOUNT");
                
                Invoice invoice = new Invoice();
                invoice.setClientName(client);
                invoice.setInvoiceDate(date);
                invoice.setInvoiceId(invoiceId);
                invoice.setOrderTotal(orderTotal);
                invoice.setOrderTotalWithDiscount(orderWithDiscount);
                
                //add invoice to invoices array
                invoices.add(invoice);
            }
            
            return invoices;
        }
        
        public synchronized List<Client> getClientList() throws SQLException {
            
            List<Client> clients = new ArrayList<Client>();
            
            // run the query
            ResultSet rs = connection.prepareStatement("SELECT * FROM CLIENTS").executeQuery();
            
            while(rs.next()) {
                
                int id = rs.getInt("CLIENT_ID");
                String name = rs.getString("NAME");
                String address = rs.getString("ADDRESS");
                String zipCode = rs.getString("ZIPCODE");
                String state = rs.getString("STATE");
                 
                Client client = new Client(name, address, zipCode, state, id);
                
                //add invoice to invoices array
                clients.add(client);
            }
            
            return clients;
        }
        
        /**
         * Utility Method, not intended for public usage
         */
        public synchronized void deleteAllRecords() throws SQLException{
            connection.prepareStatement("DELETE FROM ORDERS;").execute();
        }
	
	/**
	 * Stops the HSQLDB Embedded Server
	 */
	public void terminate() throws SQLException {
            
            Logger.getLogger(HsqlEmbeddedServer.class.getName()).log(Level.INFO, "---- TERMINATING HSQLDB PROCESS PER TERMINATE() REQUEST ----");
            Statement st = connection.createStatement();
            st.execute("SHUTDOWN");
            
            connection.close();
	}
        
        public synchronized void alterOrderPrimaryKey(String newKey) throws SQLException{
            connection.prepareStatement("ALTER TABLE ORDERS ALTER COLUMN ID RESTART WITH " + newKey).execute();
        }

}

