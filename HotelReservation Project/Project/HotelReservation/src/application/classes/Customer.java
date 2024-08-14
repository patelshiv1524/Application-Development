/**********************************************
Project
Course:APD545 - Semester 5
Last Name:Narahari
First Name:Dhivi
ID:156429219
Section:NAA
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: Dhivi
Date:11.30.2023
**********************************************/
package application.classes;

public class Customer {
	// Properties of the Customer class
    private String title;
    private String firstName;
    private String lastName;
    private String address;
    private int phone;
    private String email;
    
 // Constructor to initialize the Customer object with provided values
	public Customer(String title, String firstName, String lastName, String address, int phone, String email) {
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}
	  // Getter and setter methods for the properties of the Customer class

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
    
}
