# BIDDING API Spec

### 1. Register Buyer

POST http://127.0.0.1:8080/secured/user/register-buyer

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
  "name":"Test Pembeli",
  "email":"testpembeli@sksk.id",
  "password":"passbuyer"
}
```

### 2. Create Bidding

POST http://127.0.0.1:8080/secured/auction/list/12

Request Headers

Authorization =
```json lines
Bearer <YOUR TOKEN>
```

Body

```json
{
  "bid" : 10001,
  "bidder" : 20
}
```