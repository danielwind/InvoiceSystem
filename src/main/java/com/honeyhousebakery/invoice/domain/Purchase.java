package com.honeyhousebakery.invoice.domain;

import java.math.BigDecimal;

/**
 *
 * @author danielwind
 */
public class Purchase {
    
    private String itemDescription;
    private BigDecimal itemPrice;
    private int quantity;
    private BigDecimal lineTotal;

    /**
     * @return the itemDescription
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * @param itemDescription the itemDescription to set
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * @return the itemPrice
     */
    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    /**
     * @param itemPrice the itemPrice to set
     */
    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the subtotal
     */
    public BigDecimal getLinetotal() {
        return lineTotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setLineTotal(BigDecimal lineTotal) {
        //this.lineTotal = lineTotal;
    }
    
    public BigDecimal calculateLineTotal() {
        
        lineTotal = itemPrice.multiply(new BigDecimal(quantity));
        
        return lineTotal;
    }
    
    public Object[] toObjectArray() {
        return new Object[]{itemDescription, itemPrice, quantity, lineTotal};
    }
    
}
