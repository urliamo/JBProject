package DB;

import java.util.ArrayList;

import JavaBeans.Company;
import JavaBeans.Coupon;



// DAO = Data access object
public interface ICompaniesDAO {
	

    long addCompany(Company company) throws Exception;
    ArrayList<Coupon> getCouponsByCompanyID (long CompanyID) throws Exception;

    void updateCompany(Company company) throws Exception;   
        
	ArrayList<Company> getAllCompanies() throws Exception;
    Company getCompanyByID(long companyID) throws Exception;
    boolean isCompanyExists(String email, String password) throws Exception;

    void deleteCompany(long companyID) throws Exception;

}