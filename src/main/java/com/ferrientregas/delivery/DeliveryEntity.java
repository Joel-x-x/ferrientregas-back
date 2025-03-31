package com.ferrientregas.delivery;

import com.ferrientregas.audit.Auditable;
import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "deliveries")
public class DeliveryEntity extends Auditable {

    private String numeration;
    private String invoiceNumber;
    private LocalDate deliveryDate;
    private LocalTime estimateHourInit;
    private LocalTime estimateHourEnd;

    @ManyToOne
    @JoinColumn(name = "delivery_status_id", nullable = false)
    private DeliveryStatusEntity deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "payment_type_id", nullable = false)
    private PaymentTypeEntity paymentType;
    private BigDecimal credit = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvidenceEntity> evidence;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private CustomerEntity customer;

    private String deliveryData;
    private String observations;
    private String comments;

    @PrePersist
    public void onCreate(){
       this.setCreatedAt(now());
       this.setUpdatedAt(now());
       this.setDeletedAt(now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(now());
        if(this.isDeleted()) this.setDeletedAt(now());
    }
}
