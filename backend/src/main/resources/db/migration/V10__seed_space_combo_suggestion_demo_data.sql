INSERT INTO floors (branch_id, name, status, created_at, updated_at)
SELECT 1, 'Tầng 1', 'ACTIVE', NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM floors WHERE branch_id = 1 AND name = 'Tầng 1'
);

INSERT INTO space_units (branch_id, floor_id, parent_id, space_type, name, capacity, is_directly_rentable, status, price_group, created_at, updated_at)
SELECT 1, 1, NULL, 'ROOM', 'P01', 20, TRUE, 'ACTIVE', 'ROOM_STD', NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM space_units WHERE name = 'P01' AND space_type = 'ROOM'
);

INSERT INTO space_units (branch_id, floor_id, parent_id, space_type, name, capacity, is_directly_rentable, status, price_group, created_at, updated_at)
SELECT 1, NULL, 1, 'TABLE', 'B01', 4, TRUE, 'ACTIVE', 'TABLE_4', NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM space_units WHERE name = 'B01' AND space_type = 'TABLE'
);

INSERT INTO space_units (branch_id, floor_id, parent_id, space_type, name, capacity, is_directly_rentable, status, price_group, created_at, updated_at)
SELECT 1, NULL, 1, 'TABLE', 'B02', 6, TRUE, 'ACTIVE', 'TABLE_6', NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM space_units WHERE name = 'B02' AND space_type = 'TABLE'
);

INSERT INTO combo_plans (name, duration_minutes, price, applicable_space_type, min_capacity, max_capacity, applicable_price_group, is_active, created_at, updated_at)
SELECT 'Bàn 4 - 2 giờ', 120, 80000, 'TABLE', 4, 4, 'TABLE_4', TRUE, NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM combo_plans WHERE name = 'Bàn 4 - 2 giờ'
);

INSERT INTO combo_plans (name, duration_minutes, price, applicable_space_type, min_capacity, max_capacity, applicable_price_group, is_active, created_at, updated_at)
SELECT 'Bàn 4 - 3 giờ', 180, 110000, 'TABLE', 4, 4, 'TABLE_4', TRUE, NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM combo_plans WHERE name = 'Bàn 4 - 3 giờ'
);

INSERT INTO combo_plans (name, duration_minutes, price, applicable_space_type, min_capacity, max_capacity, applicable_price_group, is_active, created_at, updated_at)
SELECT 'Bàn 6 - 2 giờ', 120, 120000, 'TABLE', 5, 6, 'TABLE_6', TRUE, NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM combo_plans WHERE name = 'Bàn 6 - 2 giờ'
);

INSERT INTO suggestion_rules (min_people, max_people, target_space_type, min_capacity, max_capacity, applicable_price_group, priority, is_active, created_at, updated_at)
SELECT 1, 4, 'TABLE', 2, 4, 'TABLE_4', 20, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM suggestion_rules WHERE min_people = 1 AND max_people = 4 AND applicable_price_group = 'TABLE_4'
);

INSERT INTO suggestion_rules (min_people, max_people, target_space_type, min_capacity, max_capacity, applicable_price_group, priority, is_active, created_at, updated_at)
SELECT 5, 6, 'TABLE', 5, 6, 'TABLE_6', 20, TRUE, NOW(), NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM suggestion_rules WHERE min_people = 5 AND max_people = 6 AND applicable_price_group = 'TABLE_6'
);