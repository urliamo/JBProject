package DB;
import java.util.ArrayList;

import JavaBeans.Coupon;


public interface CouponsDAO {

	void addCoupon(Coupon coupon) throws Exception;
    void updateCoupon(Coupon coupon) throws Exception;   
	ArrayList<Coupon> getAllCoupons() throws Exception;
    Coupon getOneCoupon(int CouponID) throws Exception;
    void deleteCoupon(int CouponID) throws Exception;
    void deleteCouponPurchase(int CouponID, int CustomerID) throws Exception;
    void addCouponPurchase(int CouponID, int CustomerID) throws Exception;
}
