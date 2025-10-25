package order_management.order_service.services;



import order_management.order_service.dto.OrderRequestDTO;
import order_management.order_service.dto.OrderResponseDTO;
import order_management.order_service.models.Order;

import java.util.List;

public interface OrderService {

    void createOrder(OrderRequestDTO orderRequestDTO);

    List<Order> getAllOrders();

    OrderResponseDTO getOrderById(Long id);

    String updateOrder(Long id, OrderRequestDTO orderRequestDTO);

    String deleteOrderById(Long id);
}
