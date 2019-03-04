package Clients;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale.Category;

import JavaBeans.Coupon;
import JavaBeans.Customer;
/**
 * object returned when user logs in as customer. in charge of login and DBDAO actions for customers. 
 *
 * @param  customerID int containing the unique ID of the current customer using this object instance.
 * @see         JavaBeans.Customer
 * @see 		JavaBeans.Coupon
 */
public class CustomerFacade extends ClientFacade{
	
	private int customerID;

	/**
	 * compares input mail and pass to customers DB and sets the ID of the current instance to the returned customerID
	 *
	 * @param  email mail used to login
	 * @param password password used to login
	 * @see 		DB.customerDBDAO
	 * @throws 		wrong mail/password!
	 */
	public void login(String email, String password){
		try {
			if (!customerDBDAO.isCustomerExists(email, password)) {
				throw new Exception("wrong mail/password");
			}
			this.setCustomerID(customerDBDAO.getCustomerID(email, password));
			}
			catch(Exception Ex){
				 System.out.println(Ex.getMessage());

			}
	
	}
	
	
	public CustomerFacade() {
		super();
	}


	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	/**
	 * adds a new coupon puchase to the customer
	 * <p>
	 * this also checks the coupon is available, not out of date and is not already owned by the customer.
	 *
	 * @param coupon the coupon to be purchased by the customer
	 * @see 		DB.couponsDBDAO
	 * @throws		coupon out of stock!
	 * @throws		coupon out of date!
	 * @throws 		coupon already owned by customer!
	 */
public void purchaseCoupon(Coupon coupon) {
	try
	{
		if (couponsDBDAO.isCouponPurchaseExists(customerID, coupon.getId()))
			throw new Exception("customer already has this coupon purchased");
		Coupon couponDB = couponsDBDAO.getOneCoupon(coupon.getId());
		if (couponDB.getAmount()<1)
			throw new Exception("coupon out of stock");
		if (LocalDate.now().isAfter(coupon.getEnd_date()))
			throw new Exception("Coupon out of date");
		
		couponsDBDAO.addCouponPurchase(coupon.getId(), customerID);
		couponDB.setAmount(couponDB.getAmount()-1);
		couponsDBDAO.updateCoupon(couponDB);

	}
	catch(Exception Ex){
		 System.out.println(Ex.getMessage());

	}
}


/**
 * returns a list of all customer coupons 
 * @see 		DB.CouponDBDAO
 * @see			JavaBeans.Coupon
 * @see			DB.CustomerDBDAO
 * @return 		ArrayList of coupons belonging to this customer	
 * @throws 		customer has no coupons
 */
	public ArrayList<Coupon> getCustomerCoupons() {
		ArrayList<Coupon> customerCoupons = new ArrayList<Coupon>();

		try {
			ArrayList<Integer> customerCouponsID = customerDBDAO.getAllCouponsByCustomer(customerID);
			if (customerCouponsID.size()==0)
				throw new Exception("Customer has no coupons");
			for (int id : customerCouponsID) {
				customerCoupons.add(couponsDBDAO.getOneCoupon(id));
			}
		}
		catch(Exception Ex){
			 System.out.println(Ex.getMessage());

		}
		return customerCoupons;

	}
	/**
	 * returns a list of all customer coupons of a specified category
	 * 
	 * @param  Category the category of coupons to be returnes
	 * @see 		DB.customerDBDAO
	 * @see			JavaBeans.Coupon
	 * @see			JavaBeans.Category
	 * @return 		ArrayList of coupons belonging to this customer of specified category
	 * @throws 		customer has no coupons
	 */
	
	public ArrayList<Coupon> getCustomerCoupons(Category category) {
		ArrayList<Coupon> customerCoupons = new ArrayList<Coupon>();

		try {
			ArrayList<Integer> customerCouponsID = customerDBDAO.getAllCouponsByCustomer(customerID);
			if (customerCouponsID.size()==0)
				throw new Exception("Customer has no coupons");
			for (int id : customerCouponsID) {
				customerCoupons.add(couponsDBDAO.getOneCoupon(id));
			}
			for (Coupon c : customerCoupons) {
				if (couponsDBDAO.getCouponCategory(c.getCategory_id())!=category)
					customerCoupons.remove(c);
			}
		}
		catch(Exception Ex){
			 System.out.println(Ex.getMessage());

		}
		return customerCoupons;

	}
	
	/**
	 * returns a list of all customer coupons lower than input price
	 * 
	 * @param  maxprice the maximum price of returned coupons
	 * @see 		DB.customerDBDAO
	 * @see			JavaBeans.Coupon
	 * @see			JavaBeans.Category
	 * @return 		ArrayList of coupons belonging to this customer of limited price
	 * @throws 		customer has no coupons
	 */
	public ArrayList<Coupon> getCustomerCoupons(double maxPrice) {
		ArrayList<Coupon> customerCoupons = new ArrayList<Coupon>();

		try {
			ArrayList<Integer> customerCouponsID = customerDBDAO.getAllCouponsByCustomer(customerID);
			if (customerCouponsID.size()==0)
				throw new Exception("Customer has no coupons");
			for (int id : customerCouponsID) {
				customerCoupons.add(couponsDBDAO.getOneCoupon(id));
			}
			for (Coupon c : customerCoupons) {
				if (c.getPrice()>maxPrice)
					customerCoupons.remove(c);
			}
		}
		catch(Exception Ex){
			 System.out.println(Ex.getMessage());

		}
		return customerCoupons;

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
