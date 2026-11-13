CREATE TABLE tasks (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,   -- (5) id gerado pelo banco
    title       VARCHAR(150) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL,
    priority    VARCHAR(20)  NOT NULL,
    due_date    DATE,                                              -- (8) sem default, pode ser nulo
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),               -- (9)(10)
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),               -- (1) sem ON UPDATE

    CONSTRAINT chk_tasks_status
        CHECK (status IN ('PENDING', 'IN_PROGRESS', 'DONE')),      -- (6) maiúsculo = enum Java
    CONSTRAINT chk_tasks_priority
        CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH'))              -- (7)
);                                                                 -- (4)

-- (3) índices são comandos separados, depois da tabela
CREATE INDEX idx_tasks_status   ON tasks (status);
CREATE INDEX idx_tasks_priority ON tasks (priority);
CREATE INDEX idx_tasks_due_date ON tasks (due_date);