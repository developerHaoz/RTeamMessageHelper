package com.hans.alpha.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ClassUtil {

    private static final Map<Class<?>, ClassType> classTypeMap = new ConcurrentHashMap<Class<?>, ClassType>();

    private static final Map<Class<?>, Class<?>> baseClassMap = new ConcurrentHashMap<Class<?>, Class<?>>();
    private static final Map<Class<?>, Class<?>> simpleClassMap = new ConcurrentHashMap<Class<?>, Class<?>>();
    private static final Map<Class<?>, Class<?>> baseArrayMap = new ConcurrentHashMap<Class<?>, Class<?>>();
    private static final Map<Class<?>, Class<?>> simpleArrayMap = new ConcurrentHashMap<Class<?>, Class<?>>();
    private static final Map<String, Class<?>> baseClassNameMap = new ConcurrentHashMap<String, Class<?>>();
    private static final Map<String, Class<?>> simpleClassNameMap = new ConcurrentHashMap<String, Class<?>>();

    public static final ClassProcessor DEFAULT_CLASS_PROCESSOR = new DefaultClassProcessor();

    private ClassUtil() {
    }

    public static enum ClassType {
        JAVA_LANG_STRING, JAVA_LANG_CHARACTER, JAVA_LANG_SHORT, JAVA_LANG_INTEGER, JAVA_LANG_LONG,
        JAVA_LANG_FLOAT, JAVA_LANG_DOUBLE, JAVA_LANG_BOOLEAN, JAVA_LANG_BYTE, JAVA_UTIL_DATE,
        CHAR, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BOOLEAN,
        JAVA_LANG_STRING_ARRAY, JAVA_LANG_CHARACTER_ARRAY, JAVA_LANG_SHORT_ARRAY, JAVA_LANG_INTEGER_ARRAY, JAVA_LANG_LONG_ARRAY,
        JAVA_LANG_FLOAT_ARRAY, JAVA_LANG_DOUBLE_ARRAY, JAVA_LANG_BOOLEAN_ARRAY, JAVA_LANG_BYTE_ARRAY,
        CHAR_ARRAY, BYTE_ARRAY, SHORT_ARRAY, INT_ARRAY, LONG_ARRAY, FLOAT_ARRAY, DOUBLE_ARRAY, BOOLEAN_ARRAY
    }

    static {
        classTypeMap.put(String.class, ClassType.JAVA_LANG_STRING);
        classTypeMap.put(Character.class, ClassType.JAVA_LANG_CHARACTER);
        classTypeMap.put(Short.class, ClassType.JAVA_LANG_SHORT);
        classTypeMap.put(Integer.class, ClassType.JAVA_LANG_INTEGER);
        classTypeMap.put(Long.class, ClassType.JAVA_LANG_LONG);
        classTypeMap.put(Float.class, ClassType.JAVA_LANG_FLOAT);
        classTypeMap.put(Double.class, ClassType.JAVA_LANG_DOUBLE);
        classTypeMap.put(Boolean.class, ClassType.JAVA_LANG_BOOLEAN);
        classTypeMap.put(Byte.class, ClassType.JAVA_LANG_BYTE);
        classTypeMap.put(Date.class, ClassType.JAVA_UTIL_DATE);
        classTypeMap.put(char.class, ClassType.CHAR);
        classTypeMap.put(byte.class, ClassType.BYTE);
        classTypeMap.put(short.class, ClassType.SHORT);
        classTypeMap.put(int.class, ClassType.INT);
        classTypeMap.put(long.class, ClassType.LONG);
        classTypeMap.put(float.class, ClassType.FLOAT);
        classTypeMap.put(double.class, ClassType.DOUBLE);
        classTypeMap.put(boolean.class, ClassType.BOOLEAN);

        classTypeMap.put(String[].class, ClassType.JAVA_LANG_STRING_ARRAY);
        classTypeMap.put(Character[].class, ClassType.JAVA_LANG_CHARACTER_ARRAY);
        classTypeMap.put(Short[].class, ClassType.JAVA_LANG_SHORT_ARRAY);
        classTypeMap.put(Integer[].class, ClassType.JAVA_LANG_INTEGER_ARRAY);
        classTypeMap.put(Long[].class, ClassType.JAVA_LANG_LONG_ARRAY);
        classTypeMap.put(Float[].class, ClassType.JAVA_LANG_FLOAT_ARRAY);
        classTypeMap.put(Double[].class, ClassType.JAVA_LANG_DOUBLE_ARRAY);
        classTypeMap.put(Boolean[].class, ClassType.JAVA_LANG_BOOLEAN_ARRAY);
        classTypeMap.put(Byte[].class, ClassType.JAVA_LANG_BYTE_ARRAY);
        classTypeMap.put(char[].class, ClassType.CHAR_ARRAY);
        classTypeMap.put(byte[].class, ClassType.BYTE_ARRAY);
        classTypeMap.put(short[].class, ClassType.SHORT_ARRAY);
        classTypeMap.put(int[].class, ClassType.INT_ARRAY);
        classTypeMap.put(long[].class, ClassType.LONG_ARRAY);
        classTypeMap.put(float[].class, ClassType.FLOAT_ARRAY);
        classTypeMap.put(double[].class, ClassType.DOUBLE_ARRAY);
        classTypeMap.put(boolean[].class, ClassType.BOOLEAN_ARRAY);

        baseClassMap.put(char.class, char.class);
        baseClassMap.put(byte.class, byte.class);
        baseClassMap.put(short.class, short.class);
        baseClassMap.put(int.class, int.class);
        baseClassMap.put(long.class, long.class);
        baseClassMap.put(float.class, float.class);
        baseClassMap.put(double.class, double.class);
        baseClassMap.put(boolean.class, boolean.class);

        simpleClassMap.put(String.class, String.class);
        simpleClassMap.put(Character.class, Character.class);
        simpleClassMap.put(Byte.class, Byte.class);
        simpleClassMap.put(Short.class, Short.class);
        simpleClassMap.put(Integer.class, Integer.class);
        simpleClassMap.put(Long.class, Long.class);
        simpleClassMap.put(Float.class, Float.class);
        simpleClassMap.put(Double.class, Double.class);
        simpleClassMap.put(Boolean.class, Boolean.class);

        baseArrayMap.put(char[].class, char[].class);
        baseArrayMap.put(byte[].class, byte[].class);
        baseArrayMap.put(short[].class, short[].class);
        baseArrayMap.put(int[].class, int[].class);
        baseArrayMap.put(long[].class, long[].class);
        baseArrayMap.put(float[].class, float[].class);
        baseArrayMap.put(double[].class, double[].class);
        baseArrayMap.put(boolean[].class, boolean[].class);

        simpleArrayMap.put(String[].class, String[].class);
        simpleArrayMap.put(Character[].class, Character[].class);
        simpleArrayMap.put(Short[].class, Short[].class);
        simpleArrayMap.put(Integer[].class, Integer[].class);
        simpleArrayMap.put(Long[].class, Long[].class);
        simpleArrayMap.put(Float[].class, Float[].class);
        simpleArrayMap.put(Double[].class, Double[].class);
        simpleArrayMap.put(Boolean[].class, Boolean[].class);
        simpleArrayMap.put(Byte[].class, Byte[].class);

        baseClassNameMap.put(char.class.getName(), char.class);
        baseClassNameMap.put(byte.class.getName(), byte.class);
        baseClassNameMap.put(short.class.getName(), short.class);
        baseClassNameMap.put(int.class.getName(), int.class);
        baseClassNameMap.put(long.class.getName(), long.class);
        baseClassNameMap.put(float.class.getName(), float.class);
        baseClassNameMap.put(double.class.getName(), double.class);
        baseClassNameMap.put(boolean.class.getName(), boolean.class);

        simpleClassNameMap.put(String.class.getName(), String.class);
        simpleClassNameMap.put(Character.class.getName(), Character.class);
        simpleClassNameMap.put(Byte.class.getName(), Byte.class);
        simpleClassNameMap.put(Short.class.getName(), Short.class);
        simpleClassNameMap.put(Integer.class.getName(), Integer.class);
        simpleClassNameMap.put(Long.class.getName(), Long.class);
        simpleClassNameMap.put(Float.class.getName(), Float.class);
        simpleClassNameMap.put(Double.class.getName(), Double.class);
        simpleClassNameMap.put(Boolean.class.getName(), Boolean.class);
    }

    /**
     * get class with class name
     *
     * @param className
     * @return Type
     * @throws Exception class not found
     */
    public static Class<?> getClass(final ClassLoader classLoader, final String className) throws Exception {
        Class<?> clazz = null;
        if (classLoader != null && className != null) {
            if (baseClassNameMap.containsKey(className)) {
                clazz = baseClassNameMap.get(className);
            } else {
                clazz = classLoader.loadClass(className);
            }
        }
        return clazz;
    }

    /**
     * getClassType,for manual judge use
     *
     * @param clazz
     * @return ClassType
     */
    public static ClassType getClassType(Class<?> clazz) {
        return classTypeMap.get(clazz);
    }

    /**
     * is base class or not
     * include boolean short int long float double byte char
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isBaseClass(Class<?> clazz) {
        boolean result = false;
        if (clazz != null) {
            if (baseClassMap.containsKey(clazz)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * is base class or not
     * include boolean short int long float double byte char
     *
     * @param className
     * @return boolean
     */
    public static boolean isBaseClass(String className) {
        boolean result = false;
        if (className != null) {
            if (baseClassNameMap.containsKey(className)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * simple class or not
     * include Boolean Short Integer Long Float Double Byte String
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isSimpleClass(Class<?> clazz) {
        boolean result = false;
        if (clazz != null) {
            if (simpleClassMap.containsKey(clazz)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * simple class or not
     * include Boolean Short Integer Long Float Double Byte String
     *
     * @param className
     * @return boolean
     */
    public static boolean isSimpleClass(String className) {
        boolean result = false;
        if (className != null) {
            if (simpleClassNameMap.containsKey(className)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * basic array or not
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isBaseArray(Class<?> clazz) {
        boolean result = false;
        if (clazz != null) {
            if (baseArrayMap.containsKey(clazz)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * simple array or not
     *
     * @param clazz
     * @return boolean
     */
    public static boolean isSimpleArray(Class<?> clazz) {
        boolean result = false;
        if (clazz != null) {
            if (simpleArrayMap.containsKey(clazz)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * change type
     *
     * @param <T>
     * @param clazz
     * @param values
     * @return Object
     */
    public static <T extends Object> T changeType(Class<T> clazz, String[] values) {
        return changeType(clazz, values, null, DEFAULT_CLASS_PROCESSOR);
    }

    /**
     * change type width class processor
     *
     * @param <T>
     * @param clazz
     * @param values
     * @param classProcessor
     * @return Object
     */
    public static <T extends Object> T changeType(Class<T> clazz, String[] values, ClassProcessor classProcessor) {
        return changeType(clazz, values, null, classProcessor);
    }

    /**
     * change type
     *
     * @param <T>
     * @param clazz
     * @param values
     * @param fieldName is null if not exist
     * @return Object
     */
    public static <T extends Object> T changeType(Class<T> clazz, String[] values, String fieldName) {
        return changeType(clazz, values, fieldName, DEFAULT_CLASS_PROCESSOR);
    }

    /**
     * change type width class processor
     *
     * @param <T>
     * @param clazz
     * @param values
     * @param fieldName      is null if not exist
     * @param classProcessor
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T changeType(Class<T> clazz, String[] values, String fieldName, ClassProcessor classProcessor) {
        Object object = null;
        if (classProcessor != null) {
            object = classProcessor.changeClassProcess(clazz, values, fieldName);
        } else {
            throw new NullPointerException("ClassProcessor can not be null.");
        }
        return (T) object;
    }

    public static interface ClassProcessor {

        /**
         * change class process
         *
         * @param clazz
         * @param values
         * @param fieldName is null if not exist
         * @return Object
         */
        public abstract Object changeClassProcess(Class<?> clazz, String[] values, String fieldName);
    }

    /**
     * get class loader
     *
     * @param cls
     * @return class loader
     */
    public static ClassLoader getClassLoader(Class<?> cls) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = cls.getClassLoader();
        }
        return cl;
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>
     * Call this method if you intend to use the thread context ClassLoader in a
     * scenario where you absolutely need a non-null ClassLoader reference: for
     * example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
     * reference as well).
     *
     * @return the default ClassLoader (never <code>null</code>)
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getClassLoader() {
        return getClassLoader(Class.class);
    }

    /**
     * Same as <code>Class.forName()</code>, except that it works for primitive
     * types.
     */
    public static Class<?> forName(String name) throws ClassNotFoundException {
        return forName(name, getClassLoader());
    }

    /**
     * Replacement for <code>Class.forName()</code> that also returns Class
     * instances for primitives (like "int") and array class names (like
     * "String[]").
     *
     * @param name the name of the Class
     * @param classLoader the class loader to use (may be <code>null</code>,
     *            which indicates the default class loader)
     * @return Class instance for the supplied name
     * @throws ClassNotFoundException if the class was not found
     * @throws LinkageError if the class file could not be loaded
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static Class<?> forName(String name, ClassLoader classLoader)
            throws ClassNotFoundException, LinkageError {

        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz != null) {
            return clazz;
        }

        // "java.lang.String[]" style arrays
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[Ljava.lang.String;" style arrays
        int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
        if (internalArrayMarker != -1 && name.endsWith(";")) {
            String elementClassName = null;
            if (internalArrayMarker == 0) {
                elementClassName = name
                        .substring(INTERNAL_ARRAY_PREFIX.length(), name.length() - 1);
            } else if (name.startsWith("[")) {
                elementClassName = name.substring(1);
            }
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = getClassLoader();
        }
        return classLoaderToUse.loadClass(name);
    }

    /**
     * Resolve the given class name as primitive class, if appropriate,
     * according to the JVM's naming rules for primitive classes.
     * <p>
     * Also supports the JVM's internal class names for primitive arrays. Does
     * <i>not</i> support the "[]" suffix notation for primitive arrays; this is
     * only supported by {@link #forName}.
     *
     * @param name the name of the potentially primitive class
     * @return the primitive class, or <code>null</code> if the name does not
     *         denote a primitive class or primitive array class
     */
    public static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;
        // Most class names will be quite long, considering that they
        // SHOULD sit in a package, so a length check is worthwhile.
        if (name != null && name.length() <= 8) {
            // Could be a primitive - likely.
            result = (Class<?>) primitiveTypeNameMap.get(name);
        }
        return result;
    }

    /** Suffix for array class names: "[]" */
    public static final String  ARRAY_SUFFIX            = "[]";
    /** Prefix for internal array class names: "[L" */
    private static final String INTERNAL_ARRAY_PREFIX   = "[L";

    /**
     * Map with primitive type name as key and corresponding primitive type as
     * value, for example: "int" -> "int.class".
     */
    private static final Map<String,Class<?>>    primitiveTypeNameMap    = new HashMap<String, Class<?>>(16);

    /**
     * Map with primitive wrapper type as key and corresponding primitive type
     * as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>,Class<?>>    primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        Set<Class<?>> primitiveTypeNames = new HashSet<Class<?>>(16);
        primitiveTypeNames.addAll(primitiveWrapperTypeMap.values());
        primitiveTypeNames.addAll(Arrays
                .asList(new Class<?>[] { boolean[].class, byte[].class, char[].class, double[].class,
                        float[].class, int[].class, long[].class, short[].class }));
        for (Iterator<Class<?>> it = primitiveTypeNames.iterator(); it.hasNext();) {
            Class<?> primitiveClass = (Class<?>) it.next();
            primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
        }
    }

    public static String toShortString(Object obj){
        if(obj == null){
            return "null";
        }
        return obj.getClass().getSimpleName() + "@" + System.identityHashCode(obj);

    }
}
