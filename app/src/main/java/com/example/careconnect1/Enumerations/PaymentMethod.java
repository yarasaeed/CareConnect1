package com.example.careconnect1.Enumerations;

import androidx.annotation.NonNull;

public enum PaymentMethod {


    DEBIT{
        @NonNull
        @Override
        public String toString() {
            return "debit card";
        }
    },
    PAYPAL{
        @NonNull
        @Override
        public String toString() {
            return "paypal";
        }
    },
    CREDIT{
        @NonNull
        @Override
        public String toString() {
            return "credit card";
        }
    },
    CASH{
        @NonNull
        @Override
        public String toString() {
            return "cash";
        }
    }
}
