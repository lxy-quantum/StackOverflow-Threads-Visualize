-- top N topics most frequently asked
SELECT topic, CAST(round(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM questions), 2) AS DOUBLE PRECISION) AS frequency
FROM (
         SELECT UNNEST(STRING_TO_ARRAY(tags, ', ')) AS topic
         FROM questions
     ) topics
WHERE topic not like 'java'
GROUP BY topic
ORDER BY frequency DESC
LIMIT 15;


-- top N topics with most engagement from high reputation users
SELECT
    tag,
    round(SUM(engagement_score)) AS total_engagement
FROM (
         -- Combine all queries with UNION ALL
         (
             SELECT UNNEST(STRING_TO_ARRAY(q.tags, ', ')) AS tag, 0.35 * SUM(u.reputation) AS engagement_score
             FROM questions q
                      JOIN users u ON q.owner_user_id = u.user_id
             WHERE u.reputation > 50000
             GROUP BY tag
         ) UNION ALL
         (
             SELECT UNNEST(STRING_TO_ARRAY(q.tags, ', ')) AS tag, 0.25 * SUM(u.reputation) AS engagement_score
             FROM answers a
                      JOIN questions q ON a.question_id = q.question_id
                      JOIN users u ON a.owner_user_id = u.user_id
             WHERE u.reputation > 50000
             GROUP BY tag
         ) UNION ALL
         (
             SELECT UNNEST(STRING_TO_ARRAY(q.tags, ', ')) AS tag, 0.2 * SUM(u.reputation) AS engagement_score
             FROM questions q
                      JOIN users u ON q.last_editor_id = u.user_id
             WHERE u.reputation > 50000
             GROUP BY tag
         ) UNION ALL
         (
             SELECT UNNEST(STRING_TO_ARRAY(q.tags, ', ')) AS tag, 0.1 * SUM(u.reputation) AS engagement_score
             FROM comments c
                      JOIN questions q ON c.post_id = q.question_id AND c.post_type = 'question'
                      JOIN users u ON c.owner_user_id = u.user_id
             WHERE u.reputation > 50000
             GROUP BY tag
         ) UNION ALL
         (
             SELECT UNNEST(STRING_TO_ARRAY(q.tags, ', ')) AS tag, 0.1 * SUM(u.reputation) AS engagement_score
             FROM comments c
                      JOIN answers a ON c.post_id = a.answer_id AND c.post_type = 'answer'
                      JOIN questions q ON a.question_id = q.question_id
                      JOIN users u ON c.owner_user_id = u.user_id
             WHERE u.reputation > 50000
             GROUP BY tag
         )
     ) t
WHERE tag NOT LIKE 'java'
GROUP BY tag
ORDER BY total_engagement DESC
LIMIT 15;


-- top N errors and exceptions
SELECT matched_pattern[1] AS pattern, COUNT(*) AS frequency
FROM (
         SELECT REGEXP_MATCH(body, '[a-zA-Z]+Error') AS matched_pattern
         FROM questions
         UNION ALL
         SELECT REGEXP_MATCH(body, '[a-zA-Z]+Error') AS matched_pattern
         FROM questions
         UNION ALL
         SELECT REGEXP_MATCH(body, '[a-zA-Z]+Error') AS matched_pattern
         FROM comments
         UNION ALL
         SELECT REGEXP_MATCH(body, '[a-zA-Z]+Exception') AS matched_pattern
         FROM answers
         UNION ALL
         SELECT REGEXP_MATCH(body, '[a-zA-Z]+Exception') AS matched_pattern
         FROM answers
         UNION ALL
         SELECT REGEXP_MATCH(body, '[a-zA-Z]+Exception') AS matched_pattern
         FROM comments
     ) t
WHERE matched_pattern IS NOT NULL
GROUP BY matched_pattern[1]
ORDER BY frequency DESC
LIMIT 10;


