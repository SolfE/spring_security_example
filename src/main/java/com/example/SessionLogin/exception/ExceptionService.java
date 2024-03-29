package com.example.SessionLogin.exception;

import org.springframework.stereotype.Service;

@Service
public class ExceptionService {

    public void login() {
        throw new MyException(ErrorCode.USERNAME_NOT_FOUND);
    }

    public void writeComment() {
        throw new MyException(ErrorCode.INVALID_PERMISSION);
    }

    public void join() {
        throw new MyException(ErrorCode.DUPLICATED_USER_NAME);
    }

    public void editComment() {
        throw new MyException(ErrorCode.DATABASE_ERROR);
    }

    public void runExp() {
        throw new RuntimeException();
    }

}
