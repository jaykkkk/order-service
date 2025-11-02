package order_management.order_service.feign.fallback;

import order_management.order_service.dto.CustomerDTO;
import order_management.order_service.feign.CustomerClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerClientFallBack implements FallbackFactory<CustomerClient> {
    @Override
    public CustomerClient create(Throwable cause) {
        return id-> {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(id);
            customerDTO.setName("Unknown Customer");
            customerDTO.setEmail("N/A");

            return customerDTO;
        };
    }
}
