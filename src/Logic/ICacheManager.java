package com.avi.coupons.logic;

public interface ICacheManager {
	
	public void put(Object key, Object value);
	public Object get(Object key);
	
}
