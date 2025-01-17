package com.demo.exceptionhandler;

import com.demo.exceptionhandler.error.CommonError;
import com.demo.exceptionhandler.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ExceptionController {

    @GetMapping("exception")
    public ResponseEntity<String> exception() {
        throw new BaseException(CommonError.INVALID_INPUT_VALUE);

    }
}
