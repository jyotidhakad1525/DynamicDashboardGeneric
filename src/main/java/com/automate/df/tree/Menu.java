package com.automate.df.tree;

public class Menu implements Nodeable<String> {

	private final String pageIdentifer;
	private final String parentIdentifier;
	private final String name;
	private final String mode;
	private String redirectUrl;

	public Menu(String pageIdentifer, String parentIdentifier, String name, String mode) {
		super();
		this.pageIdentifer = pageIdentifer;
		this.parentIdentifier = parentIdentifier;
		this.name = name;
		this.mode = mode;
	}

	public Menu(String pageIdentifer, String name, String mode) {
		this(pageIdentifer, null, name, mode);
	}

	@Override
	public String getId() {
		return pageIdentifer;
	}

	@Override
	public String getParentId() {
		return parentIdentifier;
	}

	@Override
	public String toString() {
		return "Menu [pageIdentifer=" + pageIdentifer + ", parentIdentifier=" + parentIdentifier + ", name=" + name
				+ ", mode=" + mode + "]";
	}

	public String getPageIdentifer() {
		return pageIdentifer;
	}

	public String getParentIdentifier() {
		return parentIdentifier;
	}

	public String getName() {
		return name;
	}

	public String getMode() {
		return mode;
	}

}
