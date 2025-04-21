package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;
import com.wholesaleshop.demo_wholesale_shop.entity.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {

    @Mappings({
            @Mapping(source = "orders.orderId", target = "orderId"),
            @Mapping(source = "product.productId", target = "productId")
    })
    OrderDetailsDto orderDetailsToOrderDetailsDto(OrderDetails orderDetails);

    @Mappings({
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "product", ignore = true)
    })
    OrderDetails orderDetailsDtoToOrderDetails(OrderDetailsDto orderDetailsDto);
}
