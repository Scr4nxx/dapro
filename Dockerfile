FROM maven:3.8.4-openjdk-11-slim

# Install npm
RUN apt-get update && apt-get install -y npm

# Set the working directory
WORKDIR /app

# Set the entrypoint to run Maven
ENTRYPOINT ["mvn"]