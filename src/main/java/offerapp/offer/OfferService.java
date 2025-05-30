package offerapp.offer;

import offerapp.offer.dto.CreateOfferRequest;
import offerapp.offer.dto.OfferResponse;
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
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> products = productRepository.findAllById(request.getProductIds());

        Offer offer = new Offer(request.getTitle(), user, products);
        Offer saved = offerRepository.save(offer);
        return new OfferResponse(saved);
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
