package engine.usermanager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class UserManager {
    private final Set<String> users;
    public UserManager() { users = new HashSet<>(); }
    public synchronized void addUser(String username) {users.add(username);}
    public synchronized void removeUser(String username) {users.remove(username);}
    public synchronized Set<String> getUsers() { return Collections.unmodifiableSet(users);}
    public boolean isUserExists(String username) {return users.contains(username);}
}
