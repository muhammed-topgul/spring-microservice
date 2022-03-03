package com.muhammedtopgul.application.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author muhammed-topgul
 * @since 03.03.2022 17:27
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ApplicationException extends RuntimeException {

    private final String message;
    private final Class<?> classType;
}
