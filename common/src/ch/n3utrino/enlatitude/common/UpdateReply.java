package ch.n3utrino.enlatitude.common;


import java.util.HashMap;
import java.util.Map;

public class UpdateReply {

    private Map<String, User> users = new HashMap<String, User>();
    private String status = "ERROR";

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
