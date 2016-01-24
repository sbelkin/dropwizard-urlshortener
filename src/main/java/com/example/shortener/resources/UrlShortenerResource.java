package com.example.shortener.resources;

import com.example.shortener.core.Url;
import com.example.shortener.db.UrlDAO;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class UrlShortenerResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlShortenerResource.class);

    private final UrlDAO urlDAO;

    public UrlShortenerResource(UrlDAO urlDAO) {
        this.urlDAO = urlDAO;
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    public Response getRedirectUrl(@PathParam("id") long id) {
        Optional<Url> url = urlDAO.findById(id);
        if (url.isPresent()) {
            return Response.ok().entity(url.get().getUrlTarget()).build();
        } else {
            return Response.status(400).entity("Url target does't exist.").build();
        }
    }

    @POST
    @Path("")
    @UnitOfWork
    public Response createRedirectUrl(@NotEmpty String url) {

        Url urlCreate = new Url(url);
//        return Response.ok().entity(urlCreate).build();
        Url urlReturn = urlDAO.create(urlCreate);
        return Response.ok().entity(url + " we are here " + urlReturn.getId()).build();
    }
}
