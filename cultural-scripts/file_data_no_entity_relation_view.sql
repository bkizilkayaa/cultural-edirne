CREATE VIEW file_data_no_entity_relation_view AS
SELECT fd.*
FROM file_data fd
         LEFT JOIN artwork_images ai ON fd.id = ai.image_id
         LEFT JOIN tourist_spot_images tsi ON fd.id = tsi.image_id
WHERE ai.ID IS NULL AND tsi.ID IS NULL;
