-- CMart v2: security + shop settings (safe to re-run)
-- Applied automatically by cmart.db.SchemaMigrator on startup.

CREATE TABLE IF NOT EXISTS `app_user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `display_name` VARCHAR(80) NOT NULL,
  `password_hash` VARCHAR(128) NOT NULL,
  `password_salt` VARCHAR(64) NOT NULL,
  `role` VARCHAR(20) NOT NULL,
  `active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_app_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `app_setting` (
  `setting_key` VARCHAR(64) NOT NULL,
  `setting_value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`setting_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
