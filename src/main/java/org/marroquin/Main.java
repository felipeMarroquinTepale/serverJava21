package org.marroquin;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digita el puerto para el serve: ");
        int port = sc.nextInt();
        Server server = new Server(port);
        server.definirRutaRaiz();
        server.definirRutaSendMessage();
        server.iniciar();
    }
}