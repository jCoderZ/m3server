/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcoderz.m3server.library;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.jcoderz.m3server.library.config.Parameter;
import org.jcoderz.m3server.library.config.Provider;
import org.jcoderz.m3server.library.config.Providers;
import org.jcoderz.m3server.util.Logging;

/**
 * This is the main facade to the media library.
 */
public class Library {

	private static final Logger logger = Logging.getLogger(Library.class);

	public enum Category {
		AUDIO, VIDEO, PHOTOS;
	}

	private Map<Category, Map<String, IProvider>> categoryMap = new HashMap<>();

	/**
	 * The library singleton.
	 */
	public static final Library LIBRARY = new Library();

	/**
	 * Returns an unmodifiable map of providers for the specified category.
	 * 
	 * @return an unmodifiable map of providers for the specified category.
	 */
	public Map<String, IProvider> getProviders(Category category) {
		return Collections.unmodifiableMap(categoryMap.get(category));
	}

	/**
	 * Browse the library at the given path.
	 * 
	 * @param path
	 *            the path to browse
	 * @return a collection of items found at the path
	 */
	public Collection<Item> browse(Path path) {
		// the first part of the path is the category name
		String categoryName = path.index(0);
		// the second part of the path is the provider name
		String providerName = path.index(1);
		Map<String, IProvider> providerMap = categoryMap.get(Category
				.valueOf(categoryName));
		IProvider provider = providerMap.get(providerName);
		Path rest = path.subpath(2);
		return provider.browse(rest);
	}

	public Item item(Path path) {
		// the first part of the path is the category name
		String categoryName = path.index(0);
		// the second part of the path is the provider name
		String providerName = path.index(1);
		Map<String, IProvider> providerMap = categoryMap.get(Category
				.valueOf(categoryName));
		IProvider provider = providerMap.get(providerName);
		Path rest = path.subpath(2);
		return provider.item(rest);
	}

	public void addProvider(Category category, String name, IProvider provider) {
		Map<String, IProvider> providerMap = LIBRARY.categoryMap.get(category);
		if (providerMap != null) {
			if (name != null && provider != null) {
				providerMap.put(name, provider);
			} else {
				throw new IllegalArgumentException(
						"Provider name must not be null");
			}
		} else {
			throw new IllegalArgumentException("Unknown category " + category);
		}
	}

	public void addCategory(Category category) {
		categoryMap.put(category, new HashMap<String, IProvider>());
	}

	public static void init() {
		// add the root categories
		for (Category category : Category.values()) {
			LIBRARY.addCategory(category);
		}

		try {
			JAXBContext context = JAXBContext.newInstance(Providers.class);
			Unmarshaller um = context.createUnmarshaller();

			Providers providers = (Providers) um.unmarshal(Providers.class
					.getResourceAsStream("library.xml"));
			List<Provider> providerList = providers.getProviders();
			for (Provider p : providerList) {
				Category category = p.getCategory();
				Map<String, IProvider> providerMap = LIBRARY.categoryMap
						.get(category);
				if (providerMap != null) {
					Class<?> clazz = p.getClazz();
					IProvider provider = (IProvider) clazz.newInstance();
					String name = p.getName();
					Method setNameMethod = clazz.getMethod("setName");
					try {
						setNameMethod.invoke(provider, name);
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Error invoking method "
								+ setNameMethod, e);
						break;
					}

					List<Parameter> params = p.getParameters();
					boolean initError = false;
					for (Parameter param : params) {
						Method[] methods = clazz.getMethods();
						for (Method m : methods) {
							if (m.getName()
									.toLowerCase()
									.equals("set"
											+ param.getKey().toLowerCase())) {
								try {
									// TODO
									String val = substitutePlaceholder(param
											.getValue());
									if (val != null) {
										m.invoke(provider, val);
									}
								} catch (Exception e) {
									logger.log(Level.SEVERE,
											"Error invoking method " + m, e);
									initError = true;
									break;
								}
							}
						}
					}
					if (!initError) {
						LIBRARY.addProvider(category, name, provider);
					}
				} else {
					logger.warning("Provider has unknown category: " + p);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(LIBRARY.categoryMap);

	}

	private static String substitutePlaceholder(String value) {
		String val = null;
		String pattern = "\\$\\{(.*)\\}.*";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(value);
		if (m.find()) {
			String varName = m.group(1);
			String varValue = System.getProperty(varName);
			if (varValue != null) {
				val = value.replaceAll("\\$\\{" + varName + "\\}", varValue);
			} else {
				logger.warning("Could not find system property " + varName);
			}
		}
		return val;
	}
}
