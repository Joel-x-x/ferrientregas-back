package com.ferrientregas.delivery;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deliveries")
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false,
            columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    private String numeration;
    private String invoiceNumber;
    private String deliveryDate;
    private LocalTime estimateHourInit;
    private LocalTime estimateHourEnd;

    @ManyToOne
    @JoinColumn(name = "delivery_status_id", nullable = false)
    private DeliveryStatusEntity deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "payment_type_id", nullable = false)
    private PaymentTypeEntity paymentType;
    private Double credit;
    private Double total;

    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvidenceEntity> evidence;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;
    private String deliveryData;
    private String observations;
    private String comments;
}
