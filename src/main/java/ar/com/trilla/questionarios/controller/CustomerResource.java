package ar.com.trilla.questionarios.controller;

import ar.com.trilla.questionarios.model.Customer;
import ar.com.trilla.questionarios.model.validation.CustomerValidator;
import ar.com.trilla.questionarios.model.validation.CustomerValidator.Result;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestPath;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@ApplicationScoped
@Path("/customers")
@Produces("application/json")
@Consumes("application/json")
public class CustomerResource {

    @Inject
    private CustomerValidator customerValidator;

    @GET
    public Uni<List<Customer>> get() {
        return Customer.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Uni<Customer> getSingle(Long id) {
        return Customer.findById(id);
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> create(Customer customer) {
        return Panache.<Customer>withTransaction(customer::persist)
                .onItem()
                .transform(inserted -> Response.created(URI.create("/customers/" + inserted.id)).build());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@RestPath Long id, Customer customer) {
        if (customer == null || customer.name == null) {
            throw new WebApplicationException("Customer name was not set on request.", 422);
        }

        return Panache
                .withTransaction(() -> Customer.<Customer>findById(id)
                        .onItem().ifNotNull().invoke(entity -> entity.name = customer.name)
                )
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@RestPath Long id) {
        return Panache.withTransaction(() -> Customer.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(NO_CONTENT).build()
                        : Response.ok().status(NOT_FOUND).build());
    }

    @Path("/validation")
    @POST
    public Result validation(Customer customer) {
        return customerValidator.validate(customer);
    }
}