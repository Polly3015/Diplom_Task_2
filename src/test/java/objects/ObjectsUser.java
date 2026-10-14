package objects;

public class ObjectsUser {

    private String email;
    private String password;
    private String name;

    // Конструктор со всеми полями
    public ObjectsUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Только email и пароль
    public ObjectsUser(String email, String password) {
        this(email, password, "");
    }

    // Без параметров
    public ObjectsUser() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
