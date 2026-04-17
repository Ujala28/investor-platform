CREATE TABLE investors (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    date_of_birth DATE NOT NULL,
    investor_type VARCHAR(20) NOT NULL,
    pan_number VARCHAR(10) NOT NULL,
    kyc_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contacts (
    id BIGSERIAL PRIMARY KEY,
    investor_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    relationship_type VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),

    CONSTRAINT fk_investor
        FOREIGN KEY (investor_id)
        REFERENCES investors(id)
        ON DELETE CASCADE
);