# USER API Spec

### 1. Login

POST http://localhost:8080/login

Body

```json
{
    "email": "charlie@example.com",
    "password": "satutujuan"
}
```


### 2. List User

GET http://127.0.0.1:8080/secured/user/list?page=1&size=10

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Query Params (page = 1, size = 10)


### 3. Get User

GET http://127.0.0.1:8080/secured/user/current

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

### 4. Update Profile

PUT http://127.0.0.1:8080/secured/user/update-profile

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
    "name" : "yener",
    "email" : "jener@sksk.id"
}
```

### 5. Reset Password

PUT http://localhost:8080/secured/user/reset-password

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
  "email":"test@sksk.id",
  "newPassword":"password"
}
```

### 6. Delete User (ONLY ADMIN)

DELETE http://localhost:8080/secured/user/delete-user

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
    "name": "Pembeli Baru"
}

```