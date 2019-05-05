package Logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale.Category;

import Enums.Categories;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.Company;
import JavaBeans.Coupon;


/**
 * object returned when user logs in as Company. in charge of login and DBDAO actions for companies. 
 *
 * @param  companyID int containing the unique ID of the current company using this object instance.
 * @see         JavaBeans.Company
 * @see 		JavaBeans.Coupon
 * @see			Logic.LoginManager
 */
@Controller

public class CouponController extends ClientController {

	@Autowired
	private DB.UsersDAO usersDao;
	
	@Autowired
	private DB.CompaniesDAO companiesDAO;
	@Autowired
	private DB.PurchasesDAO purchasesDAO;
	@Autowired
	private DB.CouponsDAO couponsDAO;


	public CouponController() {
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
	
	public void addCoupon(Coupon coupon) throws ApplicationException{
		
			

			
				if (couponsDBDAO.getCompanyCouponsByTitle(coupon.getCompany_id(), coupon.getTitle()) != null) {
					throw new ApplicationException(ErrorType.EXISTING_COUPON_TITLE, ErrorType.EXISTING_COUPON_TITLE.getInternalMessage());
				}
			//check coupon expiration date vs. start date
			if (coupon.getStart_date().isAfter(coupon.getEnd_date())) {
				throw new ApplicationException(ErrorType.COUPON_DATE_MISMATCH, ErrorType.COUPON_DATE_MISMATCH.getInternalMessage());
			}
			if (LocalDate.now().isAfter(coupon.getEnd_date())) {
				throw new ApplicationException(ErrorType.COUPON_ALREADY_EXPIRED, ErrorType.COUPON_ALREADY_EXPIRED.getInternalMessage());
			}
			//add coupon
		couponsDBDAO.addCoupon(coupon);
		
		
	}
	
	/**
	 * updates an existing coupon in the DB.
	 *
	 * @param  coupon the  coupon to be added updated in the DB
	 * @exception coupon does not exist
	 * @see 		couponsDBDAO
	 * @see			JavaBeans.Coupon
	 */
	public void updateCoupon(Coupon coupon) throws ApplicationException{
		
			//check coupon with this id exists
			if (!couponsDBDAO.isCouponExists(coupon.getId()))
				throw new ApplicationException(ErrorType.COUPON_ID_DOES_NOT_EXIST, ErrorType.COUPON_ID_DOES_NOT_EXIST.getInternalMessage());
				//update coupon
		couponsDBDAO.updateCoupon(coupon);
	
		
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
	public void deleteCoupon(Coupon coupon) throws ApplicationException {
		
			//check if coupon actually exists
			if (!couponsDBDAO.isCouponExists(coupon.getId())) {
				throw new ApplicationException(ErrorType.COUPON_ID_DOES_NOT_EXIST, ErrorType.COUPON_ID_DOES_NOT_EXIST.getInternalMessage());
			
			
		//delete coupon customer purchases
		purchasesDBDAO.deletePurchaseBycouponId(coupon.getId());
		//delete company coupon
		couponsDBDAO.deleteCoupon(coupon.getId());
		
	
	}
	}
	/**
	 * returns all coupons belonging to this company.
	 * 
	 * @param  coupon the new coupon to be added to the DB
	 * @see 		companiesDBDAO
	 * @return ArrayList of coupon objects belonging to this company
	 */
	public Collection<Coupon> getCompanyCoupons(long companyID) throws ApplicationException{
		if (companiesDBDAO.getCompanyByID(companyID)==null) {
			throw new ApplicationException(ErrorType.COMPANY_ID_DOES_NOT_EXIST, ErrorType.COMPANY_ID_DOES_NOT_EXIST.getInternalMessage());
		}
		return couponsDBDAO.getCompanyCoupons(companyID);
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
	public Collection<Coupon> getCompanyCoupons(long companyID, Categories category) throws ApplicationException
	{
		//get list of all company coupons
		if (companiesDBDAO.getCompanyByID(companyID)==null) {
			throw new ApplicationException(ErrorType.COMPANY_ID_DOES_NOT_EXIST, ErrorType.COMPANY_ID_DOES_NOT_EXIST.getInternalMessage());
		}
		Collection<Coupon> coupons = couponsDBDAO.getCompanyCoupons(companyID);

		//remove coupons with different category from list
		for (Coupon c : coupons) {
			if (c.getCategory()!=category)
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
	public Collection<Coupon> getCompanyCoupons(long companyID, double maxprice) throws ApplicationException{
		if (companiesDBDAO.getCompanyByID(companyID)==null) {
			throw new ApplicationException(ErrorType.COMPANY_ID_DOES_NOT_EXIST, ErrorType.COMPANY_ID_DOES_NOT_EXIST.getInternalMessage());
		}
		Collection<Coupon> coupons = couponsDBDAO.getCompanyCoupons(companyID);

		//remove coupons with higher price from returned list
		for (Coupon c : coupons) {
			if (c.getPrice()>maxprice)
				coupons.remove(c);
		}
		return coupons;
	}

	
}
