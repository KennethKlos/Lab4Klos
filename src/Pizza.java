/**
 * Project: Lab 4
 * Purpose Details: Pizza class for sending and receiving pizza data
 * Course: IST 242
 * Author: Ken Klos
 * Date Developed:
 * Last Date Changed:
 * Rev:
 */
public class Pizza {

    private String size;
    private String crust;
    private String toppings;
    private double price;

    public Pizza() {
    }

    public Pizza(String size, String crust, String toppings, double price) {
        this.size = size;
        this.crust = crust;
        this.toppings = toppings;
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCrust() {
        return crust;
    }

    public void setCrust(String crust) {
        this.crust = crust;
    }

    public String getToppings() {
        return toppings;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return size + "," + crust + "," + toppings + "," + price;
    }
}