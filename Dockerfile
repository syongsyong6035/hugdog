# 1. 베이스 이미지 (AWS Amazon Corretto 17 사용)
FROM amazoncorretto:17

# 2. 작업 폴더 설정
WORKDIR /app

# 3. 빌드된 JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 4. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]