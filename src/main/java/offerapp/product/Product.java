package offerapp.product;


import jakarta.persistence.*;
import offerapp.offer.Offer;
import offerapp.product.enums.ProductRole;
import offerapp.product.enums.ProductType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String picture;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductRole role;

    public Product() {}

    public Product(String description, String picture, Integer price, ProductType type, ProductRole role) {
        this.description = description;
        this.picture = picture;
        this.price = price;
        this.type = type;
        this.role = role;
    }

    @ManyToMany(mappedBy = "products")
    private List<Offer> offers = new ArrayList<>();

    public Long getId() {  return id; }

    public List<Offer> getOffers() { return offers; }
    public void setOffers(List<Offer> offers) { this.offers = offers; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public ProductRole getRole() {
        return role;
    }

    public void setRole(ProductRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}