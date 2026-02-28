package com.server.userservice.exception;

import com.server.userservice.domain.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends RuntimeException {

    private ResultCode resultCode;

    public BadRequestException(ResultCode resultCode) {
        super(resultCode.message());
        this.resultCode = resultCode;
    }

    public BadRequestException(ResultCode resultCode,String message) {
        super(message);
        this.resultCode = resultCode;
    }
}
