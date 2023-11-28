-- create article

CREATE TABLE article (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(200) NOT NULL,
	tags VARCHAR(40)[] DEFAULT '{}',
	trending BOOLEAN DEFAULT 'false'
);
