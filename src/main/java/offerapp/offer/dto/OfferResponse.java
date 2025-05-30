package offerapp.offer.dto;

import offerapp.offer.Offer;
import offerapp.product.dto.ProductResponse;
import offerapp.user.dto.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OfferResponse {
    private Long id;
    private String title;
    private UserResponse user;
    private List<ProductResponse> products;

    public OfferResponse(Offer offer) {
        this.id = offer.getId();
        this.title = offer.getTitle();
        this.user = new UserResponse(offer.getUser());
        this.products = offer.getProducts().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public UserResponse getUser() {
        return user;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}
