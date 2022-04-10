package com.ya.entity;

public class Courier implements Cloneable {

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

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.getCourierCredentials();
    }

    public String getFirstName() {
        return firstName;
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
    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return object;
    }

    private class CourierCredentials {
        private final String login;
        private final String password;

        protected CourierCredentials(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }
}
