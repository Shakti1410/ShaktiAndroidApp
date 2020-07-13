package com.shakti.kisanmarket.Model;

public class Feedback {
    String pid,pname,rating,review,uname,uphone;

    public Feedback() {
    }

    public Feedback(String pid, String pname, String rating, String review, String uname, String uphone) {
        this.pid = pid;
        this.pname = pname;
        this.rating = rating;
        this.review = review;
        this.uname = uname;
        this.uphone = uphone;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
