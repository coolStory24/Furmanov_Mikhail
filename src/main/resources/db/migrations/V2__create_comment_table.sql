-- create comment

CREATE TABLE comment(
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	text TEXT NOT NULL,
	"articleId" BIGINT REFERENCES article (id)  ON DELETE CASCADE NOT NULL
);