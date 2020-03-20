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
      "name": "some-name-1"
    },
    {
      "id": "some-id-2",
      "name": "some-name-2"
    }    
  ]
}
```
