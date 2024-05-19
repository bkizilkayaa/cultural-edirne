CREATE TABLE `tourist_spot` (
                                `id` bigint NOT NULL,
                                `author_id` bigint DEFAULT NULL,
                                `content` text,
                                `create_date` datetime(6) DEFAULT NULL,
                                `description` varchar(255) DEFAULT NULL,
                                `modified_date` datetime(6) DEFAULT NULL,
                                `parent_id` bigint DEFAULT NULL,
                                `title` varchar(255) DEFAULT NULL,
                                `zip_code_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK7nogxpscxr87se5ciwch4yxs` (`zip_code_id`),
                                CONSTRAINT `FK7nogxpscxr87se5ciwch4yxs` FOREIGN KEY (`zip_code_id`) REFERENCES `zip_codes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
