INSERT INTO branches (
    name,
    address,
    is_active,
    created_at,
    updated_at
)
SELECT
    'StudySpace Main Branch',
    'Default address',
    TRUE,
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM branches
    WHERE id = 1
);