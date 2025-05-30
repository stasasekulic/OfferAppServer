package offerapp.product;


import jakarta.persistence.*;
import offerapp.product.enums.ProductType;

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

    public Product() {}

    public Product(String description, String picture, Integer price, ProductType type) {
        this.description = description;
        this.picture = picture;
        this.price = price;
        this.type = type;
    }

    public Long getId() {  return id; }

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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}