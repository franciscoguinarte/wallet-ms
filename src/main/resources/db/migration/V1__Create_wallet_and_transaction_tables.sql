CREATE TABLE IF NOT EXISTS wallet (
                                      id UUID PRIMARY KEY,
                                      owner VARCHAR(255) NOT NULL,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS transaction (
                                           id UUID PRIMARY KEY,
                                           type VARCHAR(50) NOT NULL,
    direction VARCHAR(50) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    source_wallet_id UUID,
    destination_wallet_id UUID,
    timestamp TIMESTAMP NOT NULL,
    balance_source_after DECIMAL(19,2) NOT NULL,
    balance_destination_after DECIMAL(19,2),
    CONSTRAINT fk_source_wallet FOREIGN KEY (source_wallet_id) REFERENCES wallet(id),
    CONSTRAINT fk_destination_wallet FOREIGN KEY (destination_wallet_id) REFERENCES wallet(id)
    );