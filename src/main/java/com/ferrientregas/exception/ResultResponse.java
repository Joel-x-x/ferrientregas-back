package com.ferrientregas.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResultResponse <T,E>{
    private T result;
    private boolean isSuccess;
    private List<E> errors;
    private int code;

    public static <T,E> ResultResponse <T,E> success(T result, int code){
        return new ResultResponse<>(result,true,null,code);
    }

    public static <T,E> ResultResponse <T,E> failure(List<E> errors, int code){
        return new ResultResponse<>(null, false, errors,code);
    }

    public static <T,E> ResultResponse <T,E> failure(T result, List<E> errors, int code){
       return new ResultResponse<>(result,false,errors,code);
    }
}
