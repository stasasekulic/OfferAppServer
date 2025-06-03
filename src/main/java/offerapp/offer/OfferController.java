package offerapp.offer;

import offerapp.offer.dto.CreateOfferRequest;
import offerapp.offer.dto.OfferResponse;
import offerapp.offer.pdf.OfferGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;
    private final OfferGenerator offerGenerator;
    @Autowired
    public OfferController(OfferService offerService, OfferGenerator offerGenerator) {
        this.offerService = offerService;
        this.offerGenerator = offerGenerator;
    }

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(@RequestBody CreateOfferRequest request) {
        return ResponseEntity.ok(offerService.createOffer(request));
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateOfferPdf(@PathVariable Long id) {
        byte[] pdfBytes = offerGenerator.generatePdfForOffer(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "offer_details_" + id + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
