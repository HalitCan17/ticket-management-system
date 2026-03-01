CREATE TABLE sla_policies
(
    id                    BIGSERIAL PRIMARY KEY,
    priority              VARCHAR(30) NOT NULL UNIQUE,
    response_time_hours   INT         NOT NULL,
    resolution_time_hours INT         NOT NULL
);

INSERT INTO sla_policies (priority, response_time_hours, resolution_time_hours)
VALUES ('LOW', 24, 72),
       ('MEDIUM', 12, 48),
       ('HIGH', 4, 24);

ALTER TABLE tickets
    ADD COLUMN first_response_due_at TIMESTAMPTZ,
    ADD COLUMN resolution_due_at     TIMESTAMPTZ,
    ADD COLUMN first_response_at     TIMESTAMPTZ,
    ADD COLUMN resolved_at           TIMESTAMPTZ,
    ADD COLUMN sla_breached          BOOLEAN NOT NULL DEFAULT FALSE;