package order_management.order_service.feign;

import order_management.order_service.dto.CustomerDTO;
import order_management.order_service.feign.fallback.CustomerClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${external.customer-service-url}",fallbackFactory = CustomerClientFallBack.class)
public interface CustomerClient {
    @GetMapping("/api/v1/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable Long id);
}
