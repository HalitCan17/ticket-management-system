-- 1. YÖNETİCİLER (ADMIN & MANAGER)
INSERT INTO users (id, username, email, role, full_name) VALUES
 ('550e8400-e29b-41d4-a716-446655440000', 'sys_admin', 'admin@ticket.com', 'ADMIN', 'Halit Can'),
 ('770e8400-e29b-41d4-a716-446655443333', 'it_manager', 'mert.yilmaz@sirket.com', 'MANAGER', 'Mert Yılmaz');

-- 2. DESTEK PERSONELİ (AGENT)
INSERT INTO users (id, username, email, role, full_name) VALUES
('660e8400-e29b-41d4-a716-446655441111', 'agent_ahmet', 'ahmet.kaya@sirket.com', 'AGENT', 'Ahmet Kaya'),
('660e8400-e29b-41d4-a716-446655442222', 'agent_ayse', 'ayse.demir@sirket.com', 'AGENT', 'Ayşe Demir'),
('660e8400-e29b-41d4-a716-446655447777', 'agent_mehmet', 'mehmet.oz@sirket.com', 'AGENT', 'Mehmet Öz');

-- 3. MÜŞTERİLER (CUSTOMER)
INSERT INTO users (id, username, email, role, full_name) VALUES
('880e8400-e29b-41d4-a716-446655444444', 'user_ali', 'ali.ozkan@gmail.com', 'CUSTOMER', 'Ali Özkan'),
('880e8400-e29b-41d4-a716-446655445555', 'user_fatma', 'fatma.sahin@outlook.com', 'CUSTOMER', 'Fatma Şahin'),
('880e8400-e29b-41d4-a716-446655446666', 'user_zeynep', 'zeynep.yildiz@yandex.com', 'CUSTOMER', 'Zeynep Yıldız'),
('880e8400-e29b-41d4-a716-446655448888', 'user_burak', 'burak.can@hotmail.com', 'CUSTOMER', 'Burak Can');