package order_management.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO implements Serializable {
    private Long id;
    private Long productId;
    private Long customerId;
    private Integer quantity;
    private String orderStatus;
    private double price;
}
