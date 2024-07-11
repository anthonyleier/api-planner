CREATE TABLE participant (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_confirmed BOOLEAN NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);