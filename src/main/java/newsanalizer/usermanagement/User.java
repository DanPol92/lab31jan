package newsanalizer.usermanagement;

import java.io.Serializable;
import java.util.Objects;

public class User  {
    private String username;
    private String password;
    private boolean isAdmin;
    private boolean isAnalist;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAnalist() {
        return isAnalist;
    }

    public void setAnalist(boolean analist) {
        isAnalist = analist;
    }

    public boolean equalsUsers(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }



    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String addToFile(){
        return username+","+password+","+isAdmin+','+isAnalist;
    }
}
