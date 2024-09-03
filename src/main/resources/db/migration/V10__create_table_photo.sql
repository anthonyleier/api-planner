CREATE TABLE photo (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    filename VARCHAR(20) NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);
