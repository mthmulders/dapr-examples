package it.mulders.shop.cart.repository;

import java.util.Optional;

import it.mulders.shop.cart.domain.Cart;

public interface ShoppingCartRepository {
    Optional<Cart> getCart(final String cartId);
    Cart storeCart(final String id, final Cart cart);
}
