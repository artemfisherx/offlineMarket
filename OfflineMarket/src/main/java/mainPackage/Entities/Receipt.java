package mainPackage.Entities;

import java.time.ZonedDateTime;

import mainPackage.Annotations.OrderDateTime;

public class Receipt {
	
	private int id;	
	@OrderDateTime
	private ZonedDateTime datetime;
	private Item item;
	private Store store;
	private int count;
	private Counteragent supplier;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Counteragent getSupplier() {
		return supplier;
	}
	public void setSupplier(Counteragent agent) {
		this.supplier = agent;
	}
	public ZonedDateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(ZonedDateTime datetime) {
		this.datetime = datetime;
	}

}
