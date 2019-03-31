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
		
		// Create string for statement including users table creation
		 sql = "CREATE TABLE users ("+
		"ID bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,"+
		" password VARCHAR(50) NOT NULL,"+
		" email VARCHAR(50) NOT NULL,"+
		" companyID VARCHAR(50),"+
		" Type VARCHAR(50) NOT NULL,"+
		"FOREIGN KEY (companyID) REFERENCES companies(companyID))";
		
		
		
		//Execute create companies table statement
		
		statement.executeUpdate(sql);
		
		System.out.println("companies table has been created.");	
				
		// Create string for statement including companies table creation
		 sql = "CREATE TABLE companies ("+
		"companyID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,"+
		"name VARCHAR(50) NOT NULL,"+
		"email VARCHAR(50) NOT NULL,";
		
		
		
		//Execute create companies table statement
		
		statement.executeUpdate(sql);
		
		System.out.println("companies table has been created.");	
		
		// Change string for statement to include customers table creation
				
		sql = "CREATE TABLE customers (" +
					"customerID BIGINT PRIMARY KEY, " +
					"first_name VARCHAR(50) NOT NULL, " +
					"last_name VARCHAR(50) NOT NULL, " +
					"FOREIGN KEY (customerID) REFERENCES users(ID))";
				
		//Execute create customers table statement

		statement.executeUpdate(sql);  
				
		System.out.println("customers table has been created.");
		

		// Change string for statement to include categories table creation
		
		sql = "CREATE TABLE categories (" +
					"categoryID INT PRIMARY KEY AUTO_INCREMENT, " +
					"name VARCHAR(50) NOT NULL)";
				
		//Execute create categories table statement

		statement.executeUpdate(sql);
		System.out.println("categories table has been created.");

		//change string for statement to include categories to be added
		sql = "insert into categories(name) Values";
		
		//add each category under Categories class to the string
		for (Enums.Categories c : Enums.Categories.values())
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
							"couponID BIGINT PRIMARY KEY AUTO_INCREMENT, " +
							"companyID BIGINT NOT NULL, " +
							"categoryID BIGINT NOT NULL, " +
							"title VARCHAR(50) NOT NULL, " +
							"description VARCHAR(100) NOT NULL, " +
							"image VARCHAR(50) NOT NULL, " +
							"amount INT NOT NULL, " +
							"price DOUBLE NOT NULL, " +
							"start_date DATE NOT NULL, " +
							"end_date DATE NOT NULL, " +
							"FOREIGN KEY (companyID) REFERENCES companies(companyID)," +
							"FOREIGN KEY (categoryID) REFERENCES categories(categoryID))";
						
				//Execute create coupons table statement

				statement.executeUpdate(sql);
						
				System.out.println("coupons table has been created.");

				
					// Change string for statement to include customers_vs_coupons table creation
				sql = "CREATE TABLE jb.purchases ("+
					"PurchaseID BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
					"customerID BIGINT NOT NULL,"+
					"couponID BIGINT NOT NULL,"+
					"amount INT NOT NULL,"+
					"PRIMARY KEY (PurchaseID),"+
					"FOREIGN KEY (customerID) REFERENCES customers(customerID),"+
					"FOREIGN KEY (couponID) REFERENCES coupons(couponID))";
			

				//--old customer VS coupons--
				//sql = "CREATE TABLE jb.customers_vs_coupons (customerID INT , couponID INT , PRIMARY KEY (customerID, couponID), FOREIGN KEY (customerID) REFERENCES customers(customerID), FOREIGN KEY (couponID) REFERENCES coupons(couponID))";
						
				//Execute create purchases table statement

				statement.executeUpdate(sql);
						
				System.out.println("purchases table has been created.");
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