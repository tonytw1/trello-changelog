FROM eclipse-temurin:21-jre-jammy
COPY target/universal/trello-changelog-1.0.zip /tmp
RUN apt-get install unzip
RUN /usr/bin/unzip /tmp/trello-changelog-1.0.zip
RUN ls -l /
RUN mv /trello-changelog-1.0 /usr/share/trello-changelog
CMD ["/usr/share/trello-changelog/bin/trello-changelog"]
