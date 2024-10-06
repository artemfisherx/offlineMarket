package mainPackage.Entities;

public class Item {
	
	private int id;
	private String itemNumber;
	private String name;
	private Counteragent manufacturer;
	private Counteragent supplier;
	private String image="";
	private ItemType itemType=ItemType.Wallpaper;
	
	public enum ItemType
	{
		Wallpaper, Glue, Tool
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Counteragent getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(Counteragent manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Counteragent getSupplier() {
		return supplier;
	}
	public void setSupplier(Counteragent supplier) {
		this.supplier = supplier;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public ItemType getItemType() {
		return itemType;
	}
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}	
	
	@Override
	public String toString()
	{
		return String.valueOf(this.id);
	}
}
