package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DBCreator  {
private static String USERNAME = "root";
private static String PASSWORD = "123456";
private static String DRIVER = "com.mysql.jdbc.Driver";
private static String URL = "jdbc:mysql://localhost:3306/";



public static void buildDB() {
	
	Connection connection =null;
	Statement statement = null;
	
	try {
		Class.forName(DRIVER);
		// Create a connection to the database: 
		connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
		
		
		//Create SQL statement
		statement = connection.createStatement();	
		
		//Create JB DB if it does not exist
		String sql = "show databases like 'JB'";
		ResultSet isJBDB = statement.executeQuery(sql);
		if (!isJBDB.next()) {
				 sql = "CREATE DATABASE JB";
				statement.executeUpdate(sql);
				System.out.println(" JB Database created successfully...");
				
				}
		else
		{
			System.out.println(" JB Database already exists");
		}
				
		statement.close();
		connection.close();
		
		connection = DriverManager.getConnection(URL+"JB",USERNAME,PASSWORD);
		statement = connection.createStatement();	

				
		// Create string for statement including companies table creation
		 sql = "CREATE TABLE companies (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL)";
		
		
		
		//Execute create companies table statement
		
		statement.executeUpdate(sql);
		
		System.out.println("companies table has been created.");	
		
		// Change string for statement to include customers table creation
				
		sql = "CREATE TABLE customers (" +
					"id INT PRIMARY KEY AUTO_INCREMENT, " +
					"first_name VARCHAR(50) NOT NULL, " +
					"last_name VARCHAR(50) NOT NULL, " +
					"password VARCHAR(50) NOT NULL, " +
					"email VARCHAR(50) NOT NULL)";
				
		//Execute create customers table statement

		statement.executeUpdate(sql);  
				
		System.out.println("customers table has been created.");
		

		// Change string for statement to include categories table creation
		
		sql = "CREATE TABLE categories (" +
					"id INT PRIMARY KEY AUTO_INCREMENT, " +
					"name VARCHAR(50) NOT NULL)";
				
		//Execute create categories table statement

		statement.executeUpdate(sql);
		System.out.println("categories table has been created.");

		//change string for statement to include categories to be added
		sql = "insert into categories(name) Values";
		
		//add each category under Categories class to the string
		for (JavaBeans.Categories c : JavaBeans.Categories.values())
		{
			sql+="('"+c.toString()+"'),";
		}
		
		//remove final "," in string
		sql = sql.substring(0, sql.length()-1);
		
		//execute update Categories table statement
		statement.executeUpdate(sql);
		System.out.println("categories table has been updated to include categories.");

		
		// Change string for statement to include coupons table creation
		
				sql = "CREATE TABLE coupons (" +
							"id INT PRIMARY KEY AUTO_INCREMENT, " +
							"company_id INT NOT NULL, " +
							"category_id INT NOT NULL, " +
							"title VARCHAR(50) NOT NULL, " +
							"description VARCHAR(100) NOT NULL, " +
							"image VARCHAR(50) NOT NULL, " +
							"amount INT NOT NULL, " +
							"price DOUBLE NOT NULL, " +
							"start_date DATE NOT NULL, " +
							"end_date DATE NOT NULL, " +
							"FOREIGN KEY (company_id) REFERENCES companies(id)," +
							"FOREIGN KEY (category_id) REFERENCES categories(id))";
						
				//Execute create coupons table statement

				statement.executeUpdate(sql);
						
				System.out.println("coupons table has been created.");

				
					// Change string for statement to include customers_vs_coupons table creation
				
				sql = "CREATE TABLE jb.customers_vs_coupons (customer_id INT , coupon_id INT , PRIMARY KEY (customer_id, coupon_id), FOREIGN KEY (customer_id) REFERENCES customers(id), FOREIGN KEY (coupon_id) REFERENCES coupons(id))";
						
				//Execute create customers_vs_coupons table statement

				statement.executeUpdate(sql);
						
				System.out.println("customers_vs_coupons table has been created.");
	}
	catch(Exception ex) {
		 System.out.println(ex.getMessage());
	}
	finally{
		//close all connections
		if(connection!=null)
			try {
				statement.close();
				connection.close();
			} catch (SQLException ex) {
				 System.out.println(ex.getMessage());
			}
	}
}

}