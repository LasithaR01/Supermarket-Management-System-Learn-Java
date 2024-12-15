package Model;

import java.time.LocalDate;

public class Product {
	private int id;
	private char classId;
	private String name;
	private double price;
	private LocalDate expiraDate;
	private String brand;
	
    public Product() {
    	
    }
    
    public Product(int id, char classId, String name, double price, LocalDate expiraDate, String brand) {
    	this.id = id;
    	this.classId = classId;
    	this.name = name;
    	this.price = price;
    	this.expiraDate = expiraDate;
    	this.brand = brand;
    }
    
    //Getter Setter Method
    public int getId() {
    	return id;
    }
	
    public void setId(int id) {
    	this.id = id;
    }
    
    public char getClassId() {
    	return classId;
    }
    
    public void setClassId(char classId) {
    	this.classId = classId;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public double getPrice() {
    	return price;
    }
    
    public void setPrice(double price) {
    	this.price = price;
    }
    
    public LocalDate getExpiraDate() {
    	return expiraDate;
    }
    
    public void setExpiraDate(LocalDate expiraDate) {
    	this.expiraDate = expiraDate;
    }
    
    public String getBrand() {
    	return brand;
    }
    
    public void setBrand(String brand) {
    	this.brand = brand;
    }
}
