package com.ferrientregas.report;

import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.report.dto.DeliveryCountRequest;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ReportService {
    // TODO: REFACTORING WITH SPECIFICATION API
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;

    public Long countUsersByRole(String role) {
        if (!StringUtils.hasText(role)) {
            return this.userRepository.count();
        } else {
            return this.userRepository.countByRoles_Name(role);
        }
    }

    public Long countDeliveries(DeliveryCountRequest request) {
        if(request == null) {
            return this.deliveryRepository.count();
        }

        boolean hasDeliveryStatus = StringUtils.hasText(request.deliveryStatus());
        boolean hasPaymentType = StringUtils.hasText(request.paymentType());

        if (!hasDeliveryStatus && !hasPaymentType) {
            return this.deliveryRepository.count();
        } else if (hasDeliveryStatus && !hasPaymentType) {
            return this.deliveryRepository.countByDeliveryStatus_Name(request.deliveryStatus());
        } else if (!hasDeliveryStatus) {
            return this.deliveryRepository.countByPaymentType_Name(request.paymentType());
        } else {
            return this.deliveryRepository.countByDeliveryStatus_NameAndPaymentType_Name(
                    request.deliveryStatus(), request.paymentType()
            );
        }
    }

//    public Page<DeliveryResponse> getAllDeliveryByDate
}
