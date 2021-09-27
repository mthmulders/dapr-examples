package it.mulders.shop.cart;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.mulders.shop.cart.domain.Cart;
import it.mulders.shop.cart.repository.ShoppingCartRepository;

@Path("/cart")
@Singleton
public class ShoppingCartEndpoint {
    private static final Logger log = LoggerFactory.getLogger(ShoppingCartEndpoint.class);

    @Inject
    @Named("dapr")
    private ShoppingCartRepository shoppingCartRepository;
    
    @Path("/add/{productId}")
    @PUT
    public Response addItemToCart(
        @Context final HttpServletRequest request,
        @PathParam("productId") final UUID productId,
        @QueryParam("itemCount") @DefaultValue("1") final Integer itemCount
    ) {
        var cartId = getCartId(request);
        var cart = shoppingCartRepository.getCart(cartId)
            .orElseGet(() -> shoppingCartRepository.storeCart(cartId, new Cart()));

        log.info("Adding {} items of {} to cart {}", itemCount, productId, cartId);
        cart.addItems(productId, itemCount);
        shoppingCartRepository.storeCart(cartId, cart);

        return Response.noContent().build();
    }

    private String getCartId(final HttpServletRequest request) {
        var session = request.getSession(true);
        return session.getId();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Cart showCart(
        @Context final HttpServletRequest request
    ) {
        var cartId = getCartId(request);
        return shoppingCartRepository.getCart(cartId)
            .orElseThrow(NotFoundException::new);
    }
}
