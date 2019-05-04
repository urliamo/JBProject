package DB;

import java.util.ArrayList;
import java.util.Collection;

import JavaBeans.Customer;

public interface ICustomerDAO {

    void addCustomer(Customer Customer) throws Exception;
    void updateCustomer(Customer Customer) throws Exception;   
	Collection<Customer> getAllCustomers() throws Exception;
    Customer getOneCustomer(long CustomerID) throws Exception;
    //boolean isCustomerExists(String email, String password) throws Exception;
    void deleteCustomer(long CustomerID) throws Exception;
    
}
