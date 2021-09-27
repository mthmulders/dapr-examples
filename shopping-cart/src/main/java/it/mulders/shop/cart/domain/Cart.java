package it.mulders.shop.cart.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.json.bind.annotation.JsonbCreator;

public class Cart {
    public List<CartItem> items = new ArrayList<CartItem>();

    // public Cart() {
    //     this(new ArrayList<CartItem>());
    // }

    // @JsonbCreator
    // public Cart(final List<CartItem> items) {
    //     this.items = items;
    // }

    public void addItems(final UUID productId, final int itemCount) {
        for (var item : items) {
            if (item.productId.equals(productId)) {
                items.remove(item);
                items.add(new CartItem(productId, item.itemCount + itemCount));
                return;
            }
        }

        items.add(new CartItem(productId, itemCount));
    }

    // public Collection<CartItem> getItems() {
    //     return Collections.unmodifiableCollection(this.items);
    // }
}
