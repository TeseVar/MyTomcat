package edition2;

import edition2.Request;
import edition2.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    //停止服务器
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.await();
    }
    public void await(){
        ServerSocket serverSocket = null;
        int part = 8080;
        try {
            serverSocket = new ServerSocket(part);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while(!shutdown) {
            try {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();
                Response response = new Response(output);
                response.setRequest(request);
//                response.sendStaticResource();
                String uri = request.getUri();
                if(uri.startsWith("/servlet/")){
                    ServletProcessor1 servletProcessor1 = new ServletProcessor1();
                    servletProcessor1.process(request,response);
                }else{
                    StaticResourceProcessor staticResourceProcessor = new StaticResourceProcessor();
                    staticResourceProcessor.process(request,response);
                }

                socket.close();
                //check if the previous URI is a shutdown command
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

    }
}
