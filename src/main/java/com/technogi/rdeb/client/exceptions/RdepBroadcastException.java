package com.technogi.rdeb.client.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class RdepBroadcastException extends RdebRuntimeException {
    public RdepBroadcastException(Exception e) {
        super(e);
    }
}
