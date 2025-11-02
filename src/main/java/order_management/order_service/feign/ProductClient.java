package order_management.order_service.feign;

import order_management.order_service.dto.ProductDTO;
import order_management.order_service.feign.fallback.ProductClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${external.product-service-url}",fallbackFactory = ProductClientFallBack.class)
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
