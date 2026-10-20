package kh.edu.istad.platform.customer.persistence.config;

import kh.edu.istad.platform.customer.persistence.convertor.CustomerIdReadConverter;
import kh.edu.istad.platform.customer.persistence.convertor.CustomerIdWriteConverter;
import kh.edu.istad.platform.customer.persistence.convertor.EmailReadConverter;
import kh.edu.istad.platform.customer.persistence.convertor.EmailWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
@EnableReactiveMongoAuditing(auditorAwareRef = "auditorAwareConfig")
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return MongoCustomConversions.create(adapter -> {
            adapter.registerConverter(new CustomerIdReadConverter());
            adapter.registerConverter(new CustomerIdWriteConverter());
            adapter.registerConverter(new EmailReadConverter());
            adapter.registerConverter(new EmailWriteConverter());
        });
    }
}
