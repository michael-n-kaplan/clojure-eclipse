package org.maschinenstuermer.clojure.install;

public class ClojureInstall {
	private String name;
	private String location;
	private boolean isDefault = true;
	
	public ClojureInstall(final String name, final String location) {
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
