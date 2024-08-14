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

import java.time.LocalDate;
import java.util.List;

public class Reservation {
	private List<room> selectedRooms;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int bookingId;
    private int customerId;
    private LocalDate BookingDate;
    private String customerName;
    private String typeOfRooms;
	private double rate;
    private int numberOfGuests;
    private int numberOfDays;
    private int numberOfRooms;
    
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getNumberOfRooms() {
		return numberOfRooms;
	}
	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public String getTypeOfRooms() {
		return typeOfRooms;
	}
	public void setTypeOfRooms(String typeOfRooms) {
		this.typeOfRooms = typeOfRooms;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<room> getSelectedRooms() {
		return selectedRooms;
	}
	public void setSelectedRooms(List<room> selectedRooms) {
		this.selectedRooms = selectedRooms;
	}
	public LocalDate getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}
	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getNumberOfGuests() {
		return numberOfGuests;
	}
	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	 public LocalDate getBookingDate() {
			return BookingDate;
		}
		public void setBookingDate(LocalDate bookingDate) {
			BookingDate = bookingDate;
		}
    
}
