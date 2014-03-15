package com.honeyhousebakery.invoice.domain;

import com.honeyhousebakery.invoice.utils.OrderType;
import com.honeyhousebakery.invoice.utils.InvoiceUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danielwind
 */
public class Order {
    
    protected String invoiceId;
    private String date;
    private Client client;
    private OrderType type;
    protected List<Purchase> purchases;
    private String orderTotalWithDiscount;

    public Order() {
        this.purchases = new ArrayList<Purchase>();
        this.invoiceId = InvoiceUtil.getNextInvoiceID();
        this.orderTotalWithDiscount = "0.00";
    }
    
    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @param purchases the purchases to set
     */
    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }
    
    public String getInvoiceId() {
        return this.invoiceId;
    }

    /**
     * @return the type
     */
    public OrderType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(OrderType type) {
        this.type = type;
    }
    
    public List<Purchase> getPurchases() {
        return this.purchases;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the orderTotalWithDiscount
     */
    public String getOrderTotalWithDiscount() {
        return orderTotalWithDiscount;
    }

    /**
     * @param orderTotalWithDiscount the orderTotalWithDiscount to set
     */
    public void setOrderTotalWithDiscount(String orderTotalWithDiscount) {
        
        if(!orderTotalWithDiscount.equals("")) {
            
            BigDecimal discountTotal = new BigDecimal(orderTotalWithDiscount);
        
            int result = discountTotal.compareTo(getOrderTotalAmount());

            switch(result) {

                case -1:
                  this.orderTotalWithDiscount = orderTotalWithDiscount;
                break;

                case 0:
                  this.orderTotalWithDiscount = getOrderTotalAmount().toPlainString();  
                  System.out.println("Disount Total and Order total are equal hence doing nothing...");
                break;

                case 1:
                  System.out.println("ERROR: Disount Total IS GREATER THAN Order total hence someone is trying to hack this stuff..."); 
                break;    
            }
            
        } else {
            this.orderTotalWithDiscount = getOrderTotalAmount().toPlainString();
        }
        
        
        
    }
    
    public BigDecimal getOrderTotalAmount() {
        
        BigDecimal total = new BigDecimal("0.00");
        
        for (Purchase purchase : purchases) {
            total = total.add(purchase.getLinetotal());
        }
        
        return total;
    }
    
    public BigDecimal getAppliedDiscount() {
        
        BigDecimal discountDifference = new BigDecimal("0.00");
        BigDecimal discountTotal = new BigDecimal(orderTotalWithDiscount);
        
        if(!orderTotalWithDiscount.equals("0.00")){
            discountDifference = getOrderTotalAmount().subtract(discountTotal);
        }
        
        return discountDifference;
    }
    
}