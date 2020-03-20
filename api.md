# API

## List all shops

Request:
```
GET /api/shop?zip=12345
```

Response:
```json
{
  "shops": [
    {
      "id": "some-id-1",
      "name": "some-name-1",
      "distance": 5.3,
      "supportedContactTypes": [
        "telephone", "whatsapp"
      ]
    },
    {
      "id": "some-id-2",
      "name": "some-name-2",
      "distance": 7.3,
      "supportedContactTypes": [
        "facetime"
      ]
    }    
  ]
}
```

## Get shop details

Request:
```
GET /api/shop/{id}
```

Response:
```json
{
  "id": "some-id-1",
  "name": "some-name-1",
  "description": "some-description-1",
  "email": "some@email"
}
```
