-- Select first 10 posts with 0 to 1 comments
SELECT p.post_id
FROM post p
LEFT JOIN "comment" c ON p.post_id = c.post_id 
GROUP BY p.post_id 
HAVING COUNT(c.comment_id) BETWEEN 0 AND 1
ORDER BY p.post_id
LIMIT 10
