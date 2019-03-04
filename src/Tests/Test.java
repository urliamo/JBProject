package Tests;

import java.time.LocalDate;
import java.util.ArrayList;
import Clients.AdminFacade;
import Clients.ClientType;
import Clients.CompanyFacade;
import Clients.CustomerFacade;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.Customer;

public class Test {

	
	public static void TestAll() {
		try {
		Clients.LoginManager lManager = new Clients.LoginManager();
		 Thread expiredCouponRemover = new Thread(new Jobs.CoupanExpirationDailyJob());
			System.out.println("expired coupon thread started");

		 ArrayList<Coupon> companyCoupons = new ArrayList<Coupon>();
		 ArrayList<Company> companies = new ArrayList<Company>();
		 ArrayList<Customer> customers = new ArrayList<Customer>();

		 Clients.AdminFacade aFacade = (AdminFacade) lManager.login("admin@admin.com", "admin", ClientType.Administrator);
			System.out.println("admin login successfull");

		 Customer testCustomer = new Customer("McClain", "Jhon", "Jhon@McClain.com", "YippieKiYay", 1);

		 Company testCompany = new Company("testCom", "test@company.com","pass", 1, companyCoupons);

		 aFacade.addCompany(testCompany);
			System.out.println("company added");

		 aFacade.addCustomer(testCustomer);
			System.out.println("customer added");

		 companies = aFacade.getAllCompanies();
		 customers = aFacade.getAllCustomers();
		 testCustomer = customers.get(0);
		 testCompany = companies.get(0);
		 testCustomer.setEmail("adress@change.com");
		 testCompany.setName("comTest");
		 aFacade.updateCompany(testCompany);
			System.out.println("company updated");

		 aFacade.updateCustomer(testCustomer);
			System.out.println("customer updated");

		 testCompany= aFacade.getCompany(1);
		 testCustomer = aFacade.getCustomer(testCustomer.getId());
		 aFacade.deleteCompany(testCompany);
		 aFacade.deleteCustomer(testCustomer);
		 aFacade.addCompany(testCompany);
		 aFacade.addCustomer(testCustomer);
		 
		 Clients.CompanyFacade comFacade = (CompanyFacade) lManager.login("test@company.com","pass", ClientType.Company);
		 Coupon coupon = new Coupon("test Coupon", "/images/Testimage.png", "testCoupon", 1, 2,LocalDate.of(2018,9,12), LocalDate.of(2020,1,1), testCompany.getId(), 1, 10.5);
		 
		 comFacade.addCoupon(coupon);
		 testCompany = comFacade.getCompany();
		 companyCoupons = comFacade.getCompanyCoupons();
		 companyCoupons = comFacade.getCompanyCoupons(20.2);
		 companyCoupons = comFacade.getCompanyCoupons(1);
		 coupon.setAmount(5);
		 comFacade.updateCoupon(coupon);
		 
		 Clients.CustomerFacade cusFacade = (CustomerFacade) lManager.login("adress@change.com", "YippieKiYay", ClientType.Customer);
		 cusFacade.purchaseCoupon(coupon);
		 companyCoupons = cusFacade.getCustomerCoupons();
		 companyCoupons = cusFacade.getCustomerCoupons(20.5);
		 companyCoupons = cusFacade.getCustomerCoupons(1);
		 comFacade.deleteCoupon(coupon);
		 expiredCouponRemover.interrupt();
		 
	}
		catch (Exception ex) {

            System.out.println("Error: " + ex.getMessage());
        }
	}
}
