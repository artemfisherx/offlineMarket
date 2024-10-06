package mainPackage.Entities;

import mainPackage.Validation.TextLength;

public class Address {
	
	@TextLength(max=20, message = "Minimum length is 1 symbol and max length is 30 symbols")
	private String country;
	
	@TextLength(max=25, message = "Minimum length is 1 symbol and max length is 25 symbols")
	private String city;
	
	@TextLength(max=30, message = "Minimum length is 1 symbol and max length is 30 symbols")
	private String street;
	
	@TextLength(max=10, message = "Minimum length is 1 symbol and max length is 10 symbols")
	private String building;
	
	public Address() {}
	
	public Address(String country, String city, String street, String building)
	{
		this.country = country;
		this.city = city;
		this.street = street;
		this.building = building;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	
	@Override
	public String toString()
	{
		return country + ", " + city + ", " + street + ", " + building;
	}

}
