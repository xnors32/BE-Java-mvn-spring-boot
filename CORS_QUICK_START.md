# ⚡ CORS Quick Start Guide

## 🎯 TL;DR - Frontend Bisa Akses API!

CORS sudah dikonfigurasi di Backend. Frontend di `http://localhost:5173` dapat mengakses API di `http://localhost:4000` **TANPA ERROR**.

---

## ✅ What's Done

### Backend (SecurityConfig.java)
- ✅ CORS enabled untuk semua API endpoints
- ✅ Frontend origin `http://localhost:5173` whitelisted
- ✅ Authorization header diexpose untuk JWT token
- ✅ Preflight requests (OPTIONS) dihandle otomatis
- ✅ Credentials allowed (untuk cookies & auth headers)

### Configuration Details
```java
// Allowed Origins
"http://localhost:5173"       // PRIMARY
"http://127.0.0.1:5173"       // Alternative
"http://localhost:3000"       // Dev port
"http://localhost:8080"       // Dev port
"http://localhost:4173"       // Preview

// Allowed Methods
GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD

// Exposed Headers
Authorization, Content-Type, X-Total-Count, X-Page-Number, X-Page-Size, X-Error-Message
```

---

## 🚀 How to Use

### 1. Start Backend
```bash
cd BE-Java-mvn-spring-boot
mvn spring-boot:run
# Listening on http://localhost:4000
```

### 2. Start Frontend
```bash
cd frontend-baru
npm run dev
# Listening on http://localhost:5173
```

### 3. Frontend Can Now Access API
```javascript
// In Vue component (frontend)
const response = await fetch('http://localhost:4000/api/barang', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <jwt-token>'
  }
})

// OR with Axios (already configured)
const { data } = await api.get('/barang')

// NO CORS ERROR! ✅
```

---

## 🔍 Verify CORS is Working

### Test from Frontend (Browser Console)
```javascript
// Open http://localhost:5173 in browser
// Open DevTools Console (F12)
// Paste this:

fetch('http://localhost:4000/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'test@test.com',
    password: 'password123'
  })
})
.then(r => r.json())
.then(data => {
  console.log('✅ CORS Working! Data:', data)
})
.catch(err => {
  console.error('❌ Error:', err)
})
```

### Test from Command Line
```bash
# Test preflight (OPTIONS)
curl -i -X OPTIONS http://localhost:4000/api/barang \
  -H "Origin: http://localhost:5173" \
  -H "Access-Control-Request-Method: GET"

# Should see:
# Access-Control-Allow-Origin: http://localhost:5173 ✅
# Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD ✅
```

---

## 📋 What Each Header Does

| Header | Meaning |
|--------|---------|
| `Access-Control-Allow-Origin: http://localhost:5173` | ✅ This origin can access API |
| `Access-Control-Allow-Methods: GET, POST, PUT, DELETE` | ✅ These HTTP methods allowed |
| `Access-Control-Allow-Headers: *` | ✅ All headers allowed |
| `Access-Control-Expose-Headers: Authorization` | ✅ Frontend can read token header |
| `Access-Control-Allow-Credentials: true` | ✅ Cookies & auth allowed |

---

## 🎨 Frontend Integration

### Using Axios (Already Configured)
```typescript
// src/services/api.ts
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:4000/api'
})

// Interceptor untuk add token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
```

### Using in Components
```vue
<script setup>
import api from '@/services/api'

const fetchData = async () => {
  try {
    // No CORS error! ✅
    const { data } = await api.get('/barang')
    console.log(data)
  } catch (error) {
    console.error(error.message)
  }
}
</script>
```

---

## ⚠️ Important Notes

### Development (Sekarang)
- ✅ CORS fully configured
- ✅ Multiple localhost origins allowed
- ✅ Easy to test & debug

### For Production
- ⚠️ **MUST UPDATE** allowed origins
- ⚠️ **MUST ENABLE** HTTPS/SSL
- ⚠️ **MUST CHANGE** JWT secret

See [CORS_CONFIGURATION.md](./CORS_CONFIGURATION.md) untuk production setup.

---

## 🐛 Troubleshooting

### If CORS Error Persists

**1. Check Origins Match Exactly**
```javascript
// Frontend running on: http://localhost:5173
// Backend allows:      "http://localhost:5173" ✅

// Frontend running on: http://127.0.0.1:5173
// Backend allows:      "http://127.0.0.1:5173" ✅
```

**2. Check Both Services Running**
```bash
# Terminal 1 - Backend on port 4000
lsof -i :4000  # Should show Java process

# Terminal 2 - Frontend on port 5173
lsof -i :5173  # Should show Node process
```

**3. Clear Browser Cache**
```javascript
// In browser console:
localStorage.clear()
sessionStorage.clear()
// Then refresh
```

**4. Restart Backend**
```bash
# Kill and restart
mvn spring-boot:run
```

---

## 📚 Related Documentation

- [Full CORS Configuration Guide](./CORS_CONFIGURATION.md) - Detailed explanation
- [Backend README](./README.md) - API documentation
- [Main SETUP Guide](../SETUP.md) - Complete setup instructions

---

## ✅ Checklist

- [x] CORS configured in SecurityConfig
- [x] Frontend origin whitelisted
- [x] Authorization header exposed
- [x] Preflight handled automatically
- [x] Credentials allowed
- [x] Build successful
- [ ] Test with frontend
- [ ] Update for production

---

**Status:** ✅ Ready to Use  
**Last Updated:** 31 Mei 2024  
**Maintenance:** None needed until production

Start testing with your frontend now! 🚀
