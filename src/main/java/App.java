import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) throws Exception {

        // Bind to port 0 → OS gives guaranteed free port
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);

        int port = server.getAddress().getPort(); // get assigned port

        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:" + port);
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("static/index.html");

            if (is == null) {
                String response = "File not found!";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            byte[] response = is.readAllBytes();

            t.getResponseHeaders().add("Content-Type", "text/html");
            t.sendResponseHeaders(200, response.length);

            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}