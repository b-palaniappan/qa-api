# TODO List
- [x] Add Service class and method
- [x] Added Model Mapper
- [x] Add validation
- [ ] Security
- [ ] JWT
- [x] Global Exception handler. with validation error handler
- [x] REST Client
- [x] Move api key secret to Vault
- [x] Swagger UI for REST API.
- [ ] Add integration and unit testing

## Setup MongoDB in docker
```bash
docker run --rm --name local-mongo -p 27017:27017 -d mongo
```

## Generate RSA Keys
* Generate RSA Public Private key.
```bash
openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem
```
* Convert private key to PKCS#8
```bash
openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem
```

## Vault configuration
```bash
vault kv put secret/qa-service/api.hereMap api-key=<Here_map_api_key>
```

## Something's to remember
* store JWTs inside an httpOnly cookie
* OWASP docs on [jwt](https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.md)
