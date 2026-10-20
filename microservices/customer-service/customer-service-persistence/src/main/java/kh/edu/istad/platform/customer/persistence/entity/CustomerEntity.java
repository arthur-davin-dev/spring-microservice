//package kh.edu.istad.platform.customer.persistence.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.persistence.Column;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.UUID;
//import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
//
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name="customers")
//public class CustomerEntity {
//    @Id
//    private UUID customerId;
//    @Column(nullable = false, unique = true)
//    private String username;
//    @Column(nullable = false)
//    private String familyName;
//    @Column(nullable = false)
//    private String givenName;
//    @Column(nullable = false, unique = true)
//    private String email;
//    @Column(nullable = false)
//    private String phoneNumber;
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private CustomerStatus status;
//}
