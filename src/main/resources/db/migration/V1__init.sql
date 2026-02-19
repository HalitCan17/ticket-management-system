-- V1__init.sql

CREATE TABLE tickets (
  id BIGSERIAL PRIMARY KEY,
  public_id UUID NOT NULL UNIQUE,

  title VARCHAR(200) NOT NULL,
  description TEXT NOT NULL,

  status VARCHAR(30) NOT NULL,
  priority VARCHAR(30) NOT NULL,

  requester_id UUID NOT NULL,
  assignee_id UUID NULL,

  version BIGINT NOT NULL DEFAULT 0,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_tickets_status ON tickets(status);
CREATE INDEX idx_tickets_priority ON tickets(priority);
CREATE INDEX idx_tickets_requester_id ON tickets(requester_id);
CREATE INDEX idx_tickets_assignee_id ON tickets(assignee_id);
CREATE INDEX idx_tickets_created_at ON tickets(created_at);
