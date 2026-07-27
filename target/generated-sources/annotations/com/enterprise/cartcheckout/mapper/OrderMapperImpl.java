package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.AddressResponse;
import com.enterprise.cartcheckout.dto.response.OrderResponse;
import com.enterprise.cartcheckout.entity.Address;
import com.enterprise.cartcheckout.entity.Order;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-25T15:06:36+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.billingAddress( addressToAddressResponse( order.getBillingAddress() ) );
        orderResponse.couponCode( order.getCouponCode() );
        orderResponse.createdAt( order.getCreatedAt() );
        orderResponse.discount( order.getDiscount() );
        orderResponse.grandTotal( order.getGrandTotal() );
        orderResponse.id( order.getId() );
        orderResponse.invoiceNumber( order.getInvoiceNumber() );
        orderResponse.items( orderItemListToOrderItemResponseList( order.getItems() ) );
        orderResponse.orderNumber( order.getOrderNumber() );
        orderResponse.orderStatus( order.getOrderStatus() );
        orderResponse.orderTimeline( orderStatusHistoryListToOrderStatusHistoryResponseList( order.getOrderTimeline() ) );
        orderResponse.paymentMethod( order.getPaymentMethod() );
        orderResponse.paymentStatus( order.getPaymentStatus() );
        orderResponse.shippingAddress( addressToAddressResponse( order.getShippingAddress() ) );
        orderResponse.shippingCharge( order.getShippingCharge() );
        orderResponse.subTotal( order.getSubTotal() );
        orderResponse.tax( order.getTax() );
        orderResponse.trackingNumber( order.getTrackingNumber() );
        orderResponse.userId( order.getUserId() );

        return orderResponse.build();
    }

    protected AddressResponse addressToAddressResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponse.AddressResponseBuilder addressResponse = AddressResponse.builder();

        addressResponse.addressLine1( address.getAddressLine1() );
        addressResponse.addressLine2( address.getAddressLine2() );
        addressResponse.city( address.getCity() );
        addressResponse.country( address.getCountry() );
        addressResponse.defaultAddress( address.isDefaultAddress() );
        addressResponse.fullName( address.getFullName() );
        addressResponse.id( address.getId() );
        addressResponse.phone( address.getPhone() );
        addressResponse.postalCode( address.getPostalCode() );
        addressResponse.state( address.getState() );

        return addressResponse.build();
    }

    protected OrderResponse.OrderItemResponse orderItemToOrderItemResponse(Order.OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderResponse.OrderItemResponse.OrderItemResponseBuilder orderItemResponse = OrderResponse.OrderItemResponse.builder();

        orderItemResponse.price( orderItem.getPrice() );
        orderItemResponse.productId( orderItem.getProductId() );
        orderItemResponse.productName( orderItem.getProductName() );
        orderItemResponse.quantity( orderItem.getQuantity() );
        orderItemResponse.sku( orderItem.getSku() );
        orderItemResponse.taxRate( orderItem.getTaxRate() );

        return orderItemResponse.build();
    }

    protected List<OrderResponse.OrderItemResponse> orderItemListToOrderItemResponseList(List<Order.OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderResponse.OrderItemResponse> list1 = new ArrayList<OrderResponse.OrderItemResponse>( list.size() );
        for ( Order.OrderItem orderItem : list ) {
            list1.add( orderItemToOrderItemResponse( orderItem ) );
        }

        return list1;
    }

    protected OrderResponse.OrderStatusHistoryResponse orderStatusHistoryToOrderStatusHistoryResponse(Order.OrderStatusHistory orderStatusHistory) {
        if ( orderStatusHistory == null ) {
            return null;
        }

        OrderResponse.OrderStatusHistoryResponse.OrderStatusHistoryResponseBuilder orderStatusHistoryResponse = OrderResponse.OrderStatusHistoryResponse.builder();

        orderStatusHistoryResponse.description( orderStatusHistory.getDescription() );
        orderStatusHistoryResponse.status( orderStatusHistory.getStatus() );
        orderStatusHistoryResponse.timestamp( orderStatusHistory.getTimestamp() );

        return orderStatusHistoryResponse.build();
    }

    protected List<OrderResponse.OrderStatusHistoryResponse> orderStatusHistoryListToOrderStatusHistoryResponseList(List<Order.OrderStatusHistory> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderResponse.OrderStatusHistoryResponse> list1 = new ArrayList<OrderResponse.OrderStatusHistoryResponse>( list.size() );
        for ( Order.OrderStatusHistory orderStatusHistory : list ) {
            list1.add( orderStatusHistoryToOrderStatusHistoryResponse( orderStatusHistory ) );
        }

        return list1;
    }
}
