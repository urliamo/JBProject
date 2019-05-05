package Logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale.Category;

import JavaBeans.Coupon;
import JavaBeans.Customer;

@Controller

public class CustomerController extends ClientController{
	

	public CustomerController() {
		super();
	}


	/**
	 * returns the DB data of this customer
	 * 
	 * @see 		DB.customerDBDAO
	 * @see			JavaBeans.Customer
	 * @return 		customer object with this customers' data
	 */
	public Customer getCustomerDetails() throws Exception{
		return  customerDBDAO.getOneCustomer(customerID);	}
}
