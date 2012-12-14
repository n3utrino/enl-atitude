package ch.n3utrino.enlatitude.server.resources;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Main Class für den Mapper Server startet das servlet
 */
public class EnlatitudeServer {

    public static void main(String... args) {

        new EnlatitudeServer();

    }


    public EnlatitudeServer() {

        Server server = new Server(9089);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder h = new ServletHolder(new ServletContainer());
        h.setInitParameter("com.sun.jersey.config.property.packages", "ch.n3utrino.enlatitude.server.resources");
        context.addServlet(h, "/resources/*");
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
