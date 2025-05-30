package offerapp.offer.dto;

import java.util.List;

public class CreateOfferRequest {
    private String title;
    private Long userId;
    private List<Long> productIds;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
}
