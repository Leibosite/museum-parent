package com.qingruan.museum.nosql.redis;

import java.util.HashMap;
import java.util.Map;


/**
 * copy from <code>org.apache.commons.lang3.ClassUtils</code>.
 * 
 * @author tommy
 */
public class PrimitiveOrWrapperUtil {

    /**
     * Maps primitive {@code Class}es to their corresponding wrapper {@code Class}.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
    static {
         primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
         primitiveWrapperMap.put(Byte.TYPE, Byte.class);
         primitiveWrapperMap.put(Character.TYPE, Character.class);
         primitiveWrapperMap.put(Short.TYPE, Short.class);
         primitiveWrapperMap.put(Integer.TYPE, Integer.class);
         primitiveWrapperMap.put(Long.TYPE, Long.class);
         primitiveWrapperMap.put(Double.TYPE, Double.class);
         primitiveWrapperMap.put(Float.TYPE, Float.class);
         primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }

    /**
     * Maps wrapper {@code Class}es to their corresponding primitive types.
     */
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
    static {
        for (Class<?> primitiveClass : primitiveWrapperMap.keySet()) {
            Class<?> wrapperClass = primitiveWrapperMap.get(primitiveClass);
            if (!primitiveClass.equals(wrapperClass)) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
    }
    
    /**
     * Returns whether the given {@code type} is a primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character}, {@link Short},
     * {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     * 
     * @param type
     *            The class to query or null.
     * @return true if the given {@code type} is a primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character}, {@link Short},
     *         {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     * @since 3.1
     */
    public static boolean isPrimitiveWrapper(Class<?> type) {
        return wrapperPrimitiveMap.containsKey(type);
    }
    
    /**
     * Returns whether the given {@code type} is a primitive or primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character},
     * {@link Short}, {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     * 
     * @param type
     *            The class to query or null.
     * @return true if the given {@code type} is a primitive or primitive wrapper ({@link Boolean}, {@link Byte}, {@link Character},
     *         {@link Short}, {@link Integer}, {@link Long}, {@link Double}, {@link Float}).
     * @since 3.1
     */
    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        if (type == null) {
            return false;
        }
        return type.isPrimitive() || isPrimitiveWrapper(type);
    }

}
