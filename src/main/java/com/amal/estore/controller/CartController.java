package com.amal.estore.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amal.estore.beans.Cart;
import com.amal.estore.model.Customer;
import com.amal.estore.model.Product;
import com.amal.estore.model.service.ProductService;
import com.amal.estore.model.service.PurchaseService;

@Controller
public class CartController {

	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private ProductService productService;
	@Autowired
	private Cart cart;
	@Autowired
	private PurchaseService purchaseService;
	
	@RequestMapping(value = "cart/add/{productId}")
	public String addToCart(@PathVariable("productId") Long productId, @RequestHeader("referer") String referedFrom) {
		Product product = productService.findProduct(productId);
		cart.addProduct(product, 1);
		logger.debug("Adding product to cart " + product);
		return "redirect:" + referedFrom;
	}

	
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String showCart(Model model) {
		model.addAttribute(cart);
		return "cart";
	}
	
	@RequestMapping(value = "/cart/placeOrder", method = RequestMethod.POST)
	public String placeOrder(HttpSession session, RedirectAttributes redirectAttributes) {
		if (cart.getContents().isEmpty()) {
			redirectAttributes.addFlashAttribute("cartMessage", "Cart empty. Please add products to the cart.");
			return "redirect:/cart";
		}
		else {
			Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
			purchaseService.savePurchase(cart.getContents(), loggedInUser);
			redirectAttributes.addFlashAttribute("cartMessage", "Order placed. Total cost: " + cart.getTotalCost());
			cart.clearCart();
			
			return "redirect:/cart";
		}
	}
	
}
