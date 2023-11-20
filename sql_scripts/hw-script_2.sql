-- Select posts with 2 comments, 
-- title with numeric first symbol and content length > 20
SELECT p.post_id
FROM post p
LEFT JOIN "comment" c ON p.post_id = c.post_id 
GROUP BY p.post_id 
HAVING COUNT(c.comment_id) = 2 
	AND title ~ '^[0-9]' 
    AND LENGTH(p.content) > 20
ORDER BY p.post_id;
