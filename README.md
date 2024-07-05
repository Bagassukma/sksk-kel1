
### Cara Menjalankan Program

* Jalankan database migration tools
```bash
./mvnw clean flyway:migrate -Dflyway.configFiles=tools/db/migrations.conf
```

* Jalankan aplikasi
```bash
./mvnw spring-boot:run 
```