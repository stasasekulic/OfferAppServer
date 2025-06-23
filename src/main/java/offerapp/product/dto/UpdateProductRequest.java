package offerapp.product.dto;

import offerapp.product.enums.ProductRole;
import offerapp.product.enums.ProductType;

public class UpdateProductRequest {
    private String description;
    private String picture;
    private Integer price;
    private ProductType type;
    private ProductRole role;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }

    public ProductRole getRole() { return role; }
    public void setRole(ProductRole role) { this.role = role; }
}
