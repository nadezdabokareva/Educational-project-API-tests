package com.ya.entity;

public class Courier implements Cloneable {

    private class CourierCredentials{
        private String login;
        private String password;

        protected CourierCredentials(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    private String login;
    private String password;
    private String firstName;
    private int id;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier(String courierLogin, String courierPassword) {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourierCredentials getCourierCredentials() {
        return new CourierCredentials(login, password);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
