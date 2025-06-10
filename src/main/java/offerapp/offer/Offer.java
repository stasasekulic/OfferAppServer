package offerapp.offer;

import jakarta.persistence.*;
import offerapp.offer.enums.OfferStatus;
import offerapp.product.Product;
import offerapp.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "offer_products",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    public Offer() {}

    public Offer(String title, User user, List<Product> products) {
        this.title = title;
        this.user = user;
        this.products = products;
        this.status = OfferStatus.CREATED;
    }

    public Offer(String title, User user, List<Product> products, OfferStatus status) {
        this.title = title;
        this.user = user;
        this.products = products;
        this.status = status;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.getOffers().add(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.getOffers().remove(this);
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public OfferStatus getStatus() { return status; } // Dodato
    public void setStatus(OfferStatus status) { this.status = status; } // Dodato
}