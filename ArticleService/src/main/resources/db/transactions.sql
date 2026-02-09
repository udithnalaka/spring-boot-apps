-- REQUIREMENT
    /*
     SQL postgres query to get the negative amounts (credit card payments) grouped by the month and then
     count the transactions to see if more than 3 transactions are made with a total above 200.
     if not add a 5 dollar fee to the monthly amount. Return the remaining Balance for the full year
     (deposits - withdrawals including credit card month fee)
     */

-- Table creation and data
CREATE TABLE transactions (
        amount NUMERIC(10, 2),
        transaction_date DATE
);

-- Inserted data. insert more accordingly
INSERT INTO transactions (amount, transaction_date) VALUES
        (400.00, '2024-01-01'),
        (-200.50, '2024-01-02'),
        (300.00, '2024-02-03'),
        (-150.00, '2024-03-04'),
        (-400.50, '2024-03-07'),
        (500.00, '2024-06-03'),
        (-150.00, '2024-06-04'),
        (300.00, '2024-07-05');

--============
-- SOLUTION
--============
CREATE OR REPLACE FUNCTION calculate_balance()
    RETURNS TABLE(
        --positive_total NUMERIC,
        --negative_total NUMERIC,
        balance NUMERIC
    ) AS $$
    DECLARE
    v_positive_total NUMERIC := 0;
        v_negative_total NUMERIC := 0;
        v_balance NUMERIC := 0;
    BEGIN
        -- Get positive transactions
    SELECT COALESCE(SUM(amount), 0)
    INTO v_positive_total
    FROM transactions
    WHERE amount > 0;

    -- Get negative transactions with fees using sub queries

    -- SELECT COALESCE(SUM(amount), 0)
    -- INTO v_negative_total
    -- FROM transactions
    -- WHERE amount < 0;

    WITH monthly_negative_transactions AS (
        -- Get monthly negative transaction data
        SELECT
            DATE_TRUNC('month', transaction_date)::DATE AS month,
        COUNT(*) AS negative_transaction_count,
        COALESCE(SUM(amount), 0) AS total_negative_amount
    FROM transactions
    WHERE amount < 0
    GROUP BY DATE_TRUNC('month', transaction_date)
        ),
        negative_transactions_with_fees AS (
    SELECT
        month,
        negative_transaction_count,
        total_negative_amount,
        -- CASE
        --     WHEN negative_transaction_count <= 3 AND ABS(total_negative_amount) < 200
        --     THEN -5.00  -- Add $5 fee (negative because it's a charge)
        --     ELSE 0.00
        -- END AS monthly_fee,
        total_negative_amount +
        CASE
        WHEN negative_transaction_count <= 3 AND ABS(total_negative_amount) < 200
        THEN -5.00
        ELSE 0.00
        END AS adjusted_monthly_amount
    FROM monthly_negative_transactions
    ORDER BY month
        )
    SELECT
    INTO v_negative_total
        COALESCE(SUM(ntwf.adjusted_monthly_amount), 0) as negative_balance
    FROM negative_transactions_with_fees as ntwf;

    -- Calculate balance
    v_balance := v_positive_total + v_negative_total;

        -- Return results
        -- RETURN QUERY SELECT v_positive_total, v_negative_total, v_balance;
    RETURN QUERY SELECT v_balance;
END;
$$ LANGUAGE plpgsql;


-- Drop function (specify parameter type)
DROP FUNCTION IF EXISTS calculate_balance();

-- Call the function
SELECT * FROM calculate_balance();
