package Logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale.Category;

import Enums.Categories;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.Coupon;
import JavaBeans.Customer;
import JavaBeans.Purchase;

@Controller

public class PurchasesController extends ClientController{
	
	public PurchasesController() {
		super();
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
	
public void purchaseCoupon(long couponID, long customerID, int amount) {
	try
	{
		if (customerDBDAO.getOneCustomer(customerID)==null) {
			throw new ApplicationException(ErrorType.CUSTOMER_ID_DOES_NOT_EXIST, ErrorType.CUSTOMER_ID_DOES_NOT_EXIST.getInternalMessage());

		}
		if (!couponsDBDAO.isCouponExists(couponID)) {
			throw new ApplicationException(ErrorType.COUPON_ID_DOES_NOT_EXIST, ErrorType.COUPON_ID_DOES_NOT_EXIST.getInternalMessage());

		}
		
		Coupon couponDB = couponsDBDAO.getOneCoupon(couponID);
		if (couponDB.getAmount()<1)
			throw new Exception("coupon out of stock");
		if (LocalDate.now().isAfter(couponDB.getEnd_date()))
			throw new Exception("Coupon out of date");
		
		purchasesDBDAO.addCouponPurchase(couponID, customerID, amount);
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
 * @see			DB.CustomerDAO
 * @return 		ArrayList of coupons belonging to this customer	
 * @throws 		customer has no coupons
 */
	public Collection<Coupon> getCustomerCoupons(long customerID) {
		ArrayList<Coupon> customerCoupons = new ArrayList<Coupon>();

		try {
			if (customerDBDAO.getOneCustomer(customerID)==null) {
				throw new ApplicationException(ErrorType.CUSTOMER_ID_DOES_NOT_EXIST, ErrorType.CUSTOMER_ID_DOES_NOT_EXIST.getInternalMessage());

			}
			ArrayList<Purchase> customerPurchases = (ArrayList<Purchase>) purchasesDBDAO.getAllPurchasesbyCustomer(customerID);
			if (customerPurchases.size()==0)
				throw new Exception("Customer has no coupons");
			for (Purchase purchase : customerPurchases) {
				customerCoupons.add(couponsDBDAO.getOneCoupon(purchase.getCouponID()));
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
	
	public Collection<Coupon> getCustomerCoupons(Categories category, long customerID) {
		ArrayList<Coupon> customerCoupons = new ArrayList<Coupon>();

		try {
			if (customerDBDAO.getOneCustomer(customerID)==null) {
				throw new ApplicationException(ErrorType.CUSTOMER_ID_DOES_NOT_EXIST, ErrorType.CUSTOMER_ID_DOES_NOT_EXIST.getInternalMessage());

			}
			ArrayList<Purchase> customerPurchases = (ArrayList<Purchase>) purchasesDBDAO.getAllPurchasesbyCustomer(customerID);
			if (customerPurchases.size()==0)
				throw new Exception("Customer has no coupons");
			for (Purchase purchase : customerPurchases) {
				customerCoupons.add(couponsDBDAO.getOneCoupon(purchase.getCouponID()));
			}
			for (Coupon c : customerCoupons) {
				if (c.getCategory()!=category)
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
	public Collection<Coupon> getCustomerCoupons(double maxPrice) {
		ArrayList<Coupon> customerCoupons = new ArrayList<Coupon>();

		try {
			if (customerDBDAO.getOneCustomer(customerID)==null) {
				throw new ApplicationException(ErrorType.CUSTOMER_ID_DOES_NOT_EXIST, ErrorType.CUSTOMER_ID_DOES_NOT_EXIST.getInternalMessage());

			}
			ArrayList<Purchase> customerPurchases = (ArrayList<Purchase>) purchasesDBDAO.getAllPurchasesbyCustomer(customerID);
			if (customerPurchases.size()==0)
				throw new Exception("Customer has no coupons");
			for (Purchase purchase : customerPurchases) {
				customerCoupons.add(couponsDBDAO.getOneCoupon(purchase.getCouponID()));
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

