ALTER TABLE cash_entries
    ADD COLUMN expected_receipt_date DATE;

UPDATE cash_entries
SET expected_receipt_date = entry_date
WHERE expected_receipt_date IS NULL;

ALTER TABLE cash_entries
    ALTER COLUMN expected_receipt_date SET NOT NULL;
