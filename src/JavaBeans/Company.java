package JavaBeans;

import java.util.ArrayList;

public class Company {

    private String name;
    private String email;
    private String password;
    private int id;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public int getId() {
        return this.id;
    }
    
    public ArrayList<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}
    
    public Company(String name, String email, String password, int id, ArrayList<Coupon> coupons) {
		this.setName(name);
		this.setEmail(email);
		this.setPassword(password);
		this.setId(id);
		this.setCoupons(coupons);
	}

	
}