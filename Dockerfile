FROM maven:3.9.6-amazoncorretto-21

WORKDIR /app

ADD pom.xml .

RUN mvn clean verify --fail-never

ARG spring_profiles_active=default
ARG diff_publisher_bootstrap_servers
ARG diff_publisher_topic_name=diff_stream
ARG mongo_url
ARG mongo_username
ARG mongo_password
ARG mongo_dbname

ENV spring_profiles_active=${spring_profiles_active}
ENV diff_publisher_bootstrap-servers=${diff_publisher_bootstrap_servers}
ENV diff_publisher_topic-name=${diff_publisher_topic_name}
ENV MONGO_URL=${mongo_url}
ENV MONGO_USERNAME=${mongo_username}
ENV MONGO_PASSWORD=${mongo_password}
ENV MONGO_DBNAME=${mongo_dbname}

COPY . .
RUN mvn clean package

EXPOSE 8000

ENTRYPOINT ["java", "-jar", \
        "-Dspring.profiles.active=${spring_profiles_active}", \
        "-Ddiff.publisher.bootstrap-servers=${diff_publisher_bootstrap_servers}", \
        "-Ddiff.publisher.topic-name=${diff_publisher_topic-name}", \
        "target/comparator-0.0.1-SNAPSHOT.jar" \
    ]