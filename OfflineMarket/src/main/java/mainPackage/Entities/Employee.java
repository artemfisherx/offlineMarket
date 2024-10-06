package mainPackage.Entities;

public class Employee {
	
	private int id;
	private String name;
	private String surname;
	private String position;
	private TZone tzone;
	
	public enum TZone
	{		
		Moscow(4), Miami(-4), Beijing(8);
		
		private int offset;
		
		private TZone(int offset)
		{
			this.offset = offset;
		}
		
		public int getOffset()
		{
			return this.offset;
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public TZone getTzone() {
		return tzone;
	}
	public void setTzone(TZone tzone) {
		this.tzone = tzone;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(this.id);
	}

}
