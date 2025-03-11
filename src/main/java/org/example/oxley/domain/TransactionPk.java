package org.example.oxley.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class TransactionPk implements Serializable {

    String manufacturer;
    String retailerId;
    String productCode;
    UUID transactionId;

    @Override
    public boolean equals(Object o) {
        if  (this == o) {
            return true;
        }
        if (!(o instanceof TransactionPk other)) {
            return false;
        }

        return  (this.manufacturer.equals(other.manufacturer) &&
                this.retailerId.equals(other.retailerId) &&
                this.productCode.equals(other.productCode) &&
                this.transactionId.equals(other.transactionId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturer, retailerId, productCode, transactionId);
    }

}
