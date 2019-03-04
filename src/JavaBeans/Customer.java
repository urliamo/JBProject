package JavaBeans;

public class Customer {
		private String lastName;
		private String firstName;
		private String email;
	    private String password;
	    private int id;
	    
	    public String getLastName() {
			return lastName;
		}
		public void setLastName(String last_name) {
			this.lastName = last_name;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String first_name) {
			this.firstName = first_name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			if (email.indexOf(".")>email.indexOf("@"))
		        this.email = email;
		    	else
		    	{
		    		System.out.println("invalid mail");	

		    	}
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		
		public Customer(String lastName, String firstName, String email, String password, int id) {
			super();
			this.setLastName(lastName);
			this.setFirstName(firstName);
			this.setEmail(email);
			this.setPassword(password);
			this.setId(id);
		}

		
		
}
