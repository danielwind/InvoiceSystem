package com.honeyhousebakery.invoice.datasource;

import com.honeyhousebakery.invoice.domain.Client;
import com.honeyhousebakery.invoice.domain.Invoice;
import com.honeyhousebakery.invoice.domain.Order;
import com.honeyhousebakery.invoice.utils.ClientUtil;
import com.honeyhousebakery.invoice.utils.InvoiceUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

public final class MongoDBConnector {
	
	private static final String DATABASE_NAME = "hhb_invoice_system";
        private static final String DATABASE_USERNAME = "hhb";
        private static final String DATABASE_PASSWORD = "h@neyhouse2014";
        private static final int ORDER_SEQUENCE_INCREMENT = 1;
        private static final String ORDER_SEQUENCE_NAME = "orderid";
        private static final String ORDER_COLLECTION_NAME = "orders";
        private static final String COUNTER_COLLECTION_NAME = "counters";
        private static final String CLIENT_COLLECTION_NAME = "clients";
        
        private MongoClient mongoClient;
        private DB db;
	
	public MongoDBConnector() throws UnknownHostException {
	    
            MongoCredential credentials = MongoCredential.createMongoCRCredential(DATABASE_USERNAME, DATABASE_NAME, DATABASE_PASSWORD.toCharArray());	
            mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credentials));
            
            db = mongoClient.getDB(DATABASE_NAME);
	}
        
        public synchronized void saveOrder(Order order) {
            
            //1. Get collection
            DBCollection ordersCollection = db.getCollection(ORDER_COLLECTION_NAME);
            
            //2. Get Order Information
            Client client = order.getClient();
            
            Logger.getLogger(MongoDBConnector.class.getName()).log(Level.INFO, "SAVING ORDER IN DATABASE: [{0}, {1}, {2}, {3}]", new String[]{order.getDate(), client.getName(), 
                                                                                                                                                order.getOrderFullTotalAmount().toPlainString(), 
                                                                                                                                                order.getOrderTotalAmount().toPlainString()});
            
            //3. Save it!
            BasicDBObject orderDocument = new BasicDBObject("_id", getNextSequence())
                                                                  .append("client_info", new BasicDBObject("client_id", client.getId())
                                                                                                        .append("client_name", client.getName())
                                                                                                        .append("client_address", client.getAddress())
                                                                                                        .append("client_state", client.getState())
                                                                                                        .append("client_zipcode", client.getZipCode())
                                                                                                        .append("client_contact", client.getContactInfo())
                                                                                        )
                                                                  .append("order_info", new BasicDBObject("order_total", order.getOrderFullTotalAmount().toPlainString())
                                                                                                        .append("order_discount", order.getDiscount().toPlainString())
                                                                                                        .append("order_date", order.getDate())
                                                                                        );                              
          
            ordersCollection.insert(orderDocument);
        } 
        
        //Retrieve last order id
        private synchronized int getNextSequence() {   
            
            int sequenceId = 0;
            
            //1. Get collection
            DBCollection countersCollection = db.getCollection(COUNTER_COLLECTION_NAME);
            
            if(countersCollection.count() == 0){
                Logger.getLogger(MongoDBConnector.class.getName()).log(Level.SEVERE, "Counters collection does not exist, pushing records...");
                
                BasicDBObject doc = new BasicDBObject("_id", ORDER_SEQUENCE_NAME).append("seq", InvoiceUtil.DEFAULT_ID); 
                
                countersCollection.insert(doc);
            }
            
            final BasicDBObject query = new BasicDBObject("_id", ORDER_SEQUENCE_NAME);
            final BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject("seq", ORDER_SEQUENCE_INCREMENT));
        
            final DBObject modifiedDocument = countersCollection.findAndModify(query, null, null, false, update, true, false);
            
            if(modifiedDocument == null){
                Logger.getLogger(MongoDBConnector.class.getName()).log(Level.SEVERE, "Modified document is null, possibly due to empty database (No orders). Using configurable id..."); 
                sequenceId = InvoiceUtil.DEFAULT_ID;   
                
            } else {
                sequenceId = (Integer)modifiedDocument.get("seq");
            }
            
            
            return sequenceId;
        }
        
        public synchronized int getLastExistingPlusOneSequence() {
            
            DBCollection countersCollection = db.getCollection(COUNTER_COLLECTION_NAME);
            
            if(countersCollection.count() == 0){
                Logger.getLogger(MongoDBConnector.class.getName()).log(Level.SEVERE, "Counters collection does not exist, pushing records...");
                
                BasicDBObject doc = new BasicDBObject("_id", ORDER_SEQUENCE_NAME).append("seq", InvoiceUtil.DEFAULT_ID); 
                
                countersCollection.insert(doc);
            }
            BasicDBObject query = new BasicDBObject("_id", ORDER_SEQUENCE_NAME);
            
            DBObject modifiedDocument = countersCollection.findOne(query);
            
            int sequenceId = (Integer)modifiedDocument.get("seq") + 1;
            
            return sequenceId;
        }
        
        //Retrieve all orders!
        public synchronized List<Invoice> queryForOrders() {   
            
            List<Invoice> invoices = new ArrayList<Invoice>();
            
            //1. Get collection
            DBCollection ordersCollection = db.getCollection(ORDER_COLLECTION_NAME);
            
            //2. Query DB for all existing clients
            DBCursor cursor = ordersCollection.find();
                
            try {
                while(cursor.hasNext()) {
                    
                    DBObject orderRecord = cursor.next();
                    
                    int invoiceId = (Integer)orderRecord.get("_id");
                    
                    BasicDBObject orderInfoObj = (BasicDBObject)orderRecord.get("order_info");
                    String date = orderInfoObj.getString("order_date");
                    String orderTotal = (String)orderInfoObj.get("order_total");
                    String discount = (String)orderInfoObj.get("order_discount");
                    
                    BasicDBObject clientInfoObj = (BasicDBObject)orderRecord.get("client_info");
                    String client = (String)clientInfoObj.get("client_name");
                    
                    Invoice invoice = new Invoice();
                    invoice.setClientName(client);
                    invoice.setInvoiceDate(date);
                    invoice.setInvoiceId(invoiceId);
                    invoice.setOrderTotal(orderTotal);
                    invoice.setOrderTotalWithDiscount(discount);

                    //add invoice to invoices array
                    invoices.add(invoice);
                    
                }
            } finally {
                cursor.close();
            }
            
            return invoices;
        }
        
        public synchronized List<Client> getClientList() {
            
            List<Client> clients = new ArrayList<Client>();
            
            //1. Get collection
            DBCollection clientsCollection = db.getCollection(CLIENT_COLLECTION_NAME);
            
            if(clientsCollection == null) {
                 Logger.getLogger(MongoDBConnector.class.getName()).log(Level.SEVERE, "COLLECTION IS NULL!!!!");
            }
            
            //2. Query DB for all existing clients
            DBCursor cursor = clientsCollection.find();
            
             Logger.getLogger(MongoDBConnector.class.getName()).log(Level.SEVERE, "CURSOR LENGTH IS: " + cursor.length());
            
            if(cursor.length() <= 0){
               
                //bulk insert!
               BulkWriteOperation builder = clientsCollection.initializeOrderedBulkOperation(); 
               
               //read default client list
               List<Client> defaultClients = ClientUtil.getDefaultClientsList();
               
               for(Client defaultClient : defaultClients){
                   Logger.getLogger(MongoDBConnector.class.getName()).log(Level.INFO, "ENTERING HERE!!");
                    BasicDBObject defaultClientDocument = new BasicDBObject("_id", defaultClient.getId())
                       .append("client_name", defaultClient.getName())
                       .append("client_address", defaultClient.getAddress())
                       .append("client_zipcode", defaultClient.getZipCode())
                       .append("client_state", defaultClient.getState());
                   
                    builder.insert(defaultClientDocument);
               } 
               
               //commit
               BulkWriteResult result = builder.execute();
               
               cursor.close();
            }
            
            DBCollection clientsFinalCollection = db.getCollection(CLIENT_COLLECTION_NAME);
            DBCursor finalCursor = clientsFinalCollection.find();

            while(finalCursor.hasNext()) {

                DBObject clientRecord = finalCursor.next();

                String id = clientRecord.get("_id").toString();
                String name = (String)clientRecord.get("client_name");
                String address = (String)clientRecord.get("client_address");
                String zipCode = (String)clientRecord.get("client_zipcode");
                String state = (String)clientRecord.get("client_state");

                Client client = new Client(name, address, zipCode, state, id);

                //add client to clients array and return it!
                clients.add(client);
            }
            
            finalCursor.close();
            
            return clients;
        }

}

