CREATE TABLE attachments (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    ticket_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_attachment_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE
);

CREATE INDEX idx_attachments_ticket_id ON attachments(ticket_id);