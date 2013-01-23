package ch.n3utrino.enlatitude.common;

public class User {

    private String name;
    private String uuid;
    private Location location;

    private long lastUpdate;
    private long lastUpdateSince;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getLastUpdateSince() {
        return lastUpdateSince;
    }

    public void setLastUpdateSince(long lastUpdate) {
        this.lastUpdateSince = lastUpdate;
    }

}


