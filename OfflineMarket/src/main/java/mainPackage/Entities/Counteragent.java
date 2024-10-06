package mainPackage.Entities;

import jakarta.validation.Valid;
import mainPackage.Validation.Inn;
import mainPackage.Validation.TextLength;

public class Counteragent {
	
	private int id = 0;
	
	@Inn
	private long inn = 0L;
	
	@TextLength(max=100, message = "Minimum length is 1 symbol and max length is 100 symbols")
	private String name = "";	
	
	@Valid
	private Address address = new Address("","","","");
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public long getInn() {
		return inn;
	}
	public void setInn(long inn) {
		this.inn = inn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(id);
	}
	
}
