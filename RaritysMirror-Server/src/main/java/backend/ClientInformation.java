package backend;

public class ClientInformation {
	private final String version;
	private String name;
	private long lastUpdated;
	
	public ClientInformation(String version) {
		this.version = version;
	}
	
	public String getName() { return name; }
	public String getVersion() { return version; }
	public long getLastUpdated() { return lastUpdated; }
	
	public void setName(String name) { this.name = name; }
	public void setLastUpdated(long newTime) { lastUpdated = newTime; }	
}
