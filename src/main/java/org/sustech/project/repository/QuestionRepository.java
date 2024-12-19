package org.sustech.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.sustech.project.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = """
            SELECT topic, CAST(round(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM questions), 2) AS DOUBLE PRECISION) AS frequency
            FROM (
                     SELECT UNNEST(STRING_TO_ARRAY(tags, ',')) AS topic
                     FROM questions
                 ) topics
            WHERE topic not like 'java'
            GROUP BY topic
            ORDER BY frequency DESC
            LIMIT 15;""", nativeQuery = true)
    List<Object[]> findTopNJavaTopics();

    @Query(nativeQuery = true, value = """
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
            LIMIT 15;""")
    List<Object[]> findTopEngagedTopics();

}
