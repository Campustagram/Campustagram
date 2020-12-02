package com.campustagram.core.common;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.campustagram.core.controller.log.ILogger;

@ManagedBean(name = "commonCollections")
@ViewScoped
public class CommonCollections {
	private static ILogger logger = new ILogger();
	private static final String ACTIVE_CLASS_NAME = "CommonCollections";

	/**
	 * Returns the first element of the list.
	 * 
	 * @param <T>
	 * @param list
	 * @return first element of the list
	 */
	public static <T> T getFirst(List<T> list) {
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	/**
	 * Returns the last element of the list.
	 * 
	 * @param <T>
	 * @param list
	 * @return last element of the list.<br>
	 *         returns null if the list is empty or null.
	 */
	public static <T> T getLast(List<T> list) {
		return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
	}

	/**
	 * Returns the first element of the Iterable.
	 * 
	 * @param <T>
	 * @param elements
	 * @return first element of the Iterable
	 */
	public static <T> T getFirstElement(final Iterable<T> elements) {
		return elements.iterator().next();
	}

	/**
	 * Returns the last element of the Iterable.
	 * 
	 * @param <T>
	 * @param elements
	 * @return last element of the Iterable
	 */
	public static <T> T getLastElement(final Iterable<T> elements) {
		T lastElement = null;

		for (T element : elements) {
			lastElement = element;
		}

		return lastElement;
	}
}