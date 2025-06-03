package offerapp.user.dto;

import offerapp.offer.Offer;
import offerapp.offer.dto.OfferResponse;
import offerapp.user.User;
import offerapp.user.enums.UserRole;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private List<OfferResponse> offers;

    public UserResponse() {}

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();

        if (user.getOffers() != null) {
            this.offers = user.getOffers().stream()
                    .map(OfferResponse::new) // Ovo je sada ok
                    .collect(Collectors.toList());
        }
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public List<OfferResponse> getOffers() { return offers; }
    public void setOffers(List<OfferResponse> offers) { this.offers = offers; }
}
