package it.mulders.shop.cart.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Singleton;

import it.mulders.shop.cart.domain.Cart;

@Singleton
public class InMemoryShoppingCartRepository implements ShoppingCartRepository {
    private final Map<String, Cart> store = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<Cart> getCart(final String cartId) {
        return Optional.ofNullable(store.get(cartId));
    }

    @Override
    public Cart storeCart(final String id, final Cart cart) {
        store.put(id, cart);
        return cart;
    }
}
