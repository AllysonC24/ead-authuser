package com.ead.authuser.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int errorCode;
    private String errorMessage;
    private Map<String, String> errorDetails = new HashMap<>();

}
