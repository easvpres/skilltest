package org.eas.sberteh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author aesipov.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;
    private Long fromBankAccountNumber;
    private Long toBankAccountNumber;
    private BigInteger amount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        REGISTERED,
        IN_PROGRESS,
        DONE,
        FAILED
    }
}
