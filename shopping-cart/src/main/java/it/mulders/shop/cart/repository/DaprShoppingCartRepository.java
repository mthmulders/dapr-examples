package it.mulders.shop.cart.repository;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import it.mulders.shop.cart.domain.Cart;

@Named("dapr")
@Singleton
public class DaprShoppingCartRepository implements ShoppingCartRepository {
    private final DaprStateStore<Cart> carts = new DaprStateStore<>("carts", Cart.class);

    @Override
    public Optional<Cart> getCart(final String cartId) {
        return carts.getValue(cartId);
    }

    @Override
    public Cart storeCart(final String id, final Cart cart) {
        carts.storeValue(id, cart);
        return cart;
    }
}
