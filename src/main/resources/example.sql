WITH r AS (
    SELECT
        webpage_id
    FROM goalreachestrafficsource
    WHERE date BETWEEN '2020-12-01' AND '2020-12-30'
    GROUP BY webpage_id)
SELECT
    pageid
FROM webpage
WHERE webpage.pageid not in (SELECT webpage_id FROM r);

/*ALL WEBPAGES WITHOUT COUNTERS*/
SELECT * FROM webpage
WHERE pageID IN (65064478, 67865518,
                 65156065, 56247157,
                 53667862, 37960820,
                 42267299, 58976794,
                 67426333, 69704290);

/*NUMBER OF GOALS*/
SELECT COUNT(distinct goalId) FROM goal;

/*NUMBER OF GOALS FOR EACH COUNTER*/
SELECT
    webpage_id,
    COUNT(DISTINCT goalId)
FROM goal
GROUP BY webpage_id;

/*PARTICULAR GOAL REACHES BY SEARCH ENGINE FOR PERIOD*/
WITH r AS(
    SELECT* FROM
    goalssearchengine
    WHERE webpage_id = 62401888
    AND date BETWEEN '2020-12-01' AND '2021-01-04'
)
SELECT
    goal_id,
    engine_id,
    sum(reaches)
FROM r
GROUP BY goal_id, engine_id;

/*ALL GOAL REACHES BY SEARCH ENGINE FOR PERIOD*/
WITH final AS (
    WITH g AS (
    SELECT * FROM
    goalssearchengine
    WHERE date BETWEEN '2020-12-01' AND '2020-12-31'
        )
        SELECT
            webpage_id,
            engine_id,
            sum(reaches) AS reaches
        FROM g
        GROUP BY webpage_id, engine_id
)
SELECT
    w.name AS webpage,
    engine_id,
    reaches
FROM final
LEFT JOIN webpage as w
ON final.webpage_id = w.pageid;

/*ALL VIEWS BY SEARCH ENGINE FOR PERIOD*/

WITH r AS (
    SELECT * FROM
    viewssearchengine
    WHERE webpage_id = 62401888
    AND date BETWEEN '2020-12-01' AND '2020-12-09'
)
SELECT
    engine_id,
    sum(reaches)
FROM r
GROUP BY webpage_id, engine_id;

WITH r AS (
    SELECT * FROM
    viewssearchengine
    WHERE webpage_id = 62401888
    AND date BETWEEN '2021-01-01' AND '2021-01-09'
)
SELECT
    engine_id,
    sum(reaches)
FROM r
GROUP BY webpage_id, engine_id;

WITH r AS (
    SELECT * FROM
    purchasessearchengine
    WHERE webpage_id = 62401888
    AND date BETWEEN '2020-12-01' AND '2020-12-09'
)
SELECT
    engine_id,
    sum(reaches)
FROM r
GROUP BY webpage_id, engine_id;

WITH r AS (
    SELECT * FROM
    purchasessearchengine
    WHERE webpage_id = 62401888
    AND date BETWEEN '2021-01-01' AND '2021-01-09'
)
SELECT
    engine_id,
    sum(reaches)
FROM r
GROUP BY webpage_id, engine_id;

/*PARTICULAR GOAL REACHES BY SOCIAL NETWORK FOR PERIOD*/
WITH r AS(
    SELECT* FROM
    goalssocialnetwork
    WHERE webpage_id = 62401888
    AND date BETWEEN '2020-12-01' AND '2021-01-04'
)
SELECT
    goal_id,
    network_id,
    sum(reaches)
FROM r
GROUP BY goal_id, network_id;

/*PARTICULAR GOAL REACHES BY REFERRAL SOURCE FOR PERIOD*/
WITH r AS(
    SELECT* FROM
    goalsreferral
    WHERE webpage_id = 62401888
    AND date BETWEEN '2020-12-01' AND '2021-01-04'
)
SELECT
    referral_id,
    sum(reaches)
FROM r
GROUP BY referral_id;
