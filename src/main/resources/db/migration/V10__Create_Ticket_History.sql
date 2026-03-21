CREATE TABLE ticket_history (
                                id UUID PRIMARY KEY,
                                ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
                                old_status VARCHAR(50),
                                new_status VARCHAR(50),
                                old_assignee_id UUID,
                                new_assignee_id UUID,
                                changed_by_id UUID NOT NULL,
                                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ticket_history_ticket_id ON ticket_history(ticket_id);