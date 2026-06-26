package classes;


public class user_preferences {
private int user_id;
private int maxbudget;
private int minbudget;
private String preffered_color;
private String preffered_fueltype;
private String preffered_bodytype;
private String preffered_transmission;
private int preffered_num_seats;
private String purpose;
public user_preferences(int user_id, int maxbudget, int minbudget, String preffered_color, String preffered_fueltype,
		String preffered_bodytype, String preffered_transmission, int preffered_num_seats, String purpose) {
	super();
	this.user_id = user_id;
	this.maxbudget = maxbudget;
	this.minbudget = minbudget;
	this.preffered_color = preffered_color;
	this.preffered_fueltype = preffered_fueltype;
	this.preffered_bodytype = preffered_bodytype;
	this.preffered_transmission = preffered_transmission;
	this.preffered_num_seats = preffered_num_seats;
	this.purpose = purpose;
}
public int getUser_id() {
	return user_id;
}
public void setUser_id(int user_id) {
	this.user_id = user_id;
}
public int getMaxbudget() {
	return maxbudget;
}
public void setMaxbudget(int maxbudget) {
	this.maxbudget = maxbudget;
}
public int getMinbudget() {
	return minbudget;
}
public void setMinbudget(int minbudget) {
	this.minbudget = minbudget;
}
public String getPreffered_color() {
	return preffered_color;
}
public void setPreffered_color(String preffered_color) {
	this.preffered_color = preffered_color;
}
public String getPreffered_fueltype() {
	return preffered_fueltype;
}
public void setPreffered_fueltype(String preffered_fueltype) {
	this.preffered_fueltype = preffered_fueltype;
}
public String getPreffered_bodytype() {
	return preffered_bodytype;
}
public void setPreffered_bodytype(String preffered_bodytype) {
	this.preffered_bodytype = preffered_bodytype;
}
public String getPreffered_transmission() {
	return preffered_transmission;
}
public void setPreffered_transmission(String preffered_transmission) {
	this.preffered_transmission = preffered_transmission;
}
public int getPreffered_num_seats() {
	return preffered_num_seats;
}
public void setPreffered_num_seats(int preffered_num_seats) {
	this.preffered_num_seats = preffered_num_seats;
}
public String getPurpose() {
	return purpose;
}
public void setPurpose(String purpose) {
	this.purpose = purpose;
}



}

