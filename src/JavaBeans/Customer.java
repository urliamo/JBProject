package JavaBeans;

public class Customer {
		private String lastName;
		private String firstName;
	    private int customerId;
	    
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
		
		public int getCustomerId() {
			return customerId;
		}
		public void setCustomerId(int id) {
			this.customerId = id;
		}
		
		
		public Customer(String lastName, String firstName, String email, String password, int id) {
			super();
			this.setLastName(lastName);
			this.setFirstName(firstName);
			this.setCustomerId(id);
		}

		
		
}
