package offerapp.offer.dto;

import offerapp.offer.Offer;
import offerapp.product.Product;
import offerapp.product.dto.ProductResponse;
import offerapp.user.User;
import offerapp.user.dto.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OfferResponse {
    private Long id;
    private String title;
    private UserResponse user;
    private List<ProductResponse> products;

    public OfferResponse(offerapp.offer.Offer offer) {
        this.id = offer.getId();
        this.title = offer.getTitle();
        this.user = new UserResponse(offer.getUser());
        this.products = offer.getProducts().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
