package com.ferrientregas.evidence.exception;

public class EvidenceNotFoundException extends RuntimeException {
    public EvidenceNotFoundException(String message) {
        super("Evidence not found: " + message);
    }
}
