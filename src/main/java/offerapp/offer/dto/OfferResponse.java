package offerapp.offer.dto;

import offerapp.offer.Offer;
import offerapp.product.Product;
import offerapp.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class OfferResponse {
    private Long id;
    private String title;
    private Long userId;
    private List<Long> productIds;

    public OfferResponse(Offer offer) {
        this.id = offer.getId();
        this.title = offer.getTitle();
        this.userId = offer.getUser().getId();
        this.productIds = offer.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public Long getUserId() { return userId; }

    public List<Long> getProductIds() { return productIds; }
}
