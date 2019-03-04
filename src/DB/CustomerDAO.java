package DB;

import java.util.ArrayList;

import JavaBeans.Customer;

public interface CustomerDAO {

    void addCustomer(Customer Customer) throws Exception;
    void updateCustomer(Customer Customer) throws Exception;   
	ArrayList<Customer> getAllCustomers() throws Exception;
    Customer getOneCustomer(int CustomerID) throws Exception;
    boolean isCustomerExists(String email, String password) throws Exception;
    void deleteCustomer(int CustomerID) throws Exception;
    
}
