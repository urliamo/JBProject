package DB;

import java.util.ArrayList;

import JavaBeans.Company;
import JavaBeans.Coupon;



// DAO = Data access object
public interface CompaniesDAO {
	

    void addCompany(Company company) throws Exception;
    ArrayList<Coupon> getCouponsByCompanyID (int CompanyID) throws Exception;

    void updateCompany(Company company) throws Exception;   
        
	ArrayList<Company> getAllCompanies() throws Exception;
    Company getOneCompany(int companyID) throws Exception;
    boolean isCompanyExists(String email, String password) throws Exception;

    void deleteCompany(int companyID) throws Exception;

}