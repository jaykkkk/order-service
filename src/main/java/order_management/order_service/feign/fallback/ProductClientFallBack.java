package order_management.order_service.feign.fallback;

import order_management.order_service.dto.ProductDTO;
import order_management.order_service.feign.ProductClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallBack implements FallbackFactory<ProductClient> {
    @Override
    public ProductClient create(Throwable cause) {
      return id-> {
          ProductDTO productDTO = new ProductDTO();
          productDTO.setId(id);
          productDTO.setName("Unknown Product");
          productDTO.setDescription("N/A");
          productDTO.setPrice(0.0);

          return productDTO;
      };
    }
}
