package Clients;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale.Category;

import JavaBeans.Company;
import JavaBeans.Coupon;


/**
 * object returned when user logs in as Company. in charge of login and DBDAO actions for companies. 
 *
 * @param  companyID int containing the unique ID of the current company using this object instance.
 * @see         JavaBeans.Company
 * @see 		JavaBeans.Coupon
 * @see			Clients.LoginManager
 */
public class CompanyFacade extends ClientFacade {

	private int companyID;
	
	
	/**
	 * compares input mail and pass to companies DB and set the ID of the current instance to the returned companyID
	 *
	 * @param  email mail used to login
	 * @param password password used to login
	 * @see 		companiesDBDAO
	 * @throws		wrong mail/password!
	 */
	
	public void login(String email, String password){
		try {
			//check company mail\pass combination exists
			if (!companiesDBDAO.isCompanyExists(email, password)) {
				throw new Exception("wrong mail/password");
			}
			//return the ID for the logged-in company
			this.setCompanyID(companiesDBDAO.getCompanyID(email, password));
			
			}
			catch(Exception Ex){
				 System.out.println(Ex.getMessage());

			}
	
	}


	public void setCompanyID(long l) {
		this.companyID = l;
	}

	public CompanyFacade() {
		super();
	}

	/**
	 * adds a new coupon to the DB.
	 * <p> 
	 * this also checks for coupon validity of date or duplication.
	 *
	 * @param  coupon the new coupon to be added to the DB
	 * @exception coupon already exists
	 * @exception coupon starts after it expired
	 * @exception coupon already expired
	 * @see 		couponsDBDAO
	 * @see			JavaBeans.Coupon
	 */
	
	public void addCoupon(Coupon coupon) {
		try {
			
			//get all company coupons
			ArrayList<Coupon> coupons = companiesDBDAO.getCouponsByCompanyID(companyID);
			for (Coupon c : coupons) {
				//check if new coupon already exists for this company
				if (c.getTitle() == coupon.getTitle())
						throw new Exception("Coupon with this title already exists for this company");
			}
			//check coupon expiration date vs. start date
			if (coupon.getStart_date().isAfter(coupon.getEnd_date()))
			throw new Exception("This Coupon starts after it ends");
			
			if (LocalDate.now().isAfter(coupon.getEnd_date()))
				throw new Exception("This Coupon already expired");
		
			//add coupon
		couponsDBDAO.addCoupon(coupon);
		System.out.println("coupon added");
		}
		catch(Exception Ex){
			 System.out.println(Ex.getMessage());

		}
	}
	
	/**
	 * updates an existing coupon in the DB.
	 *
	 * @param  coupon the  coupon to be added updated in the DB
	 * @exception coupon does not exist
	 * @see 		couponsDBDAO
	 * @see			JavaBeans.Coupon
	 */
	public void updateCoupon(Coupon coupon){
		try {
			//check coupon with this id exists
			if (!couponsDBDAO.isCouponExists(coupon.getId()))
				throw new Exception("coupon with this ID does not exist!");
				//update coupon
		couponsDBDAO.updateCoupon(coupon);
	}
		catch(Exception Ex){
			 System.out.println(Ex.getMessage());

		}
	}
	
	/**
	 * removes a coupon from the DB.
	 * <p>
	 * this also removes any coupons purchased by customers
	 * 
	 * 
	 * @param  coupon the coupon to be removed from the DB
	 * @exception coupon does not exist!
	 * @see 		couponsDBDAO
	 * @see			JavaBeans.Coupon
	 */
	public void deleteCoupon(Coupon coupon) {
		try {
			//check if coupon actually exists
			if (!couponsDBDAO.isCouponExists(coupon.getId()))
				throw new Exception("coupon with this ID does not exist!");
			
		//delete coupon customer purchases
		couponsDBDAO.deleteCouponPurchase(coupon.getId(), -1);	
		//delete company coupon
		couponsDBDAO.deleteCoupon(coupon.getId());
		
	}
		catch(Exception Ex){
			 System.out.println(Ex.getMessage());

		}
	}
	/**
	 * returns all coupons belonging to this company.
	 * 
	 * @param  coupon the new coupon to be added to the DB
	 * @see 		companiesDBDAO
	 * @return ArrayList of coupon objects belonging to this company
	 */
	public ArrayList<Coupon> getCompanyCoupons() throws Exception{
		return companiesDBDAO.getCouponsByCompanyID(companyID);
	}
	
	/**
	 * returns a list of all company coupons of a specified category
	 * 
	 * @param  Category the category of coupons to be returnes
	 * @see 		companiesDBDAO
	 * @see			JavaBeans.Coupon
	 * @see			JavaBeans.Category
	 * @return 		ArrayList of coupons
	 */
	public ArrayList<Coupon> getCompanyCoupons(Category category) throws Exception
	{
		//get list of all company coupons
		ArrayList<Coupon> coupons = companiesDBDAO.getCouponsByCompanyID(companyID);

		//remove coupons with different category from list
		for (Coupon c : coupons) {
			if (couponsDBDAO.getCouponCategory(c.getCategory_id())!=category)
				coupons.remove(c);
		}
		return coupons;

	}
	/**
	 * returns a list of all company coupons of a specified max price
	 * 
	 * @param  maxprice the highest price of the returned coupons
	 * @see 		companiesDBDAO
	 * @see			JavaBeans.Coupon
	 * @return ArrayList of coupons
	 */
	public ArrayList<Coupon> getCompanyCoupons(double maxprice) throws Exception{
		ArrayList<Coupon> coupons = companiesDBDAO.getCouponsByCompanyID(companyID);

		//remove coupons with higher price from returned list
		for (Coupon c : coupons) {
			if (c.getPrice()>maxprice)
				coupons.remove(c);
		}
		return coupons;
	}
	/**
	 * returns the DB data of the current company as a company object.
	 * 
	 * @see 		companiesDBDAO
	 * @see			JavaBeans.Company
	 * @return		Company object with this company data.
	 */
	//return the logged-in company
	public Company getCompany() throws Exception{
		return companiesDBDAO.getCompanyByID(companyID);
	}
}
