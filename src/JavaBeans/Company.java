package JavaBeans;

import java.util.ArrayList;

public class Company {

    private String name;
    private String email;
    private long company_id;
    private ArrayList<Coupon> coupons;
    
    

	

	public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
    	if (email.indexOf(".")>email.indexOf("@"))
        this.email = email;
    	else
    	{
    		System.out.println("invalid mail");	

    	}
    }



    public void setCompanyID(long id) {
        this.company_id = id;
    }

    public String getName() {
        return this.name;
    }



    public String getEmail() {
        return this.email;
    }

    public long getCompanyID() {
        return this.company_id;
    }
    
    public ArrayList<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}
    
    public Company(String name, String email, String password, long id, ArrayList<Coupon> coupons) {
		this.setName(name);
		this.setEmail(email);
		this.setCompanyID(company_id);
		this.setCoupons(coupons);
	}

	
}