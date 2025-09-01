# API Endpoints

Base URL: https://e-commerce-api-19yj.onrender.com

## Users
- GET    /api/users

## Products
- GET    /api/products
- GET    /api/products/{id}
- POST   /api/products
- PUT    /api/products/{id}
- DELETE /api/products/{id}

## Cart
- GET    /api/cart/items?userId={userId}
- POST   /api/cart/add
- PUT    /api/cart/update
- DELETE /api/cart/remove

## Orders
- POST   /api/orders/checkout
- GET    /api/orders/history?userId={userId}  
