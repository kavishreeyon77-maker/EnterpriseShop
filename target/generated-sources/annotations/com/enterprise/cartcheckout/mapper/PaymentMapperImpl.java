package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.PaymentResponse;
import com.enterprise.cartcheckout.entity.Payment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-25T15:06:36+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toResponse(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentResponse.PaymentResponseBuilder paymentResponse = PaymentResponse.builder();

        paymentResponse.amount( payment.getAmount() );
        paymentResponse.id( payment.getId() );
        paymentResponse.orderId( payment.getOrderId() );
        paymentResponse.paidAt( payment.getPaidAt() );
        paymentResponse.paymentMethod( payment.getPaymentMethod() );
        paymentResponse.status( payment.getStatus() );
        paymentResponse.transactionId( payment.getTransactionId() );

        return paymentResponse.build();
    }
}
