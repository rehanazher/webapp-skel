/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.commons.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaut.skeleton.commons.util.dao.Entity;

public class EntityUtils {

	public static <S, E extends Entity> Map<String, E> list2Map(
			final List<E> entityList) {

		Map<String, E> map = null;
		if (!Validators.isEmpty(entityList)) {
			map = new HashMap<String, E>();
			for (E entity : entityList) {
				map.put(entity.getId(), entity);
			}
		}

		return map;
	}
}
