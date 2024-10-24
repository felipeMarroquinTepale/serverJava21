package org.marroquin;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Server {
    HttpServer server;
    ArrayList<String> mensajes = new ArrayList<>();


    public Server() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8091),0);
    }

    public void definirRuta(){
        server.createContext("/",exchange -> {

            var bytes = Files.readAllBytes(Path.of("/home/felipe/IdeaProjects/server/src/main/resources/index.html"));

            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        });

        server.createContext("/sendMessage", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Leer el cuerpo de la solicitud
                InputStream is = exchange.getRequestBody();
                String requestBody = new String(is.readAllBytes());

                // Extraer el contenido del mensaje (application/x-www-form-urlencoded)
                String mensaje = requestBody.split("=")[1]; // Asumiendo que solo hay un parámetro llamado "mensaje"

                // Guardar el mensaje en la lista
                mensajes.add(mensaje);

                // Mostrar el mensaje en la consola del servidor
                System.out.println("Mensaje recibido en el server: " + mensajes);

                // Enviar respuesta de confirmación
                String response = "Mensaje recibido y almacenado: " + mensajes;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

    }

    public void iniciar(){
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado en el puerto 8091");
    }


}

