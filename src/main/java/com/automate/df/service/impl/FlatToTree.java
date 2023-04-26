package com.automate.df.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.automate.df.entity.OrgVerticalLocationRoleMenu;
import com.google.gson.Gson;

public class FlatToTree {

	private FlatToTree() {

	}

	/**
	 * @param <T>
	 * @param <ID>
	 * @param nodesMap
	 * @param childNode
	 */
	private static void addToParent(final Map<Integer, OrgVerticalLocationRoleMenu> nodesMap,
			final OrgVerticalLocationRoleMenu childNode) {
		Integer parentId = childNode.getParentIdentifier();
		if (null != parentId) {
			final OrgVerticalLocationRoleMenu parentT = nodesMap.get(parentId);
			final List<OrgVerticalLocationRoleMenu> children = parentT.computeChildernIfAbsent();
			children.add(childNode);
		}
	}

	public static List<OrgVerticalLocationRoleMenu> construct(final List<OrgVerticalLocationRoleMenu> data) {
		final Map<Integer, OrgVerticalLocationRoleMenu> nodesMap = data.stream()
				.collect(Collectors.toMap(menu -> menu.getId(), Function.identity(), (oldValue, newValue) -> newValue));

		nodesMap.forEach((tId, tNode) -> addToParent(nodesMap, tNode));

		return data.stream().filter(t -> null == t.getParentIdentifier()).collect(Collectors.toList());
	}
	
	public static void main(String[] args) {
		List<OrgVerticalLocationRoleMenu> menus = new ArrayList<>();

		OrgVerticalLocationRoleMenu dashBoard = new OrgVerticalLocationRoleMenu(0, "DashBoard", "edit");
		menus.add(dashBoard);

		OrgVerticalLocationRoleMenu bdNInfra = new OrgVerticalLocationRoleMenu(1, "BD & Infra", "edit");
		menus.add(bdNInfra);

		OrgVerticalLocationRoleMenu franchiser = new OrgVerticalLocationRoleMenu(2, 1, "Franchiser", "edit");
		OrgVerticalLocationRoleMenu fi = new OrgVerticalLocationRoleMenu(21, 2, "Franchiser Information", "edit");
		OrgVerticalLocationRoleMenu ca = new OrgVerticalLocationRoleMenu(22, 2, "Create Advertisment", "edit");
		OrgVerticalLocationRoleMenu va = new OrgVerticalLocationRoleMenu(23, 2, "View Advertisment", "edit");
		OrgVerticalLocationRoleMenu sa = new OrgVerticalLocationRoleMenu(24, 2, "Search Advertisment", "edit");
		OrgVerticalLocationRoleMenu da = new OrgVerticalLocationRoleMenu(25, 2, "Dealer Applicatioin", "edit");
		OrgVerticalLocationRoleMenu viewApp = new OrgVerticalLocationRoleMenu(26, 2, "View Application", "edit");
		OrgVerticalLocationRoleMenu as = new OrgVerticalLocationRoleMenu(27, 2, "Application Status", "edit");
		OrgVerticalLocationRoleMenu ls = new OrgVerticalLocationRoleMenu(28, 2, "Layout Status", "edit");
		OrgVerticalLocationRoleMenu acts = new OrgVerticalLocationRoleMenu(29, 2, "Activity Status", "edit");

		menus.add(franchiser);
		menus.add(fi);
		menus.add(ca);
		menus.add(va);
		menus.add(sa);
		menus.add(da);
		menus.add(viewApp);
		menus.add(as);
		menus.add(ls);
		menus.add(acts);

		OrgVerticalLocationRoleMenu hrms = new OrgVerticalLocationRoleMenu(3, "HRMS", "edit");
		menus.add(hrms);

		List<OrgVerticalLocationRoleMenu> values = FlatToTree.construct(menus);
		String json = new Gson().toJson(values);
		System.out.println(json);
	}

}
