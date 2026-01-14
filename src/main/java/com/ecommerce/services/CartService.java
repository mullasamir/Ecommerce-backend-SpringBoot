package com.ecommerce.services;

import com.ecommerce.exceptions.CartItemException;
import com.ecommerce.exceptions.ProductException;
import com.ecommerce.exceptions.UserException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.request.AddItemRequest;

public interface CartService {

    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;

    public Cart findUserCart(Long userId);


}
