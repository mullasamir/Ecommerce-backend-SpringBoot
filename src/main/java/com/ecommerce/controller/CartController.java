package com.ecommerce.controller;

import com.ecommerce.exceptions.CartItemException;
import com.ecommerce.exceptions.ProductException;
import com.ecommerce.exceptions.UserException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.reponse.ApiResponse;
import com.ecommerce.request.AddItemRequest;
import com.ecommerce.services.CartItemService;
import com.ecommerce.services.CartService;
import com.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/")
    public ResponseEntity<Cart>findUserCart(@RequestHeader("Authorization") String jwt) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse>addItemToCart(@RequestBody AddItemRequest req,
                                                    @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(user.getId(),req);
        ApiResponse res = new ApiResponse();
        res.setMsg("Item added to cart.");
        res.setStatus(true);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }


    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse>removeItemFromCart(@PathVariable Long cartItemId,@RequestHeader("Authorization") String jwt) throws UserException, CartItemException {

        User user = userService.findUserProfileByJwt(jwt);

        cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse res = new ApiResponse();
        res.setMsg("Cart Item with id :"+cartItemId+ " is Deleted");
        res.setStatus(true);
        return new ResponseEntity<>(res,HttpStatus.OK);


    }
}
