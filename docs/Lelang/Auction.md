# AUCTION API Spec

### 1. Register Seller

POST http://127.0.0.1:8080/secured/user/register-seller

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
    "name":"penjualbaru",
    "email":"penjualbaru@sksk.id",
    "password":"sellerpassword"
}
```

### 2. Create Auction

POST http://127.0.0.1:8080/secured/auction/create

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
  "name": "COBA LELANG TANAH 1 HEKTAR",
  "description": "Lelang TANAH 1 HEKTAR",
  "minimumPrice": 1000,
  "startedAt": "2024-07-15T12:00:00+07:00",
  "endedAt": "2024-07-16T12:00:00+07:00"
}
```

### 3. List Auction (ALL ROLE)

GET http://127.0.0.1:8080/secured/auction/list

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Query Params (page = 1, size = 10, name = kulkas)

### 4. List Auction By ID (ALL ROLE)

GET http://127.0.0.1:8080/secured/auction/list/12

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

### 5. Update Status Auction (ONLY ADMIN)

PUT http://127.0.0.1:8080/secured/auction/status

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body (IF APPROVED)

```json
{
    "id": 12,
    "status": "APPROVED"
}
```

Body (IF REJECTED)

```json
{
    "id": 12,
    "status": "REJECTED"
}
```

### 6. Update Status Auction Close (SELLER & ADMIN)

PUT http://127.0.0.1:8080/secured/auction/close

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
    "id": 12,
    "status": "CLOSED"
}
```