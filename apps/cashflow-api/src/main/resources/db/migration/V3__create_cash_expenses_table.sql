CREATE TABLE cash_expenses (
    id UUID PRIMARY KEY,
    description VARCHAR(120) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    expense_date DATE NOT NULL,
    category_id UUID NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    notes VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_cash_expenses_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id),
    CONSTRAINT ck_cash_expenses_amount_positive
        CHECK (amount > 0)
);
