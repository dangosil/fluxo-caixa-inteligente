ALTER TABLE cash_entries
    ADD COLUMN fee_amount NUMERIC(15,2) NOT NULL DEFAULT 0,
    ADD COLUMN fee_payer VARCHAR(20) NOT NULL DEFAULT 'MERCHANT',
    ADD COLUMN card_brand VARCHAR(30),
    ADD COLUMN installment_count INTEGER NOT NULL DEFAULT 1,
    ADD COLUMN installment_amount NUMERIC(15,2);

ALTER TABLE cash_entries
    ADD CONSTRAINT ck_cash_entries_fee_amount_not_negative
        CHECK (fee_amount >= 0),
    ADD CONSTRAINT ck_cash_entries_fee_payer_valid
        CHECK (fee_payer IN ('MERCHANT', 'CUSTOMER')),
    ADD CONSTRAINT ck_cash_entries_installment_count_positive
        CHECK (installment_count >= 1),
    ADD CONSTRAINT ck_cash_entries_installment_amount_not_negative
        CHECK (installment_amount IS NULL OR installment_amount >= 0),
    ADD CONSTRAINT ck_cash_entries_merchant_fee_not_greater_than_amount
        CHECK (fee_payer <> 'MERCHANT' OR fee_amount <= amount);
