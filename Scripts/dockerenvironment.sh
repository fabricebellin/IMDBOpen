# Run MariaDB
docker run -d --name MariDb \
  -e MARIADB_ROOT_PASSWORD=bl@dg3r$$ \
  -e MARIADB_DATABASE=ImDbOpenDB \
  -e MARIADB_USER=myuser \
  -e MARIADB_PASSWORD=bl@dg3r$$ \
  -p 3306:3306 \
  -v "B:/MariaDBData:/var/lib/mysql" \
  mariadb:10.8  # Stable version

# Run Java 21 Container
docker run -d --name TestSpring \
  -p 8080:8080 \
  -v "B:/JavaApp:/usr/src/app" \
  openjdk:21-jdk-slim  # Using Java 21
