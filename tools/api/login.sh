RESPONSE=$(curl -H 'Content-Type: application/json' -d '{"email":"charlie@example.com","password":"12345678"}' 'http://127.0.0.1:8080/login')
#RESPONSE=$(curl -H 'Content-Type: application/json' -d '{"email":"budi@sksk.id","password":"12345678"}' 'http://127.0.0.1:8080/login')
TOKEN=$(echo $RESPONSE | jq -r .data)
export SK_TOKEN=$TOKEN
echo $RESPONSE | jq
