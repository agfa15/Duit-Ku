package com.example.duitku;

public class Data {

    String nama, item, date, id, deskripsi;
    int ammount, month;

    public Data(){

    }

    public Data(String nama, String date, String id, String deskripsi, int ammount, int month) {
        this.nama = nama;
        this.date = date;
        this.id = id;
        this.deskripsi = deskripsi;
        this.ammount = ammount;
        this.month = month;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
