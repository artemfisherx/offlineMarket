package mainPackage.AdditionalEntities;

import mainPackage.Entities.Item;
import mainPackage.Entities.Store;

public class ItemStore {
	
	private int id;
	private Item item;
	private Store store;
	private int count;
	
	public ItemStore(int id, Item item, Store store, int count)
	{
		this.id = id;
		this.item = item;
		this.store = store;
		this.count = count;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
