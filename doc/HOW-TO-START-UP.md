### 1. Configure MySQL Docker on the server
```
docker pull nexus.sbisec.int:8442/mysql:8.0.30
docker run --name bo-mysql -p <PORT>:3306 -e MYSQL_ROOT_PASSWORD=<PASSWORD> -d nexus.sbisec.int:8442/mysql:8.0.30
```

### 2. Configure the SSH channel on the local command line

```
ssh -L <PORT>:localhost:<PORT> your_server_name_or_ip -Nf 
```
Example:
```
ssh -L 5430:localhost:5430 m1-bits-jpdl101.ad.sbibits.com -Nf 
```
### 3. Create database `bo-seed`

```
create database `bo-seed`;
```

### 4. Clone the code and import it to IDEA

```
git clone https://gitlab.cn.sbibits.com/vc-future/vc-bo-seed/bo-java-seed
```

### 5. Modify the DB configuration file of Flyway of bo-java-seed:
`shared/flyway/src/main/resources/flyway.conf`

Example:
```
flyway.user=root
flyway.password=<PASSWORD>
flyway.schemas=bo-seed
flyway.url=jdbc:mysql://localhost:<PORT>/bo-seed?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Tokyo
flyway.locations=filesystem:shared/flyway/src/main/resources/migration
flyway.outOfOrder=true
```

### 6. Execute the Flyway command to create a table

```
./gradlew flywayMigrate
```

### 7. Modify the database connection information in the configuration file backoffice/src/main/resources/application-dev.yml. 

```yaml
spring:
  datasource:
    seed:
      type: com.zaxxer.hikari.HikariDataSource
      jdbc-url: jdbc:mysql://localhost:<PORT>/bo-seed?characterEncoding=utf8&useSSL=false
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: <PASSWORD>

```

### 8. Start the project in IDEA and visit swagger URL for verification.

http://localhost:8888/swagger-ui/index.html#

----


