package DB;
import java.util.ArrayList;

import JavaBeans.Coupon;


public interface ICouponsDAO {

	long addCoupon(Coupon coupon) throws Exception;
    void updateCoupon(Coupon coupon) throws Exception;   
	ArrayList<Coupon> getAllCoupons() throws Exception;
    Coupon getOneCoupon(long CouponID) throws Exception;
    void deleteCoupon(long CouponID) throws Exception;
}
