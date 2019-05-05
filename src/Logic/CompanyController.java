package Logic;

import java.util.Collection;

import org.springframework.stereotype.Controller;

import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.Company;
import Utils.EmailUtils;
import Utils.NameUtils;


/**
 * object returned when user logs in as admin. in charge of login and DBDAO actions for admins. 
 *
 * @see         JavaBeans.Company
 * @see 		JavaBeans.Customer
 */

@Controller
public class CompanyController extends ClientController{

	
	/**
	 *adds a new company to the DB using the DBDAO.
	 *
	 * @param  company the new company to be added to the DB.
	 * @see 		DB.companiesDBDAO
	 * @see 		JavaBeans.Company
	 * @throws company already exists!
	 */
	public long addCompany(JavaBeans.Company company) throws ApplicationException{
		if (company == null) 
		{
			throw new ApplicationException(ErrorType.EMPTY, ErrorType.EMPTY.getInternalMessage());
		}

		NameUtils.isValidName(company.getName());
		EmailUtils.isValidEmail(company.getEmail());

		if (company.getCompanyID() != 0) {
			throw new ApplicationException(ErrorType.COMPANY_ID_MUST_BE_ASSIGNED, ErrorType.COMPANY_ID_MUST_BE_ASSIGNED.getInternalMessage());
		}
		
		if (!companiesDBDAO.isCompanyExistsByMailOrName(company.getEmail(), company.getName())) { 
			throw new ApplicationException(ErrorType.NAME_IS_ALREADY_EXISTS, ErrorType.NAME_IS_ALREADY_EXISTS.getInternalMessage());
		}
			return companiesDBDAO.addCompany(company);
		
	}
	
	/**
	 *updates an existing company in the DB using the DBDAO.
	 *
	 * @param  company the company to be updates
	 * @see 		companiesDBDAO
	 * @see 		JavaBeans.Company
	 * @throws 		company does not exist!
	 * @throws 		company name cannot be updated!
	 */
	public void updateCompany(JavaBeans.Company company) throws ApplicationException{
		
				if (!companiesDBDAO.isCompanyExists(company.getEmail(), company.getName())) {
					throw new ApplicationException(ErrorType.COMPANY_ID_DOES_NOT_EXIST, ErrorType.COMPANY_ID_DOES_NOT_EXIST.getInternalMessage());
				}
				
			if (companiesDBDAO.getCompanyByID(company.getCompanyID()).getName()!= company.getName()) {
				throw new ApplicationException(ErrorType.NAME_IS_IRREPLACEABLE, ErrorType.NAME_IS_IRREPLACEABLE.getInternalMessage());
			}
			else {
				companiesDBDAO.updateCompany(company);
			}
			
	
	}
	
	/**
	 *removes an existing company from the DB using the DBDAO.
	 *<P>
	 *this also removes any coupons belonging to the company.
	 *
	 * @param  company the company to be removed
	 * @see 		companiesDBDAO
	 * @see 		JavaBeans.Company
	 * @throws 		company does not exist!
	 */
		public void deleteCompany(JavaBeans.Company company) throws ApplicationException{
			try {
				if (!companiesDBDAO.isCompanyExists(company.getEmail(), company.getName())) {
					throw new ApplicationException(ErrorType.COMPANY_ID_DOES_NOT_EXIST, ErrorType.COMPANY_ID_DOES_NOT_EXIST.getInternalMessage());
				}
				//remove company from DB
				companiesDBDAO.deleteCompany(company.getCompanyID());
				
				//find company coupons
				Collection<JavaBeans.Coupon> coupons = couponsDBDAO.getCompanyCoupons(company.getCompanyID());
				
				//remove all company coupons and customer purchases
				for (JavaBeans.Coupon c : coupons) {
					couponsDBDAO.deleteCoupon(c.getId());
					purchasesDBDAO.deletePurchaseBycouponId(c.getId());
				}
				
				
				
			}
			catch(Exception Ex) {
				 System.out.println(Ex.getMessage());

			}
		}
		
		/**
		 *	returns an ArrayList of Company objects with all companies using the DBDAO.
		 *
		 * @see 		companiesDBDAO
		 * @see 		JavaBeans.Company
		 * @return		ArrayList of all companies
		 */
		public Collection<Company> getAllCompanies() throws Exception{
			return companiesDBDAO.getAllCompanies();
		}
		
		/**
		 *	returns a company of the specified ID
		 *
		 * @param		companyID long containing the ID of the company to be returned
		 * @see 		companiesDBDAO
		 * @see 		JavaBeans.Company
		 * @return		Company object with the company data of the specified ID.
		 */
		public Company getCompany(long id) throws Exception{
			return companiesDBDAO.getCompanyByID(id);
		}

		
 	}

