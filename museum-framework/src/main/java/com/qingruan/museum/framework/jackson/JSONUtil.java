package com.qingruan.museum.framework.jackson;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 序列化和反序列化json的工具类.
 * 
 * @author tommy
 */
@Slf4j
public class JSONUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

//	private static final Gson gson = new GsonBuilder().registerTypeAdapter(
//			MuseumMsg.class, new InterfaceTypeAdapter()).create();

	static {
		if (log.isDebugEnabled()) {
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		}

		objectMapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
		objectMapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T deserialize(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		}

		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T deserializeGson(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		}

		try {
			return null;
//			return gson.fromJson(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * 
	 * @param createNew
	 * @return
	 */
	public static ObjectMapper getObjectMapper(boolean createNew) {
		if (createNew) {
			return new ObjectMapper();
		}

		return getObjectMapper();
	}

}
