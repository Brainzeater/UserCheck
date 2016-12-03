package password;

public class Element {
    /*Соль*/
    private String salt;
    /*Хэш соли и пароля*/
    private String hash;
    /*Логин*/
    private String user;

    public Element(String salt, String hash, String user) {
        this.salt = salt;
        this.hash = hash;
        this.user = user;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public String getUser() {
        return user;
    }
}
