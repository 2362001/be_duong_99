Request vào
│
▼
┌─────────────────────────────────────────────┐
│ Lớp 1: WHITELIST                            │
│ → accessControlService.isWhiteList(uri)     │
│ → Nếu match → ✅ Cho qua (không cần token) │
└──────────────┬──────────────────────────────┘
│ Không match
▼
┌─────────────────────────────────────────────┐
│ Lớp 2: AUTHENTICATED                       │
│ → accessControlService.isAuthenticated(uri) │
│ → Nếu match + đã login → ✅ Cho qua        │
│ → Nếu match + chưa login → ❌ 401          │
└──────────────┬──────────────────────────────┘
│ Không match
▼
┌─────────────────────────────────────────────┐
│ Lớp 3: RBAC (Role-Based Access Control)     │
│ → Chưa login? → ❌ 401                     │
│ → Có role ADMIN? → ✅ Bypass hoàn toàn      │
│ → Lấy actionCodes từ DB (role_mapping)      │
│ → accessControlService.isAuthorized()?      │
│   → Có quyền → ✅ 200                      │
│   → Không quyền → ❌ 403                   │
└─────────────────────────────────────────────┘

đây là luồng của file AccesControlFilter