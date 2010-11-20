package org.maschinenstuermer.clojure.install;

public class ClojureInstall {
	public static class Version {
		private final int major;
		private final int minor;
		private final int incremental;
		
		public Version(int major, int minor, int incremental) {
			this.major = major;
			this.minor = minor;
			this.incremental = incremental;
		}

		public int getMajor() {
			return major;
		}

		public int getMinor() {
			return minor;
		}

		public int getIncremental() {
			return incremental;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + incremental;
			result = prime * result + major;
			result = prime * result + minor;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Version other = (Version) obj;
			if (incremental != other.incremental)
				return false;
			if (major != other.major)
				return false;
			if (minor != other.minor)
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return String.format("%d.%d.%d", major, minor, incremental);
		}
	}
	private String name;
	private Version version;
	private String location;
	private boolean isDefault = true;
	
	ClojureInstall(final String name, final Version version, final String location) {
		this.name = name;
		this.version = version;
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

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
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
