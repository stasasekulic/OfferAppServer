package offerapp.offer;

import offerapp.offer.dto.CreateOfferRequest;
import offerapp.offer.dto.OfferResponse;
import offerapp.offer.enums.OfferStatus;
import offerapp.product.Product;
import offerapp.product.ProductRepository;
import offerapp.user.User;
import offerapp.user.UserRepository;
import offerapp.offer.exception.OfferNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public OfferResponse createOffer(CreateOfferRequest request) {
        if (request.getProductIds() == null || request.getProductIds().isEmpty()) {
            throw new IllegalArgumentException("Offer must contain at least one product.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Product> products = productRepository.findAllById(request.getProductIds());
        if (products.size() != request.getProductIds().size()) {
            throw new IllegalArgumentException("Some products not found");
        }

        Offer offer = new Offer();
        offer.setTitle(request.getTitle());
        offer.setUser(user);
        offer.setProducts(products);
        offer.setStatus(OfferStatus.CREATED);

        Offer savedOffer = offerRepository.save(offer);

        return new OfferResponse(savedOffer);
    }

    public List<OfferResponse> getAllOffers() {
        return offerRepository.findAll().stream()
                .map(OfferResponse::new)
                .collect(Collectors.toList());
    }

    public OfferResponse getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with id: " + id));
        return new OfferResponse(offer);
    }

    public void deleteOffer(Long id) {
        if (!offerRepository.existsById(id)) {
            throw new OfferNotFoundException("Offer not found with id: " + id);
        }
        offerRepository.deleteById(id);
    }
}