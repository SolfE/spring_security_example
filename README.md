# Spring Security

Spring Security를 공부하면서, 현재 자주 사용되는 로그인 기능들을 직접 구현해보며 정리했습니다.
각 구현방식의 장단점이나 특징은 블로그에 정리해두겠습니다.

블로그
https://velog.io/@solfe/Session-JWT-Refresh-Token

## 목차

0. [Tech Stack](#Tech_Stack)
1. [Cookie](#Cookie)
2. [Session](#Session)
3. [Spring Security Form Login(Session)](#Form_Login)
4. [JWT](#Jason_Web_Token)
5. [JWT + Refresh Token](#Refresh_Token)
6. [OAuth](#OAuth)
7. [CI/CD](#CI_CD)
8[참고](#참고)

## Tech_Stack

FRAMEWORK

<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">


## Cookie

Spring Security 없이 사용자를 식별하고 인증하기 위해 user id를 cookie에 넣었습니다.

### sequence diagram

![image](https://github.com/SolfE/spring_security_example/assets/74342496/6a8b2b75-5175-4f70-9e8a-7b5810526283)

## Session

Spring Security 없이 사용자를 식별하고 인증하기 위해 Session id를 cookie에 넣었습니다.

### sequence diagram

![image](https://github.com/SolfE/spring_security_example/assets/74342496/201053f0-a5fd-48ee-9114-f1fb5a3493ca)


## Form_Login

Spring Security의 formLogin 설정을 활용한 로그인.


### sequence diagram

회원가입

![image](https://github.com/SolfE/spring_security_example/assets/74342496/ac1e1f4e-5ffb-4d02-9f71-d96aa21a2c2e)

인증&인가 (UsernamePasswordAuthenticationFilter를 통해서 지원)

![image](https://github.com/SolfE/spring_security_example/assets/74342496/6071e1e5-4b6a-4ac4-acac-d39757ac3958)


## Jason_Web_Token

JWT 방식을 활용한 로그인

### sequence diagram

![image](https://github.com/SolfE/spring_security_example/assets/74342496/ee7ae9ce-893c-441c-b3f1-cdf1a156573b)


## Refresh_Token

미구현


## OAuth

OAuth를 활용한 회원가입 및 로그인

### sequence diagram

kakao login

![image](https://github.com/SolfE/spring_security_example/assets/74342496/7a3640f8-4c97-4bc4-8f4a-39c28cec8aee)

이후 내부에서는 Spring Security Form Login(JSESSION) 인증인가 방식으로 진행

## CI_CD
![image](https://github.com/SolfE/spring_security_example/assets/74342496/609d7989-0c03-4e3a-acce-6915ab875737)

## 참고
https://chb2005.tistory.com/173

https://spring.io/projects/spring-security