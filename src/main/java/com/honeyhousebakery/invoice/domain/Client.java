package com.honeyhousebakery.invoice.domain;

/**
 *
 * @author danielwind
 */
public class Client {
    
    private String id;
    private String name;
    private String address;
    private String state;
    private String zipCode;
    private String contactInfo;

    
    public Client(String name, String address, String zipCode, String state) {
        
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.state = state;
        
    }
    
    public Client(String name, String address, String zipCode, String state, String id) {
        
        this.id = id;
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.state = state;
        
    }
    
    public Client(String name, String address, String contactInfo) {
        
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the contactInfo
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * @param contactInfo the contactInfo to set
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("Client name: " + this.getName());
        sb.append(" , ");
        sb.append("Address: " + this.getAddress());
        sb.append(" , ");
        sb.append("State: " + this.getState());
        sb.append(" , ");
        sb.append("Zip Code: " + this.getZipCode());
        
        return sb.toString();
        
    }
}