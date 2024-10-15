FROM maven:3.9.6-amazoncorretto-21

WORKDIR /app

ADD pom.xml .

RUN mvn clean verify --fail-never

COPY . .
RUN mvn clean package

EXPOSE 8000

ENTRYPOINT ["java", "-jar", \
        "-Dspring.profiles.active=${spring_profiles_active}", \
        "-Ddiff.publisher.bootstrap-servers=${diff_publisher_bootstrap_servers}", \
        "-Ddiff.publisher.topic-name=${diff_publisher_topic_name}", \
        "target/comparator-0.0.1-SNAPSHOT.jar" \
    ]