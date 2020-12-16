package com.example.contactbook;

import android.database.Cursor;

public class ModelClass
{
    String name,pcontact, scontact,email;
    Cursor cursor;

    ModelClass()
    {

    }

    public ModelClass(String name, String pcontact,String scontact, String email)
    {
        this.name = name;
        this.pcontact = pcontact;
        this.scontact = scontact;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPcontact() {
        return pcontact;
    }

    public void setPcontact(String pcontact) {
        this.pcontact = pcontact;
    }

    public String getScontact() {
        return scontact;
    }

    public void setScontact(String scontact) {
        this.scontact = scontact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
