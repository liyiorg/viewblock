package com.github.liyiorg.viewblock.core;

import java.util.LinkedHashMap;
import java.util.Map;


public class BModelMap extends LinkedHashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6081352570622358131L;

	/**
	 * Construct a new, empty {@code ModelMap}.
	 */
	public BModelMap() {
	}

	/**
	 * Construct a new {@code ModelMap} containing the supplied attribute
	 * under the supplied name.
	 * @see #addAttribute(String, Object)
	 */
	public BModelMap(String attributeName, Object attributeValue) {
		addAttribute(attributeName, attributeValue);
	}


	/**
	 * Add the supplied attribute under the supplied name.
	 * @param attributeName the name of the model attribute (never {@code null})
	 * @param attributeValue the model attribute value (can be {@code null})
	 */
	public BModelMap addAttribute(String attributeName, Object attributeValue) {
		put(attributeName, attributeValue);
		return this;
	}

	

	/**
	 * Copy all attributes in the supplied {@code Map} into this {@code Map}.
	 * @see #addAttribute(String, Object)
	 */
	public BModelMap addAllAttributes(Map<String, ?> attributes) {
		if (attributes != null) {
			putAll(attributes);
		}
		return this;
	}

	/**
	 * Copy all attributes in the supplied {@code Map} into this {@code Map},
	 * with existing objects of the same name taking precedence (i.e. not getting
	 * replaced).
	 */
	public BModelMap mergeAttributes(Map<String, ?> attributes) {
		if (attributes != null) {
			for (String key : attributes.keySet()) {
				if (!containsKey(key)) {
					put(key, attributes.get(key));
				}
			}
		}
		return this;
	}

	/**
	 * Does this model contain an attribute of the given name?
	 * @param attributeName the name of the model attribute (never {@code null})
	 * @return whether this model contains a corresponding attribute
	 */
	public boolean containsAttribute(String attributeName) {
		return containsKey(attributeName);
	}
}
