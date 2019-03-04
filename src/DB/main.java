package DB;


public class main {



//	    public static void buildDB() throws SQLException {
//	        
//	    }

	   /* public static void resetDB() throws SQLException {

	        ResetDB.resetCompaniesTable();
	        ResetDB.resetCustomersTable();
	        ResetDB.resetCouponsTable();
	        ResetDB.resetJoinTable();
	    }
*/
	    public static void main(String[] args) {

	        try {
	        	DBCreator.buildDB();
	        	Tests.Test.TestAll();

	        } catch (Exception ex) {

	            System.out.println("Error: " + ex.getMessage());
	        }

	    }
	
}
