package kh.edu.istad.platform.customer.persistence.document;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CustomerDocument implements Persistable<String> {

    @Id
    private String id;

    private String username;

    private String familyName;

    private String givenName;

    private String email;

    private String phoneNumber;

    private CustomerStatus status;

    @DocumentReference
    private SegmentDocument segment;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedBy
    private String updateBy;

    @LastModifiedDate
    private Instant updateAt;

    @Override
    public boolean isNew() {
        // Create uses insert(); documents with an assigned ID must be treated as existing for save().
        return id == null;
    }
}
