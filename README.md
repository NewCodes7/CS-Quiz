<div align="center">
  <h1>CS Quiz</h1>

<img width="1419" alt="Untitled" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/51c85ce4-ffea-4e59-9133-14c92fdaa10a">
<img width="1421" alt="Untitled 1" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/bb861056-7efd-4c75-9a80-320c7b72b52a">

</div>

## 📋 목차

<!-- TOC -->
* [CS Quiz](#cs-quiz)
  * [🔗 프로젝트 노션](#-프로젝트-노션)
  * [📋 목차](#-목차)
  * [🎯 핵심 기능](#-핵심-기능)
    * [기초적인 CS 빈칸 문제를 풀어보세요](#기초적인-cs-빈칸-문제를-풀어보세요)
    * [CS 문제를 추가해보세요](#cs-문제를-추가해보세요)
  * [💫 부가 기능](#-부가-기능)
    * [풀이 기록이 저장돼요](#풀이-기록이-저장돼요)
    * [원하는 문제를 찾을 수 있어요](#원하는-문제를-찾을-수-있어요)
  * [🛠️ 기술 스택](#-기술-스택)
    * [사용 기술](#사용-기술)
    * [기타 사용 기술](#기타-사용-기술)
  * [시스템 아키텍처](#시스템-아키텍처)
<!-- TOC -->

## 🔗 [프로젝트 노션](https://www.notion.so/CS-Quiz-9eb241a1943c4f1eb09d6515fe652984?pvs=21)

- [문제 해결 경험](https://same-treatment-dcb.notion.site/684b91f13ebf4ab1b9bd1435d476c32b)
- [기획서](https://same-treatment-dcb.notion.site/7d085a060d9044de8bd5d8438513ff92?pvs=4)
- [데이터베이스 설계](https://same-treatment-dcb.notion.site/81c122573dbc408fb1f940ed4714f228?pvs=4)
- [api 명세서](https://same-treatment-dcb.notion.site/api-e1c0e207cc144b06bc6f216571627af4?pvs=4)

## ✍ 프로젝트 개요

CS Quiz는 빈칸 문제를 통해 컴퓨터 과학 기초 지식 학습을 돕는 서비스입니다. 방대한 CS 지식 중에서 무엇을 알고 모르는지 알 수 있습니다. 

<br />

## 🎯 핵심 기능

### 기초적인 CS 빈칸 문제를 풀어보세요!

> 모든 빈칸이 맞았다면 정답 처리합니다. 

<img width="1424" alt="Untitled 5" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/4ebccc49-47bb-4317-b6fa-71dd1eb0324e">

- 틀린 경우

  <img width="677" alt="Untitled 6" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/2a8ea722-acb4-4000-a68b-3a97830799f9">

- 맞은 경우

  <img width="696" alt="Untitled 7" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/4dccd4b8-4908-4b98-849d-88319105f339">

  - 한글이 아닌 영어로 입력해도 정답 처리합니다. (ex. deadlock)
  - 띄어쓰기, 대문자, 소문자를 구분하지 않습니다. (ex. Deadlock)
  - 하나의 정답에 대해 여러 복수 정답이 존재합니다. (ex. 데드락, 교착상태)

### CS 문제를 추가해보세요!

> 새로운 CS 문제를 제안할 수 있습니다. 관리자의 검토가 완료되면 업로드됩니다.

<img width="962" alt="Untitled 8" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/188b4f19-a85c-4f98-ad05-ea6c014c5c06">

## 💫 부가 기능

### 풀이 기록이 저장돼요!

> 로그인을 하면 푼 문제가 저장됩니다.

<img width="1420" alt="Untitled 9" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/63fba967-9060-4e34-81ef-86025ddad33f">

### 원하는 문제를 찾을 수 있어요!

> 문제 유형에 따라 문제를 분류합니다.

<img width="1421" alt="Untitled 2" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/ee9afb99-dcb9-4736-9a9a-0cf7a6e42019">

> 풀이 상태에 따라 문제를 분류합니다.

<img width="1415" alt="Untitled 3" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/74e75ad2-3a97-4db7-86b6-dd03d321c1c0">

> 원하는 문제를 검색할 수 있습니다.

<img width="1417" alt="Untitled 4" src="https://github.com/NewCodes7/CS-Quiz/assets/123712285/5a1f483c-0a3f-41fe-8a61-8a38deb55acc">

## 🗓️ 기간

- 2024.04.26 ~ 2024.06 진행중

## 🛠️ 기술 스택

- Java 17
- SpringBoot `3.2.5`
- gradle `8.7`
- thymeleaf
- JDBC template
- MySQL
- JUnit5

## 🌈 업데이트 예정 목록

- 프로젝트 배포
  - 배포하여 운영하면서 사용자로부터 피드백 받기
- 프로젝트 컨텐츠 추가
  - OS, Network, DB를 중심으로 문제 업로드
- 프로젝트 기능 추가
  - OAuth 로그인/회원가입 기능
  - 마이 페이지 및 관리자 페이지
  - 정답 바로 보기 기능
  - 객관식 문제 풀이 기능