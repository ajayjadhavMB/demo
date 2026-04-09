# Sample Product API (MVC Flow)

Base URL: `http://localhost:8010/api/v1/products`

## 1. Create Product

```bash
curl -X POST 'http://localhost:8010/api/v1/products' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Laptop Pro 14",
    "description": "High performance laptop",
    "sku": "LAP-12345",
    "price": 1299.99,
    "status": "ACTIVE"
  }'
```

## 2. Get Product By Id

```bash
curl 'http://localhost:8010/api/v1/products/1'
```

## 3. Search Products (keyword + status + pagination)

```bash
curl 'http://localhost:8010/api/v1/products?keyword=laptop&status=ACTIVE&page=0&size=10'
```

## 4. Update Product

```bash
curl -X PUT 'http://localhost:8010/api/v1/products/1' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Laptop Pro 14 Gen 2",
    "description": "Updated model",
    "price": 1399.99,
    "status": "ACTIVE"
  }'
```

## 5. Delete Product

```bash
curl -X DELETE 'http://localhost:8010/api/v1/products/1'
```

## Notes

- SKU format validation: `AAA-12345` (e.g. `LAP-12345`).
- Global exception handling is implemented with structured error response.
- Filtering uses JPA `Specification` (`nameContains`, `hasStatus`).