-- factors that contribute to answer quality
-- by elapsed time
SELECT time_interval, accept_rate, avg_upvotes, count
FROM (SELECT CASE
                 WHEN t1.time_interval = '0-1 hours' THEN 1
                 WHEN t1.time_interval = '1-2 hours' THEN 2
                 WHEN t1.time_interval = '2-4 hours' THEN 3
                 WHEN t1.time_interval = '4-8 hours' THEN 4
                 WHEN t1.time_interval = '8-24 hours' THEN 5
                 WHEN t1.time_interval = '24-72 hours' THEN 6
                 WHEN t1.time_interval = '3 days - 1 week' THEN 7
                 WHEN t1.time_interval = '1 week - 1 month' THEN 8
                 ELSE 9
                 END AS time,
             t1.time_interval,
             t1.accept_rate,
             t1.avg_upvotes,
             count
      FROM (SELECT CASE
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '1 hour' THEN '0-1 hours'
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '2 hours' THEN '1-2 hours'
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '4 hours' THEN '2-4 hours'
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '8 hours' THEN '4-8 hours'
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '24 hours' THEN '8-24 hours'
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '72 hours' THEN '24-72 hours'
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '1 week' THEN '3 days - 1 week'
                       WHEN (a.creation_date - q.creation_date) < INTERVAL '1 month' THEN '1 week - 1 month'
                       ELSE '1 month+'
                       END                                                                            AS time_interval,
                   cast(round(avg(CASE WHEN a.is_accepted THEN 1 ELSE 0 END), 2) as double precision) AS accept_rate,
                   cast(round(avg(a.up_vote_count), 2) as double precision)                           AS avg_upvotes,
                   count(*)                                                                           AS count
            FROM answers a
                     JOIN questions q ON a.question_id = q.question_id
            GROUP BY time_interval
            ORDER BY time_interval) AS t1
      ORDER BY time) AS t2;

-- by reputation
SELECT reputation_interval, accept_rate, avg_upvotes, count
FROM (SELECT CASE
                 WHEN t1.reputation_interval = '0-100' THEN 1
                 WHEN t1.reputation_interval = '100-500' THEN 2
                 WHEN t1.reputation_interval = '500-1000' THEN 3
                 WHEN t1.reputation_interval = '1000-5000' THEN 4
                 WHEN t1.reputation_interval = '5000-10000' THEN 5
                 WHEN t1.reputation_interval = '10000-50000' THEN 6
                 WHEN t1.reputation_interval = '50000-100000' THEN 7
                 ELSE 8
                 END AS level,
             t1.reputation_interval,
             t1.accept_rate,
             t1.avg_upvotes,
             count
      FROM (SELECT CASE
                       WHEN u.reputation <= 100 THEN '0-100'
                       WHEN u.reputation <= 500 THEN '100-500'
                       WHEN u.reputation <= 1000 THEN '500-1000'
                       WHEN u.reputation <= 5000 THEN '1000-5000'
                       WHEN u.reputation <= 10000 THEN '5000-10000'
                       WHEN u.reputation <= 50000 THEN '10000-50000'
                       WHEN u.reputation <= 100000 THEN '50000-100000'
                       ELSE '100000+'
                       END                                                                            AS reputation_interval,
                   cast(round(avg(CASE WHEN a.is_accepted THEN 1 ELSE 0 END), 2) as double precision) AS accept_rate,
                   cast(round(avg(a.up_vote_count), 2) as double precision)                           AS avg_upvotes,
                   count(*)                                                                           AS count
            FROM answers a
                     JOIN users u ON a.owner_user_id = u.user_id
            GROUP BY reputation_interval
            ORDER BY reputation_interval) AS t1
      ORDER BY level) AS t2;

-- by answer length
SELECT answer_length_interval, accept_rate, avg_upvotes, count
FROM (SELECT CASE
                 WHEN answer_length_interval = '0-300' THEN 1
                 WHEN answer_length_interval = '300-500' THEN 2
                 WHEN answer_length_interval = '500-700' THEN 3
                 WHEN answer_length_interval = '700-1000' THEN 4
                 WHEN answer_length_interval = '1000-3000' THEN 5
                 WHEN answer_length_interval = '3000-5000' THEN 6
                 ELSE 7
                 END AS level,
             answer_length_interval,
             accept_rate,
             avg_upvotes,
             count
      FROM (SELECT CASE
                       WHEN length(a.body) <= 300 THEN '0-300'
                       WHEN length(a.body) <= 500 THEN '300-500'
                       WHEN length(a.body) <= 700 THEN '500-700'
                       WHEN length(a.body) <= 1000 THEN '700-1000'
                       WHEN length(a.body) <= 3000 THEN '1000-3000'
                       WHEN length(a.body) <= 5000 THEN '3000-5000'
                       ELSE '5000+'
                       END                                                                            AS answer_length_interval,
                   cast(round(avg(CASE WHEN a.is_accepted THEN 1 ELSE 0 END), 2) as double precision) AS accept_rate,
                   cast(round(avg(a.up_vote_count), 2) as double precision)                           AS avg_upvotes,
                   count(*)                                                                           AS count
            FROM answers a
            GROUP BY answer_length_interval
            ORDER BY answer_length_interval) AS t1
      ORDER BY level) AS t2;