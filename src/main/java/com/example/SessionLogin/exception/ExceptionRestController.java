package com.example.SessionLogin.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exception-example")
@RequiredArgsConstructor
public class ExceptionRestController {

    private final ExceptionService exceptionService;

    @GetMapping("/throw-my-exception/1")
    public void throwMyException1() {
        exceptionService.login();
    }

    @GetMapping("/throw-my-exception/2")
    public void throwMyException2() {
        exceptionService.writeComment();
    }

    @GetMapping("/throw-my-exception/3")
    public void throwMyException3() {
        exceptionService.join();
    }

    @GetMapping("/throw-my-exception/4")
    public void throwMyException4() {
        exceptionService.editComment();
    }

    @GetMapping("/throw-my-exception/run-tim")
    public void throwMyExcetion5() {
        exceptionService.runExp();
    }

     // ExceptionRestController.class 내부에서 MyException.class 에러가 발생하면 해당 핸들러 호출
     @ExceptionHandler(MyException.class)
     public ResponseEntity<?> myExceptionHandler(MyException e) {
         e.printStackTrace();
         return ResponseEntity.status(e.getErrorCode().getStatus())
                 .body(new ExceptionDto(e.getErrorCode()));
     }
}
