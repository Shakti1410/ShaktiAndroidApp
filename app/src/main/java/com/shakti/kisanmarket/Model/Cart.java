package com.shakti.kisanmarket.Model;

public class Cart {

    private String pid,pname,price,quantity,discount,psellername,psellerphone;

    public Cart() {

    }

    public Cart(String pid, String pname, String price, String quantity, String discount, String psellername, String psellerphone) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.psellername = psellername;
        this.psellerphone = psellerphone;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPsellername() {
        return psellername;
    }

    public void setPsellername(String psellername) {
        this.psellername = psellername;
    }

    public String getPsellerphone() {
        return psellerphone;
    }

    public void setPsellerphone(String psellerphone) {
        this.psellerphone = psellerphone;
    }
}
