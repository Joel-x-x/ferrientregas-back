package com.ferrientregas.email.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String token){
        return "Hola" + name + ",\n\nTu nueva cuenta ha sido creada." +
                "Este es tu codigo de verificacion: " + token + "\n\n" +
                "Gracias por usar nuestro servicio";
    }
}