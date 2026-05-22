package com.faust.ticketing.rest.v1.endpoint;

import com.faust.ticketing.core.service.CategoryService;
import com.faust.ticketing.persistence.model.dto.client.CategoryDTO;
import com.faust.ticketing.persistence.model.dto.client.SaveCategoryDTO;
import com.faust.ticketing.persistence.model.entity.user.RoleName;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Cache(maxAge = 5)
@DenyAll
@Path("/category")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryEndpoint {
    @EJB
    private CategoryService categoryService;

    @Context
    private UriInfo uriInfo;

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    @Path("/")
    @GET
    public Response getCategories() {
        final Set<CategoryDTO> categories = categoryService.getAll();
        return Response.ok(categories).build();
    }

    @RolesAllowed({RoleName.Constant.BASIC_FUNCTIONALITY})
    @Path("/{id}")
    @GET
    public Response getCategory(@PathParam("id") final Integer categoryId) {
        final CategoryDTO category = categoryService.getById(categoryId);
        return Response.ok(category).build();
    }

    @RolesAllowed({RoleName.Constant.CATEGORY_ADMINISTRATION})
    @Path("/{id}")
    @DELETE
    public Response deleteCategory(@PathParam("id") final Integer categoryId) {
        categoryService.delete(categoryId);
        return Response.noContent().build();
    }

    @RolesAllowed({RoleName.Constant.CATEGORY_ADMINISTRATION})
    @Path("/{id}")
    @PUT
    public Response updateCategory(@PathParam("id") final Integer categoryId, final SaveCategoryDTO category) {
        final CategoryDTO updatedCategory = categoryService.update(categoryId, category);
        return Response.ok(updatedCategory).build();
    }

    @RolesAllowed({RoleName.Constant.CATEGORY_ADMINISTRATION})
    @Path("/")
    @POST
    public Response createCategory(final SaveCategoryDTO category) {
        final CategoryDTO createdCategory = categoryService.create(category);
        return Response.ok(createdCategory).build();
    }
}
