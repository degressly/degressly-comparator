package com.degressly.proxy.comparator.engine;

import java.lang.reflect.Field;

public class Utils {

	@SuppressWarnings("unchecked")
	public static <T> T mergeObjects(T first, T second) throws IllegalAccessException, InstantiationException {
		Class<?> clazz = first.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Object returnValue = clazz.newInstance();
		for (Field field : fields) {
			field.setAccessible(true);
			Object value1 = field.get(first);
			Object value2 = field.get(second);
			Object value = (value1 != null) ? value1 : value2;
			field.set(returnValue, value);
		}
		return (T) returnValue;
	}

}
