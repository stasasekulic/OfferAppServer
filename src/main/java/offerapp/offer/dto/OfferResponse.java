package offerapp.offer.dto;

import offerapp.offer.Offer;
import offerapp.product.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OfferResponse {
    private Long id;
    private String title;
    private Long userId;
    private String userEmail;
    private List<ProductResponse> products;

    public OfferResponse(Offer offer) {
        this.id = offer.getId();
        this.title = offer.getTitle();
        this.userId = offer.getUser() != null ? offer.getUser().getId() : null;
        this.userEmail = offer.getUser() != null ? offer.getUser().getEmail() : null;

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

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}