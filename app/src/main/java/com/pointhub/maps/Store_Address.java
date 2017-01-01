package com.pointhub.maps;

/**
 * Created by Admin on 12/16/2016.
 */

public class Store_Address
{
    private String Address;
    private String Locality;

    public Store_Address(){}

    public Store_Address(String Address, String Locality){
        this.Address=Address;
        this.Locality=Locality;

    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }
}

