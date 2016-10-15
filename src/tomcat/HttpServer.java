package tomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    String HOST = "127.0.0.1";
    int PORT = 8888;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    @SuppressWarnings("resource")
	public void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT, 1, InetAddress.getByName(HOST));
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                Request req = new Request(input);
                Response res = new Response(output);

                if (req.getParameter("Uri") != null) {
                    if (req.getParameter("Uri").contains(".")) {
                        StaticResourceProcessor processor = new StaticResourceProcessor();
                        processor.process(req, res);
                    } else {
                        ServletProcessor processor=new ServletProcessor();
                        processor.process(req,res);
                    }
                }
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
