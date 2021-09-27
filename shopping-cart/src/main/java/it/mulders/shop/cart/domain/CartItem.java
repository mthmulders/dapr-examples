package it.mulders.shop.cart.domain;

import java.util.UUID;

public class CartItem {
    public UUID productId;
    public Integer itemCount;

    public CartItem() {
    }

    public CartItem(final UUID productId, final Integer itemCount) {
        this.itemCount = itemCount;
        this.productId = productId;
    }

    // public Integer getItemCount() {
    //     return this.itemCount;
    // }

    // public UUID getProductId() {
    //     return this.productId;
    // }
}
