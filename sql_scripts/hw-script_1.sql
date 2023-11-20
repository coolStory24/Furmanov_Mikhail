-- Count the number of users without any posts
SELECT COUNT(profile.profile_id)
FROM profile
LEFT JOIN post p ON profile.profile_id = p.profile_id
WHERE p.post_id IS NULL;
