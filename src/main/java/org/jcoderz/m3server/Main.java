package org.jcoderz.m3server;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.jcoderz.m3server.rest.Mp3Test;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class Main {

    private static URI getBaseURI() {
         return UriBuilder.fromUri("http://localhost/").port(9998).build();
     }
 
     public static final URI BASE_URI = getBaseURI();
 
     protected static HttpServer startServer() throws IOException {
         System.out.println("Starting grizzly...");
         ResourceConfig rc = new PackagesResourceConfig("org.jcoderz.m3server.rest");
         rc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
         HttpServer httpServer = GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
         httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("/home/micha/workspaces/jcoderz/m3server/src/main/webapp"),"/test");
         return httpServer;
     }
     
     public static void main(String[] args) throws IOException {
         HttpServer httpServer = startServer();

         WebappContext ctx = new WebappContext("Test", "/contextPath");

         final ServletRegistration reg = ctx.addServlet("xxx", new Mp3Test());
         ctx.deploy(httpServer);
         System.out.println(String.format("Jersey app started with WADL available at "
                 + "%sapplication.wadl\nTry out %shelloworld\nHit enter to stop it...",
                 BASE_URI, BASE_URI));
         System.in.read();
         httpServer.stop();
     }    
}