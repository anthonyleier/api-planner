CREATE TABLE link (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);