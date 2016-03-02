package com.qingruan.museum.pma.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * 鎵弿鎸囧畾鍖咃紙鍖呮嫭jar锛変笅鐨刢lass鏂囦欢 <br>
 * <a href="http://sjsky.iteye.com">http://sjsky.iteye.com</a>
 * 
 * @author michael
 */
public class ClassPathScanHandler {

	/**
	 * 鏄惁鎺掗櫎鍐呴儴绫�true->鏄�false->鍚�
	 */
	private boolean excludeInner = true;
	/**
	 * 杩囨护瑙勫垯閫傜敤鎯呭喌 true鈥�鎼滅储绗﹀悎瑙勫垯鐨�false->鎺掗櫎绗﹀悎瑙勫垯鐨�
	 */
	private boolean checkInOrEx = true;

	/**
	 * 杩囨护瑙勫垯鍒楄〃 濡傛灉鏄痭ull鎴栬�绌猴紝鍗冲叏閮ㄧ鍚堜笉杩囨护
	 */
	private List<String> classFilters = null;

	/**
	 * 鏃犲弬鏋勯�鍣紝榛樿鏄帓闄ゅ唴閮ㄧ被銆佸苟鎼滅储绗﹀悎瑙勫垯
	 */
	public ClassPathScanHandler() {
	}

	/**
	 * excludeInner:鏄惁鎺掗櫎鍐呴儴绫�true->鏄�false->鍚�br>
	 * checkInOrEx锛氳繃婊よ鍒欓�鐢ㄦ儏鍐�true鈥�鎼滅储绗﹀悎瑙勫垯鐨�false->鎺掗櫎绗﹀悎瑙勫垯鐨�br>
	 * classFilters锛氳嚜瀹氫箟杩囨护瑙勫垯锛屽鏋滄槸null鎴栬�绌猴紝鍗冲叏閮ㄧ鍚堜笉杩囨护
	 * 
	 * @param excludeInner
	 * @param checkInOrEx
	 * @param classFilters
	 */
	public ClassPathScanHandler(Boolean excludeInner, Boolean checkInOrEx,
			List<String> classFilters) {
		this.excludeInner = excludeInner;
		this.checkInOrEx = checkInOrEx;
		this.classFilters = classFilters;

	}

	/**
	 * 鎵弿鍖� *
	 * 
	 * @param basePackage
	 *            鍩虹鍖� * @param recursive 鏄惁閫掑綊鎼滅储瀛愬寘
	 * @return Set
	 */
	public Set<Class<?>> getPackageAllClasses(String basePackage,
			boolean recursive) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packageName = basePackage;
		if (packageName.endsWith(".")) {
			packageName = packageName
					.substring(0, packageName.lastIndexOf('.'));
		}
		String package2Path = packageName.replace('.', '/');

		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(package2Path);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					// log.info("鎵弿file绫诲瀷鐨刢lass鏂囦欢....");
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					doScanPackageClassesByFile(classes, packageName, filePath,
							recursive);
				} else if ("jar".equals(protocol)) {
					// log.info("鎵弿jar鏂囦欢涓殑绫�...");
					doScanPackageClassesByJar(packageName, url, recursive,
							classes);
				}
			}
		} catch (IOException e) {
			// log.error("IOException error:", e);
		}

		return classes;
	}

	/**
	 * 浠ar鐨勬柟寮忔壂鎻忓寘涓嬬殑鎵�湁Class鏂囦欢<br>
	 * 
	 * @param basePackage
	 *            eg锛歮ichael.utils.
	 * @param url
	 * @param recursive
	 * @param classes
	 */
	private void doScanPackageClassesByJar(String basePackage, URL url,
			final boolean recursive, Set<Class<?>> classes) {
		String packageName = basePackage;
		String package2Path = packageName.replace('.', '/');
		JarFile jar;
		try {
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (!name.startsWith(package2Path) || entry.isDirectory()) {
					continue;
				}

				// 鍒ゆ柇鏄惁閫掑綊鎼滅储瀛愬寘
				if (!recursive
						&& name.lastIndexOf('/') != package2Path.length()) {
					continue;
				}
				// 鍒ゆ柇鏄惁杩囨护 inner class
				if (this.excludeInner && name.indexOf('$') != -1) {
					// log.info("exclude inner class with name:" + name);
					continue;
				}
				String classSimpleName = name
						.substring(name.lastIndexOf('/') + 1);
				// 鍒ゅ畾鏄惁绗﹀悎杩囨护鏉′欢
				if (this.filterClassName(classSimpleName)) {
					String className = name.replace('/', '.');
					className = className.substring(0, className.length() - 6);
					try {
						classes.add(Thread.currentThread()
								.getContextClassLoader().loadClass(className));
					} catch (ClassNotFoundException e) {
						// log.error("Class.forName error:", e);
					}
				}
			}
		} catch (IOException e) {
			// log.error("IOException error:", e);
		}
	}

	/**
	 * 浠ユ枃浠剁殑鏂瑰紡鎵弿鍖呬笅鐨勬墍鏈塁lass鏂囦欢
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private void doScanPackageClassesByFile(Set<Class<?>> classes,
			String packageName, String packagePath, boolean recursive) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		final boolean fileRecursive = recursive;
		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return fileRecursive;
				}
				String filename = file.getName();
				if (excludeInner && filename.indexOf('$') != -1) {
					// log.info("exclude inner class with name:" + filename);
					return false;
				}
				return filterClassName(filename);
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				doScanPackageClassesByFile(classes,
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive);
			} else {
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className));

				} catch (ClassNotFoundException e) {
					// log.error("IOException error:", e);
				}
			}
		}
	}

	/**
	 * 鏍规嵁杩囨护瑙勫垯鍒ゆ柇绫诲悕
	 * 
	 * @param className
	 * @return
	 */
	private boolean filterClassName(String className) {
		if (!className.endsWith(".class")) {
			return false;
		}
		if (null == this.classFilters || this.classFilters.isEmpty()) {
			return true;
		}
		String tmpName = className.substring(0, className.length() - 6);
		boolean flag = false;
		for (String str : classFilters) {
			String tmpreg = "^" + str.replace("*", ".*") + "$";
			Pattern p = Pattern.compile(tmpreg);
			if (p.matcher(tmpName).find()) {
				flag = true;
				break;
			}
		}
		return (checkInOrEx && flag) || (!checkInOrEx && !flag);
	}

	/**
	 * @return the excludeInner
	 */
	public boolean isExcludeInner() {
		return excludeInner;
	}

	/**
	 * @return the checkInOrEx
	 */
	public boolean isCheckInOrEx() {
		return checkInOrEx;
	}

	/**
	 * @return the classFilters
	 */
	public List<String> getClassFilters() {
		return classFilters;
	}

	/**
	 * @param pExcludeInner
	 *            the excludeInner to set
	 */
	public void setExcludeInner(boolean pExcludeInner) {
		excludeInner = pExcludeInner;
	}

	/**
	 * @param pCheckInOrEx
	 *            the checkInOrEx to set
	 */
	public void setCheckInOrEx(boolean pCheckInOrEx) {
		checkInOrEx = pCheckInOrEx;
	}

	/**
	 * @param pClassFilters
	 *            the classFilters to set
	 */
	public void setClassFilters(List<String> pClassFilters) {
		classFilters = pClassFilters;
	}
}
