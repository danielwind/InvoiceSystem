package com.honeyhousebakery.invoice.domain;

/**
 *
 * @author danielwind
 */
public class Invoice {
   
    private int invoiceId;
    private String clientName;
    private String invoiceDate;
    private String orderTotal;
    private String orderTotalWithDiscount;

    /**
     * @return the invoiceId
     */
    public int getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the invoiceDate
     */
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate the invoiceDate to set
     */
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * @return the orderTotal
     */
    public String getOrderTotal() {
        return orderTotal;
    }

    /**
     * @param orderTotal the orderTotal to set
     */
    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
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
        this.orderTotalWithDiscount = orderTotalWithDiscount;
    }
    
    public Object[] toObjectArray() {
        return new Object[]{invoiceId, invoiceDate, clientName, orderTotal, orderTotalWithDiscount};
    }
}
