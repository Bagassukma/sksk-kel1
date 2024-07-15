
### Cara Menjalankan Program

### Install JAVA JDK
* Download pada website
Java JDK 21 -> [JavaJDK](https://www.oracle.com/id/java/technologies/downloads/#java21)

### Simpan sebagai Java HOME di Environment
* Di Windows Search env lalu plih environment variable
* Pilih path dan copy folder dari java jdk yang sudah di install .bin file,
* Jangan Lupa buat Variable JAVA_HOME Paste di variable value lalu simpan,
* Variable Path juga diisi dengan file bin dari java jdk juga dan simpan.

### Install Maven
* Install Maven bisa dari sini -> [Maven](https://maven.apache.org/install.html)

### Migration Dari GitHub
* Jalankan database migration tools
```bash
./mvn clean flyway:migrate -Dflyway.configFiles=tools/db/migrations.conf
```
### Perintah Untuk Running Program ke Postman
* Jalankan aplikasi
```bash
./mvn spring-boot:run 
```

### Hasil Running dengan Postman
Postman Docs -> [Postman](https://documenter.getpostman.com/view/36769762/2sA3kPq4gs)

### Dokumentasi
Markdown Doc -> [Docs](https://github.com/Bagassukma/sksk-kel1/tree/Auctionsksk/docs/Lelang)