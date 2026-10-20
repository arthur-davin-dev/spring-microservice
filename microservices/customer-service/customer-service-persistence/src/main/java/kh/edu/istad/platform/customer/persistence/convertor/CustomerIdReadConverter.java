
package kh.edu.istad.platform.customer.persistence.convertor;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@ReadingConverter
public class CustomerIdReadConverter implements Converter<Document, CustomerId> {

    @Override
    public CustomerId convert(Document source) {
        Object storedId = source.get("_id");
        if (storedId == null) {
            return null;
        }

        String id = storedId.toString();
        try {
            return new CustomerId(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            // Older documents may have Mongo-generated ObjectIds. Map them to a
            // stable domain ID so they can still be returned by read operations.
            return new CustomerId(UUID.nameUUIDFromBytes(id.getBytes(StandardCharsets.UTF_8)));
        }
    }
}
