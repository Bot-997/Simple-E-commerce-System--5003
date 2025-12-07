# Vue 3 E-commerce Frontend

Introduction
This is a modern e-commerce frontend project built with Vue 3 and Element Plus. Designed primarily for PC browsers, it serves as an excellent template for student group projects or entry-level developers. It covers the complete core flow of an e-commerce system, including product browsing, SKU selection, shopping cart management, order placement, and user center.

## Tech Stack
1. Core Framework: Vue 3 

2. Build Tool: Vite

3. UI Library: Element Plus

4. State Management: Pinia

5. Routing: Vue Router 4

6. HTTP Client: Axios


## Project Setup
1. Install Dependencies
```console
npm install
```
2. Run Development Server
```console
npm run dev
```
The app will typically run at http://localhost:5173.

3. Build for Production
```console
npm run build
```

## Project Structure
```
src/
├── api/             # API definition (Axios requests)
├── assets/          # Static assets (Images, CSS)
├── components/      # Reusable components (NavBar, ProductCard)
├── router/          # Router configuration
├── stores/          # Pinia state management (User, Cart, Wishlist, Product)
├── utils/           # Utilities (Axios interceptors)
├── views/           # Page views
│   ├── HomeView.vue        # Homepage
│   ├── ProductDetail.vue   # Product Details (SKU Logic)
│   ├── CartView.vue        # Shopping Cart
│   ├── CheckoutView.vue    # Order Checkout
│   ├── OrderListView.vue   # My Orders
│   ├── OrderDetailView.vue # Order Details
│   ├── UserProfileView.vue # User Center (Address/Password)
│   ├── WishlistView.vue    # Wishlist
│   └── LoginView.vue       # Login/Register
└── App.vue          # Root Component
```

Default Base URL: http://localhost:8080/api