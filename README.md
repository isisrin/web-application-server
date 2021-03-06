# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* stream을 log나  println하면 한 번 읽혀졌기 때문에 다시 주워담을 수 없다. 왜 그런지는 잘 모르겠다! 조사해봐야곘다..

### 요구사항 2 - get 방식으로 회원가입
* 

### 요구사항 3 - post 방식으로 회원가입
* 아래와 같이 입력하면 stream의 첫번째로 돌아갈 수 있다! ㅇㅁㅇ!
```
  int BUFFER_SIZE = 1000;

  buf.mark(BUFFER_SIZE);
  buf.readLine();  // returns the GET
  buf.readLine();  // returns the Host header
  buf.reset();     // rewinds the stream back to the mark
  buf.readLine();  // returns the GET again

```
### 요구사항 4 - redirect 방식으로 이동
* Location 과 Content-Location의 차이!
```
Location이 리다이렉션의 대상(혹은 새롭게 만들어진 문서의 URL)을 가르키는데 반해,
Content-Location은 더 이상의 컨텐츠 협상없이, 리소스 접근에 필요한 직접적인 URL을 가르킵니다.
Location은 응답과 연관된 헤더인데 반해, Content-Location 은 반환된 개체와 연관이 있습니다.
```
POST일 때에는 Location이로군!

* static 함수가 깔린 Class는 abstract를 써주자!
아니면 아래와 같이 쓸 수 있기 때문
```
A a = new A();
a.method() // ---- X
```
### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
*

### 리팩토링 하면서 알게 된 점
* java8이후에 interface에 static을 붙일 수 있게 되었다!
 ```
 default 메소드
 - interface내에서 메소드 구현이 가능
 - implements한 클래스에서 재정의 가능

 static 메소드
 - interface내에서 메소드 구현 가능
 - 재정의 불가
```