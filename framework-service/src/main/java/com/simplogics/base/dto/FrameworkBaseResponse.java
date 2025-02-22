package com.simplogics.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FrameworkBaseResponse {

    private Boolean status;
    private Object data;
    private String message;
    private Boolean hasErrors;
    private List<String> errors;

}
