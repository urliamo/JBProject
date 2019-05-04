package com.avi.coupons.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class HartaCacheManager implements ICacheManager{
	
	private Map<Object, Object> map;
	
	public HartaCacheManager() {
		this.map = new HashMap<Object, Object>();
	}

	@Override
	public void put(Object key, Object value) {
		this.map.put(key, value);
	}

	@Override
	public Object get(Object key) {
		return this.map.get(key);
	}
}
