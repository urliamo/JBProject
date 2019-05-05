package JavaBeans;
public class Company {

    private String name;
    private String email;
    private long company_id=0;
    
    

	

	public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    	
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
    

    
    public Company(String name, String email, long id) {
		this.setName(name);
		this.setEmail(email);
		this.setCompanyID(company_id);
	}

	
}