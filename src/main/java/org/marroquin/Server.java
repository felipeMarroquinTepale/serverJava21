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


    public Server(int port) throws IOException {
        //Esta creando un servidor HTTP que escucha el puerto 8091
        server = HttpServer.create(new InetSocketAddress(port),0);
    }

    public void definirRutaRaiz(){
        server.createContext("/",exchange -> {
            var bytes = Files.readAllBytes(Path.of("/home/felipe/IdeaProjects/server/src/main/resources/index.html"));
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        });
    }

    public void definirRutaSendMessage(){
        server.createContext("/sendMessage", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String requestBody = new String(is.readAllBytes());

                System.out.println(requestBody);

                String mensaje = requestBody.split("=")[1];
                Message msg = new Message(mensaje);

                mensajes.add(msg.getContent());

                String response = String.valueOf(mensajes);
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

