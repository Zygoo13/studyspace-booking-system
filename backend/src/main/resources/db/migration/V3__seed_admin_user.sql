INSERT INTO users (
    full_name,
    phone,
    email,
    password_hash,
    role,
    is_active,
    created_at,
    updated_at
)
SELECT
    'System Admin',
    '0123456789',
    'admin@studyspace.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'ADMIN',
    TRUE,
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE email = 'admin@studyspace.com'
);