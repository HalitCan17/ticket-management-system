CREATE TABLE tickets (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  status VARCHAR(30) NOT NULL,
  priority VARCHAR(30) NOT NULL,
  product_id BIGINT,
  assignee_id VARCHAR(50),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
