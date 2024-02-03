### 백엔드 <https://github.com/SWE-CTF/backend>

먼저, 해당 레포지토리에서 프로젝트를 받고 시작하세요.

1. develop 브랜치에서 프로젝트를 받아오세요.
   ```
   git clone -b develop --single-branch https://github.com/SWE-CTF/backend.git
   ```
2. 다음 디렉토리로 이동합니다.
   ```
   cd /backend/src/main
   ```
3. 해당 위치에 resources 폴더를 생성합니다.
   ```
   mkdir resources
   ```
4. resources 폴더에 application.properties를 생성합니다.
5. 다음의 설정중 사용자의 편의에 맞게 수정해주세요.
   ```
   spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver
   spring.datasource.url = jdbc:mysql://데이터베이스IP주소/데이터베이스이름
   spring.datasource.username = 데이터베이스 접속 아이디
   spring.datasource.password = 데이터베이스 접속 비밀번호

   spring.jpa.hibernate.ddl-auto = (none, validate, update, create, create-drop)
   spring.jpa.generate.ddl = (true, false)
   spring.jpa.show-sql = (true, false)
   spring.jpa.properties.hibernate.show_sql = (true, false)
   spring.jpa.properties.hibernate.format_sql = (true, false)
   spring.mvc.pathmatch.matching-strategy = ant_path_matcher

   user.file.path = 사진 파일 저장 경로
   code.storage.path = 코드 파일 저장 경로

   jwt.secret.key = 256bit의 문자열
   ```
6. 설정이 완료되면 CtfApplication을 실행합니다.
7. 웹 백엔드 서버가 켜지고 프론트에서 접속한 사이트로 사용하면 됩니다.


### Prerequisites / 선행 조건

아래 사항들이 설치가 되어있어야합니다.

```
Node 18.17.1 이상, JDK 11 (temurin), mysql 8.0.35-0 이상
```
