package offerapp.product.dto;

import offerapp.product.Product;
import offerapp.product.enums.ProductType;

public class ProductResponse {
    private final String description;
    private final String picture;
    private final Integer price;
    private final ProductType type;

    public ProductResponse(Product product) {
        this.description = product.getDescription();
        this.picture = product.getPicture();
        this.price = product.getPrice();
        this.type = product.getType();
    }

    public String getDescription() { return description; }
    public String getPicture() { return picture; }
    public Integer getPrice() { return price; }
    public ProductType getType() { return type; }
}
