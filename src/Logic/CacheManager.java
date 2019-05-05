package Logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CacheManager implements ICacheManager{
	
	private Map<Object, Object> map;
	
	public CacheManager() {
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
