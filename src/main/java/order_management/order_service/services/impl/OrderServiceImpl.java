package order_management.order_service.services.impl;

import com.jay.orderservice.dto.OrderRequestDTO;
import com.jay.orderservice.dto.OrderResponseDTO;
import com.jay.orderservice.enums.OrderStatusEnum;
import com.jay.orderservice.models.Customer;
import com.jay.orderservice.models.Order;
import com.jay.orderservice.models.Product;
import com.jay.orderservice.repository.CustomerRepository;
import com.jay.orderservice.repository.OrderRepository;
import com.jay.orderservice.repository.ProductRepository;
import com.jay.orderservice.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;



    @Override
    public void createOrder(OrderRequestDTO orderRequestDTO) {

        //validateOrderRequest(orderRequestDTO);


        Customer  customer = customerRepository.findById(orderRequestDTO.getCustomerId()).orElseThrow(()->new RuntimeException("Customer not found"));
        Product product= productRepository.findById(orderRequestDTO.getProductId()).orElseThrow(()->new RuntimeException("Product not found"));;

        if(product.getAvailableQuantity() < orderRequestDTO.getQuantity()){
            throw new RuntimeException("Insufficient product quantity");
        }
        // Deduct the ordered quantity from the product's available quantity
        product.setAvailableQuantity(product.getAvailableQuantity() - orderRequestDTO.getQuantity());
        productRepository.save(product);
        Order order=   Order.builder().
                customer(customer).
                product(product).
                quantity(orderRequestDTO.getQuantity()).
                price(product.getPrice() * orderRequestDTO.getQuantity()).
                status(OrderStatusEnum.CREATED.name())
                .build();
        orderRepository.save(order);


    }
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        // Map Order to OrderResponseDTO
        // Implement the mapping logic here and return the OrderResponseDTO

        return OrderResponseDTO.builder().id(order.getId())
                .customerId(order.getCustomer().getId())
                .productId(order.getProduct().getId())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .orderStatus(order.getStatus())
                .build();

    }

    @CachePut(value = "orders", key = "#id")
    @Override
    public String updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPrice(orderRequestDTO.getPrice());
        order.setQuantity(orderRequestDTO.getQuantity());
        orderRepository.save(order);
        return "Order updated successfully";
    }

    @CacheEvict(value = "orders", key = "#id")
    @Override
    public String deleteOrderById(Long id) {
        if(orderRepository.existsById(id)){
            orderRepository.deleteById(id);
            return "Order deleted successfully";
        }
        return "";
    }
}
