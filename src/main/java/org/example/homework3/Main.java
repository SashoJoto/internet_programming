package org.example.homework3;

import jakarta.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletHandler handler = new ServletHandler();

        ServletHolder uploadHolder = new ServletHolder();
        uploadHolder.setServlet((Servlet) new UploadServlet());
        handler.addServletWithMapping(uploadHolder, "/upload");

        ServletHolder downloadHolder = new ServletHolder();
        downloadHolder.setServlet((Servlet) new FileDownloadServlet());
        handler.addServletWithMapping(downloadHolder, "/files/*");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
