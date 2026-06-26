package classes;



public class User {
private int id;
private String name;
private int age;
private String gender;
private String location;
private String username;
private String password;
private String role;



public User(int id, String name, int age, String gender, String location, String username, String password,
		String role) {
	super();
	this.id = id;
	this.name = name;
	this.age = age;
	this.gender = gender;
	this.location = location;
	this.username = username;
	this.password = password;
	this.role = role;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}

public String getLocation() {
	return location;
}
public void setLocstion(String locstion) {
	this.location = locstion;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public void setLocation(String location) {
	this.location = location;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}



}

