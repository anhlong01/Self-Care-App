package com.lph.selfcareapp.model;

public class Patient {
    private int pid;
    private String pemail;
    private String pname;
    private String ppassword;
    private String paddress;
    private Integer ptel;
    private String pdob;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPemail() {
        return pemail;
    }

    public void setPemail(String pemail) {
        this.pemail = pemail;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPpassword() {
        return ppassword;
    }

    public void setPpassword(String ppassword) {
        this.ppassword = ppassword;
    }

    public String getPaddress() {
        return paddress;
    }

    public void setPaddress(String paddress) {
        this.paddress = paddress;
    }

    public Integer getPtel() {
        return ptel;
    }

    public void setPtel(Integer ptel) {
        this.ptel = ptel;
    }

    public String getPdob() {
        return pdob;
    }

    public void setPdob(String pdob) {
        this.pdob = pdob;
    }
}
