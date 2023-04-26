package com.automate.df.tree;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlatToTree {

	private FlatToTree() {

	}

	/**
	 * @param <T>
	 * @param <ID>
	 * @param nodesMap
	 * @param childNode
	 */
	private static <T extends Nodeable<ID>, ID> void addToParent(final Map<ID, Node<T>> nodesMap,
			final Node<T> childNode) {
		ID parentId = childNode.getElement().getParentId();
		if (null != parentId) {
			final Node<T> parentT = nodesMap.get(parentId);
			if(null != parentT) {
			final List<Node<T>> children = parentT.computeChildernIfAbsent();
			children.add(childNode);
			}
		}
	}

	public static <T extends Nodeable<ID>, ID> List<Node<T>> constructNodes(final Node<T>[] nodes) {

		final Map<ID, Node<T>> nodesMap = Arrays.stream(nodes).collect(Collectors
				.toMap(node -> node.getElement().getId(), Function.identity(), (oldValue, newValue) -> newValue));

		nodesMap.forEach((tId, tNode) -> addToParent(nodesMap, tNode));

		return Arrays.stream(nodes).filter(t -> null == t.getElement().getParentId()).collect(Collectors.toList());

	}

	public static <T extends Nodeable<ID>, ID> List<Node<T>> constructNodes(final List<Node<T>> nodes) {

		final Map<ID, Node<T>> nodesMap = nodes.stream().collect(Collectors.toMap(node -> node.getElement().getId(),
				Function.identity(), (oldValue, newValue) -> newValue));

		nodesMap.forEach((tId, tNode) -> addToParent(nodesMap, tNode));

		return nodes.stream().filter(t -> null == t.getElement().getParentId()).collect(Collectors.toList());

	}

	public static <ID, T extends Nodeable<ID>> List<Node<T>> construct(final List<T> data) {
		final List<Node<T>> nodes = data.stream().map(element -> new Node<T>(element)).collect(Collectors.toList());
		return constructNodes(nodes);
	}

}
