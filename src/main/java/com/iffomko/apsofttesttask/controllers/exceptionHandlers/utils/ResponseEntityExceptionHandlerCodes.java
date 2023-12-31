package com.iffomko.apsofttesttask.controllers.exceptionHandlers.utils;

public enum ResponseEntityExceptionHandlerCodes {
    HTTP_REQUEST_METHOD_NOT_SUPPORTED,
    HTTP_MEDIA_TYPE_NOT_SUPPORTED,
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE,
    MISSING_PATH_VARIABLE,
    MISSING_SERVLET_REQUEST_PARAMETER,
    MISSING_SERVLET_REQUEST_PART,
    MISSING_SERVLET_BINDING_EXCEPTION,
    METHOD_ARGUMENT_NOT_VALID,
    NO_HANDLER_FOUND_EXCEPTION,
    ASYNC_REQUEST_TIMEOUT_EXCEPTION,
    ERROR_RESPONSE_EXCEPTION,
    CONVERSION_NOT_SUPPORTED,
    TYPE_MISMATCH,
    HTTP_MESSAGE_NOT_READABLE,
    HTTP_MESSAGE_NOT_WRITABLE,
    SIZE_LIMIT_EXCEEDED_EXCEPTION;
}
