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

[39578050, 26059395, 24332956, 29736370, 48050831, 24596720, 23258257, 15070123, 19915630,
56140561, 45487302, 55811590, 20548771, 54241003, 62401888, 49911565, 62111218, 54131236, 57391372,
44494876, 62095546, 52392583, 59162569]

DELETE FROM viewsreferral WHERE webpage_id = 49911565 AND date < '2021-01-09';
DELETE FROM pricereferral WHERE webpage_id = 49911565 AND date < '2021-01-09';
DELETE FROM purchasesreferral WHERE webpage_id = 49911565 AND date < '2021-01-09';

SELECT referral_id, sum(reaches) FROM viewsreferral
where webpage_id = 49911565 AND date BETWEEN '2020-11-01' AND '2021-01-10'
GROUP BY referral_id ORDER BY sum(reaches) DESC;

SELECT sum(reaches) FROM viewsreferral
where webpage_id = 49911565 AND date BETWEEN '2020-11-01' AND '2021-01-10';

+ 59162569 - Лансаро - 2 счетчик
+ 54241003 - Автошкола "Перспектива"
+ 54131236 - Вектор Плюс
+ 56140561 - Кукарино-Парк
55811590 - Новая Дерябиха-2
15070123 - bestlabel.ru
44494876 - cyber-tel.ru
24332956 - OfficeMebel37.ru
26059395 - OutdoorPR.ru
24596720 - Prom-Plast37.ru
29736370 - Um-ok37.ru
62111218 - БМ ЗЧМ
39578050 - ИвСиликат
52392583 - ЛАНСАРО
45487302 - Центр Строительных Услуг
62095546 - Техресурс
+ 62401888 - Alleri
+ 19915630 - ivassorti.ru
57391372 - jenskiy-trikotaj.ru
49911565 - Анжело
20548771 - Иваново-Принт
23258257 - ТитанТехника
48050831 - Трейд-Дизайн

