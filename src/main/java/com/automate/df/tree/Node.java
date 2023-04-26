package com.automate.df.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T element;
	private List<Node<T>> childern;

	private static Object ID = new Object();

	public Node(T element, List<Node<T>> childern) {
		super();
		this.element = element;
		this.childern = childern;
	}

	public Node(T element) {
		this(element, null);
	}

	public List<Node<T>> computeChildernIfAbsent() {

		if (null == childern) {
			synchronized (ID) {
				childern = new ArrayList<>();
			}
		}

		return childern;
	}

	@Override
	public String toString() {
		return "Node [element=" + element + ", childern=" + childern + "]";
	}

	public static Object getID() {
		return ID;
	}

	public static void setID(Object iD) {
		ID = iD;
	}

	public T getElement() {
		return element;
	}

	public void setElement(T element) {
		this.element = element;
	}

	public List<Node<T>> getChildern() {
		return childern;
	}

	public void setChildern(List<Node<T>> childern) {
		this.childern = childern;
	}

}
