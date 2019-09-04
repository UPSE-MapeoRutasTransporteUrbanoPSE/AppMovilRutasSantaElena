package com.rutasbus.santaelena.app.rutas;

import android.app.Application;

/**
 * Clase que maneja variables globales
 */
public class GlobalClass extends Application{

    private String userMovil = "user_movil";
    private String passMovil = "abc123";


    public String getUserMovil() {
        return userMovil;
    }

    public void setUserMovil(String userMovil) {
        this.userMovil = userMovil;
    }

    public String getPassMovil() {
        return passMovil;
    }

    public void setPassMovil(String passMovil) {
        this.passMovil = passMovil;
    }
}
