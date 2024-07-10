CREATE TABLE activity (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    occurs_at TIMESTAMP NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);