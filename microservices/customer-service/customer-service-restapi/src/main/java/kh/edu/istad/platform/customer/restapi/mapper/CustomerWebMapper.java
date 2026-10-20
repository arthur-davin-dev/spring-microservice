package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerSegment;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerWebMapper {

    InitiateCustomerCommand toCommand(CustomerInitiateRequest request);

    CustomerInitiateResponse toResponse(InitiateCustomerResult result);

    @Mapping(source = "email", target = "email", qualifiedByName = "stringToEmail")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "stringToPhoneNumber")
    @Mapping(source = "segment", target = "segment", qualifiedByName = "segmentFromString")
    Customer toCustomer(CustomerInitiateRequest request);

    @Mapping(source = "id.value", target = "customerId")
    @Mapping(source = "email", target = "email", qualifiedByName = "emailToString")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "phoneNumberToString")
    @Mapping(source = "segment", target = "segment", qualifiedByName = "segmentToString")
    CustomerInitiateResponse toResponse(Customer customer);

    @Named("stringToEmail")
    default Email stringToEmail(String value) {
        return value == null ? null : new Email(value);
    }

    @Named("emailToString")
    default String emailToString(Email email) {
        return email == null ? null : email.value();
    }

    @Named("stringToPhoneNumber")
    default PhoneNumber stringToPhoneNumber(String value) {
        return value == null ? null : new PhoneNumber(value);
    }

    @Named("segmentFromString")
    default CustomerSegment segmentFromString(String value) {
        return CustomerSegment.from(value);
    }

    @Named("segmentToString")
    default String segmentToString(CustomerSegment value) {
        return value == null ? null : value.label();
    }

    @Named("phoneNumberToString")
    default String phoneNumberToString(PhoneNumber phoneNumber) {
        return phoneNumber == null ? null : phoneNumber.value();
    }
}
