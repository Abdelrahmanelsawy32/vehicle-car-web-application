package classes;



public class Car {
private int id;
private String model;
private String brand;
private int price;
private String fueltype;
private String bodytype;
private String transmission;
private int num_seats;


public Car(int id, String model, String brand,  int price, String fueltype, String bodytype,
		String transmission, int num_seats) {
	super();
	this.id = id;
	this.model = model;
	this.brand = brand;
	
	this.price = price;
	this.fueltype = fueltype;
	this.bodytype = bodytype;
	this.transmission = transmission;
	this.num_seats = num_seats;
}
public String getBrand() {
	return brand;
}
public void setBrand(String brand) {
	this.brand = brand;
}
public int getNum_seats() {
	return num_seats;
}
public void setNum_seats(int num_seats) {
	this.num_seats = num_seats;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getModel() {
	return model;
}
public void setModel(String model) {
	this.model = model;
}

public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}
public String getFueltype() {
	return fueltype;
}
public void setFueltype(String fueltype) {
	this.fueltype = fueltype;
}
public String getBodytype() {
	return bodytype;
}
public void setBodytype(String bodytype) {
	this.bodytype = bodytype;
}
public String getTransmission() {
	return transmission;
}
public void setTransmission(String transmission) {
	this.transmission = transmission;
}

}