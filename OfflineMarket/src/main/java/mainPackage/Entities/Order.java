package mainPackage.Entities;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


import mainPackage.AdditionalEntities.ItemStore;
import mainPackage.Annotations.OrderDateTime;

public class Order {
	
	private int id;
	private Employee seller;
	private Counteragent client;
	private List<ItemStore> positions = new ArrayList<ItemStore>();
	
	@OrderDateTime
	private ZonedDateTime datetime = ZonedDateTime.now();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Employee getSeller() {
		return seller;
	}
	public void setSeller(Employee seller) {
		this.seller = seller;
	}
	public Counteragent getClient() {
		return client;
	}
	public void setClient(Counteragent client) {
		this.client = client;
	}
	public List<ItemStore> getPositions() {
		return positions;
	}
	public void setPositions(List<ItemStore> positions) {
		this.positions = positions;
	}
	public ZonedDateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(ZonedDateTime datetime) {
		this.datetime = datetime;
	}
	

}
