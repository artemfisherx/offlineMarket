package mainPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import mainPackage.AdditionalEntities.ItemStore;
import mainPackage.CustomExceptions.NotEnoughItemBalanceException;
import mainPackage.Entities.Address;
import mainPackage.Entities.Counteragent;
import mainPackage.Entities.Employee;
import mainPackage.Entities.Employee.TZone;
import mainPackage.Entities.Item;
import mainPackage.Entities.Item.ItemType;
import mainPackage.Entities.Order;
import mainPackage.Entities.Receipt;
import mainPackage.Entities.Store;
import mainPackage.Events.TransactionEventListener;
import mainPackage.Events.TransactionEventPublisher;

@Repository(value="MainRepository")
@Transactional(isolation=Isolation.REPEATABLE_READ)
public class MainRepository {
	
	JdbcTemplate jdbc;
	
	@Autowired
	TransactionEventPublisher publisher;		
	
	
	public MainRepository(DataSource source)
	{
		this.jdbc = new JdbcTemplate(source);		
	}
	
	
	// COUNTERAGENTS
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insertCounteragent(Counteragent agent)
	{
		publisher.publish(this, agent, "insertCounteragent-1");
		
		String sql = "INSERT INTO counteragents(inn, name, country, city, street, building) VALUES(?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbc.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException
			{
				PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
				ps.setLong(1, agent.getInn());
				ps.setString(2, agent.getName());
				ps.setString(3, agent.getAddress().getCountry());
				ps.setString(4, agent.getAddress().getCity());
				ps.setString(5, agent.getAddress().getStreet());
				ps.setString(6, agent.getAddress().getBuilding());
				
				return ps;
			}
			
		}, keyHolder);
		
		publisher.publish(this, agent, "insertCounteragent-2");
		
		return keyHolder.getKey().intValue();
	}
	
	@Transactional(propagation=Propagation.NESTED)
	public void updateCounteragent(Counteragent agent)
	{
		publisher.publish(this, agent, "updateCounteragent-1");
		
		String sql = "UPDATE counteragents SET inn=?, name=?, country=?, city=?, street=?, building=? WHERE id=?";
		jdbc.update(sql,
				agent.getInn(),
				agent.getName(),
				agent.getAddress().getCountry(),
				agent.getAddress().getCity(),
				agent.getAddress().getStreet(),
				agent.getAddress().getBuilding(),
				agent.getId());
		
		publisher.publish(this, agent, "updateCounteragent-2");
	}
	
	@Transactional(readOnly=true, isolation = Isolation.SERIALIZABLE)
	public Counteragent getCounteragent(int id)
	{
		String sql = "SELECT * FROM counteragents WHERE id=?";
		return jdbc.queryForObject(sql, new Object [] {id}, new int [] {Types.INTEGER}, new CounteragentRowMapper());
	}
	
	@Transactional(readOnly=true, isolation=Isolation.SERIALIZABLE)
	public List<Counteragent> getAllCounteragents()
	{
		String sql = "SELECT * FROM counteragents ORDER BY name";
		return jdbc.query(sql, new CounteragentRowMapper());
	}
	
	public void deleteCounteragent(int id)
	{
		String sql = "DELETE FROM counteragents WHERE id=?";
		jdbc.update(sql, id);
	}
	
	
	class CounteragentRowMapper implements RowMapper<Counteragent>
	{
		@Override
		public Counteragent mapRow(ResultSet res, int numRow) throws SQLException
		{
			Counteragent agent = new Counteragent();
			agent.setId(res.getInt("id"));
			agent.setInn(res.getLong("inn"));
			agent.setName(res.getString("name"));
			
			Address address = new Address();
			address.setCountry(res.getString("country"));
			address.setCity(res.getString("city"));
			address.setStreet(res.getString("street"));			
			address.setBuilding(res.getString("building"));
			
			agent.setAddress(address);			
			
			return agent;
		}
	}
	
	
	//STORES
	
	@Transactional(propagation=Propagation.NESTED)
	public int insertStore(Store store)
	{
		String sql = "INSERT INTO stores(name) VALUES (?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbc.update(new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException
					{
						PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
						ps.setString(1, store.getName());
						
						return ps;
					}
							
				}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	@Transactional(propagation = Propagation.NESTED)
	public void updateStore(Store store)
	{
		String sql = "UPDATE stores SET name=? WHERE id=?";
		jdbc.update(sql, store.getName(), store.getId());
	}
	
	public Store getStore(int id)
	{
		String sql = "SELECT * FROM stores WHERE id=?";
		return jdbc.queryForObject(sql, new StoreMapper(), id);
	}
	
	public List<Store> getAllStores()
	{
		String sql = "SELECT * FROM stores ORDER BY name";
		return jdbc.query(sql, new StoreMapper());
	}
	
	@Transactional(propagation=Propagation.NESTED)
	public void deleteStore(int id)
	{
		String sql = "DELETE FROM stores WHERE id=?";
		jdbc.update(sql, id);
	}	
	
	class StoreMapper implements RowMapper<Store>
	{
		@Override
		public Store mapRow(ResultSet res, int rowNum) throws SQLException
		{
			int id = res.getInt("id");
			String name = res.getString("name");
			
			Store store = new Store();
			store.setId(id);
			store.setName(name);
			
			return store;
		}
	}	
	
	
	//EMPLOYEES
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int insertEmployee(Employee employee)
	{
		String sql = "INSERT INTO employees(name, surname, position, tzone) VALUES (?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbc.update(new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException
					{
						PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
						ps.setString(1, employee.getName());
						ps.setString(2, employee.getPosition());
						ps.setInt(3, employee.getTzone().getOffset());
						
						return ps;
					}
							
				}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public void updateEmployee(Employee employee)
	{
		String sql = "UPDATE employees SET name=?, surname=?, position=?, tzone=? WHERE id=?";
		jdbc.update(sql, employee.getName(), employee.getSurname(), employee.getPosition(), employee.getTzone().getOffset(), employee.getId());
	}
	
	public Employee getEmployee(int id)
	{
		String sql = "SELECT * FROM employees WHERE id=?";
		return jdbc.queryForObject(sql, new EmployeeMapper(), id);
	}
	
	public List<Employee> getAllEmployees()
	{
		String sql = "SELECT * FROM employees ORDER BY name";
		return jdbc.query(sql, new EmployeeMapper());
	}
	
	public void deleteEmployee(int id)
	{
		String sql = "DELETE FROM employees WHERE id=?";
		jdbc.update(sql, id);
	}	
	
	class EmployeeMapper implements RowMapper<Employee>
	{
		@Override
		public Employee mapRow(ResultSet res, int rowNum) throws SQLException
		{
			int id = res.getInt("id");
			String name = res.getString("name");
			String surname = res.getString("surname");
			String position = res.getString("position");
			int tz = res.getInt("tzone");
			TZone tzone = TZone.Moscow;
			
			switch(tz)
			{				
				case -4: tzone = TZone.Miami;break;
				case 8: tzone = TZone.Beijing;break;
			}
			
			Employee employee = new Employee();
			employee.setId(id);
			employee.setName(name);
			employee.setSurname(surname);
			employee.setPosition(position);
			employee.setTzone(tzone);
			
			return employee;
		}
	}	
	
	//ITEMS
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int insertItem(Item item)
	{
		String sql = "INSERT INTO items (item_number, name, manufacturer, supplier, image, item_type, balance)"
				+ "VALUES(?,?,?,?,?,?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbc.update(new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException
					{
						PreparedStatement ps = con.prepareStatement(sql, new String [] {"id"});
						ps.setString(1, item.getItemNumber());
						ps.setString(2, item.getName());
						ps.setInt(3, item.getManufacturer().getId());
						ps.setInt(4, item.getSupplier().getId());
						ps.setString(5, item.getImage());
						ps.setInt(6, item.getItemType().ordinal());						
						
						return ps;
					}
			
				}, keyHolder);
		
		return keyHolder.getKey().intValue();		
	}
	
	public void updateItem(Item item)
	{
		String sql = "UPDATE items SET item_number=?, name=?, manufacturer=?, supplier=?, image=?, item_type=?, balance=? "
				+ "WHERE id=?";
		jdbc.update(sql, new PreparedStatementSetter()
				{
					@Override
					public void setValues(PreparedStatement ps) throws SQLException
					{
						ps.setString(1, item.getItemNumber());
						ps.setString(2, item.getName());
						ps.setInt(3, item.getManufacturer().getId());
						ps.setInt(4, item.getSupplier().getId());
						ps.setString(5, item.getImage());
						ps.setInt(6, item.getItemType().ordinal());						
						ps.setInt(7, item.getId());							
					}			
				});
	}
	
	public Item getItem(int id)
	{
		String sql = "SELECT * FROM items WHERE id=?";
		return jdbc.queryForObject(sql, new ItemMapper(), id);
	}
	
	public List<Item> getAllItems()
	{
		String sql = "SELECT * FROM items";
		return jdbc.query(sql, new ItemMapper());
	}
	
	public void deleteItem(int id)
	{
		String sql = "DELETE FROM items WHERE id=?";
		jdbc.update(sql, id);
	}
	
	class ItemMapper implements RowMapper<Item>
	{
		public Item mapRow(ResultSet row, int rowNum) throws SQLException
		{
			Item item = new Item();
			int id = row.getInt("id");
			String itemNumber = row.getString("item_number");
			String name = row.getString("name");
			Counteragent manufacturer = getCounteragent(row.getInt("manufacturer"));
			Counteragent supplier = getCounteragent(row.getInt("supplier"));
			
			String image = row.getString("image");
			if(image==null)
				image="";
			
			ItemType itemType = ItemType.values()[row.getInt("item_type")];			
			
			item.setId(id);
			item.setItemNumber(itemNumber);
			item.setName(name);
			item.setManufacturer(manufacturer);
			item.setSupplier(supplier);
			item.setImage(image);
			item.setItemType(itemType);
					
			return item;
		}
	}
	
	//ORDERS
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int insertOrder(Order order)
	{
		List<ItemStore> positions = order.getPositions();
					
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		String sql = "INSERT INTO orders(seller, client, datetime) VALUES(?,?,?)";
		jdbc.update(new PreparedStatementCreator() 
		{
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException
			{
				PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
				ps.setInt(1, order.getSeller().getId());
				ps.setInt(2, order.getClient().getId());
				ps.setTimestamp(3,Timestamp.from(order.getDatetime().toInstant()));
				
				return ps;
			}
		}, keyHolder);
		
		int orderId = keyHolder.getKey().intValue();
		
		for(ItemStore pos : positions)		
			insertItemStore(pos, orderId);
		
		return orderId;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateOrder(Order order)
	{
		List<ItemStore> positions = order.getPositions();
		
		HashSet<Integer> newIds = new HashSet<>();
		HashSet<Integer> oldIds = new HashSet<>();	
		
		String sql = "SELECT id FROM order_positions WHERE \"order\"=?";
		List<Integer> oIds = jdbc.queryForList(sql, new Object[] {order.getId()}, new int[] {java.sql.Types.INTEGER}, Integer.class);
				
		for(ItemStore pos : positions)
		{
			System.out.println("is:"+ pos.getId() + " " + pos.getItem().getId() + " " + pos.getCount());
			
			if(pos.getId()==0)
				insertItemStore(pos, order.getId());
			else
			{
				newIds.add(pos.getId());
				updateItemStore(pos);
			}
		}
					
		//Calculate changes
		System.out.println("newIds:" + newIds);
		
		oldIds.addAll(oIds);
		System.out.println("oldIds.addAll(oIds)" + oldIds);
		
		oldIds.removeAll(newIds);
		System.out.println("oldIds.removeAll(newIds);" + oldIds);
		
		List<ItemStore> difPositions = getListItemStore(order.getId()).stream()
				.filter(o->oldIds.contains(o.getId())).toList();
		
		difPositions.forEach(x-> System.out.println("DIF:" + x.getId() + " - " + x.getItem().getId() + " - " + x.getCount()));
					
	
		String sql3= "DELETE FROM order_positions WHERE id=?";
		for(ItemStore is:difPositions)
		{
			jdbc.update(sql3, is.getId());			
		}
		
		String sql4 = "UPDATE orders SET seller=?, client=?, datetime=? WHERE id=?";
		jdbc.update(sql4, order.getSeller().getId(), 
				order.getClient().getId(), 
				order.getDatetime().toOffsetDateTime(),
				order.getId());
		
	}
	
	public void deleteOrder(int id)
	{
		String sql = "DELETE FROM orders WHERE id=?";
		jdbc.update(sql, id);
	}
	
	public Order getOrder(int id)
	{
		String sql = "SELECT * FROM orders WHERE id=?";
		return jdbc.queryForObject(sql, new OrderRowMapper(), id);
	}
	
	public List<Order> getAllOrders()
	{
		String sql = "SELECT * FROM orders";
		return jdbc.query(sql, new OrderRowMapper());
	}
	
	
	private int insertItemStore(ItemStore is, int orderId)
	{
		String sql = "INSERT INTO order_positions(item, store, count, \"order\") VALUES(?,?,?,?)";
			
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException
					{
						PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
						ps.setInt(1, is.getItem().getId());
						ps.setInt(2, is.getStore().getId());
						ps.setInt(3, is.getCount());
						ps.setInt(4, orderId);
						
						return ps;
					}			
			
				}, keyHolder);
		
		int key;
		try 
		{
			key = keyHolder.getKey().intValue();
		}
		catch(NullPointerException ex)
		{
			throw new NotEnoughItemBalanceException(ex.getMessage(), is.getItem().getName(), is.getStore().getName());
		}
		
		return key;
	}	
	
	private void updateItemStore(ItemStore is)
	{
		String sql = "UPDATE order_positions SET item=?, store=?, \"count\"=? WHERE id=?";
		jdbc.update(sql, is.getItem().getId(), is.getStore().getId(), is.getCount(), is.getId());
	}
	
	private List<ItemStore> getListItemStore(int orderId)
	{
		String sql="SELECT id, item, store, \"count\" FROM order_positions WHERE \"order\"=?";
		return jdbc.query(sql, new ItemStoreRowMapper(), orderId);
	}
	
	private class OrderRowMapper implements RowMapper<Order>
	{
		@Override
		public Order mapRow(ResultSet result, int numRow) throws SQLException
		{
			int id = result.getInt("id");
			int sellerId = result.getInt("seller");
			int clientId = result.getInt("client");
			OffsetDateTime dtime = OffsetDateTime.ofInstant(result.getTimestamp("datetime").toInstant(), ZoneId.systemDefault());
			
			Employee seller = getEmployee(sellerId);
			Counteragent client = getCounteragent(clientId);
			List<ItemStore> positions = getListItemStore(id);
			
			Order order = new Order();
			order.setId(id);
			order.setSeller(seller);
			order.setClient(client);
			order.setPositions(positions);
			order.setDatetime(dtime.toZonedDateTime());
			
			return order;
		}
	}
	
	private class ItemStoreRowMapper implements RowMapper<ItemStore>
	{
		@Override
		public ItemStore mapRow(ResultSet set, int numRow) throws SQLException
		{
			int id = set.getInt("id");
			int itemId = set.getInt("item");
			int storeId = set.getInt("store");
			int count = set.getInt("count");
			
			Item item = getItem(itemId);
			Store store = getStore(storeId);
			
			return new ItemStore(id, item, store, count);
		}
	}
	
	//RECEIPTS
	
	@Transactional(propagation = Propagation.NESTED)
	public int insertReceipt(Receipt receipt)
	{
		String sql="INSERT INTO receipts(datetime, item, store, count, supplier) VALUES(?,?,?,?,?);";
				
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbc.update(new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException
					{
						PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
						ps.setTimestamp(1, Timestamp.from(receipt.getDatetime().toInstant()));
						ps.setInt(2, receipt.getItem().getId());
						ps.setInt(3, receipt.getStore().getId());
						ps.setInt(4, receipt.getCount());
						ps.setInt(5, receipt.getSupplier().getId());
						
						return ps;
					}
			
				}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	@Transactional(propagation=Propagation.NESTED)
	public void updateReceipt(Receipt receipt)
	{
		String sql="UPDATE receipts SET datetime=?, item=?, store=?, count=?, supplier=? WHERE id=?;";
		jdbc.update(sql,
				receipt.getDatetime().toOffsetDateTime(),
				receipt.getItem().getId(),
				receipt.getStore().getId(),
				receipt.getCount(),
				receipt.getSupplier().getId(),
				receipt.getId());
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.REPEATABLE_READ)
	public void deleteReceipt(int id)
	{
		String sql = "DELETE FROM receipts WHERE id=?";
		jdbc.update(sql, id);
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public Receipt getReceipt(int id)
	{
		String sql="SELECT * FROM receipts WHERE id=?";
		return jdbc.queryForObject(sql, new ReceiptRowMapper(), id);
	}
	
	@Transactional(isolation=Isolation.SERIALIZABLE)	
	public List<Receipt> getAllReceipts()
	{
		String sql = "SELECT * FROM receipts ORDER BY datetime DESC";
		return jdbc.query(sql, new ReceiptRowMapper());
	}
	
	class ReceiptRowMapper implements RowMapper<Receipt>
	{
		@Override 
		public Receipt mapRow(ResultSet result, int rowNum) throws SQLException
		{
			int id = result.getInt("id");
			Instant dt = result.getTimestamp("datetime").toInstant();
			ZonedDateTime datetime = OffsetDateTime.ofInstant(dt, ZoneId.systemDefault()).toZonedDateTime();
			
			int itemId = result.getInt("item");
			Item item = getItem(itemId);
			
			int storeId = result.getInt("store");
			Store store = getStore(storeId);
			
			int count = result.getInt("count");
			
			int supplierId = result.getInt("supplier");
			Counteragent supplier = getCounteragent(supplierId);
			
			Receipt receipt = new Receipt();
			receipt.setId(id);
			receipt.setDatetime(datetime);
			receipt.setItem(item);
			receipt.setStore(store);
			receipt.setCount(count);
			receipt.setSupplier(supplier);
			
			return receipt;			
		}
	}
	
	// Current balance of items
	public List<HashMap<String, String>> getItemsBalance()
	{
		String sql="select it.name as item, st.name as store, its.balance from items_stores its\r\n"
				+ "join items it on its.item=it.id\r\n"
				+ "join stores st on its.store=st.id";
		
		SqlRowSet rowSet = jdbc.queryForRowSet(sql);
		
		List<HashMap<String, String>> balance = new ArrayList<>();
		while(rowSet.next())
		{
			HashMap<String, String> map = new HashMap<>();
			map.put("item", rowSet.getString("item"));
			map.put("store", rowSet.getString("store"));
			map.put("balance", String.valueOf(rowSet.getInt("balance")));
			balance.add(map);
		}
		
		return balance;
	}
}
