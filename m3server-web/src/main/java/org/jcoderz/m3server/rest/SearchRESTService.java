package org.jcoderz.m3server.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jcoderz.m3server.model.Member;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/search")
@RequestScoped
public class SearchRESTService {

   @GET
   @Produces("application/json")
   public List<Member> listAllMembers() {
      return null;
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Member lookupMemberById(@PathParam("id") long id) {
      return null;
   }
}
