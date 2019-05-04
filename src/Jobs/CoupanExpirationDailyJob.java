package Jobs;

import java.time.LocalDate;
import java.util.ArrayList;

import JavaBeans.Coupon;

/**
 * runnable class in charge of deleting expired coupons.
 *
 * @param  quit boolean controlling when to stop thread
 * @param couponsDBDAO DB access object for coupons
 * @param date the current time
 * @see 		JavaBeans.Coupon
 */
public class CoupanExpirationDailyJob implements Runnable {
	
	private boolean quit = false;
	private  DB.CouponsDAO couponsDBDAO = new DB.CouponsDAO();
	private LocalDate date = LocalDate.now();
	
	public void setQuit(boolean quit) {
		this.quit = quit;
	}
	
	@Override
	public void run() {
		while(!quit) {
			try {
				//check if date changes since last check
			if (date.isBefore(LocalDate.now())) {
				//update current date
				date = LocalDate.now();
				//get expired coupons
				ArrayList<Coupon> expiredCoupons = couponsDBDAO.getExpiredCoupons();
				//delete each coupon histories
				for (Coupon c : expiredCoupons) {
					couponsDBDAO.deleteCoupon(c.getId());
					couponsDBDAO.deleteCouponPurchase(c.getId(), -1);
				}
			}
			//wait 1 hour and check for date change
			try {
					Thread.sleep(1000*60*60);
				}
			 catch (InterruptedException Ex) {
				 System.out.println(Ex.getMessage());
		    }
			}
			catch(Exception Ex){
				 System.out.println(Ex.getMessage());

			}
		}
	}
	
	public CoupanExpirationDailyJob() {
		super();
		
	}
	
	public void stop() {
		this.setQuit(true);
		
	}
}
