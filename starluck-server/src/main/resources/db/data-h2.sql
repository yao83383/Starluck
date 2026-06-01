-- 初始数据：礼物目录
INSERT INTO `gift` (`name`, `icon`, `category`, `price`, `sort`) VALUES
('玫瑰花',  '🌹', 'romantic', 66,   1),
('巧克力',  '🍫', 'romantic', 88,   2),
('水晶鞋',  '👠', 'romantic', 128,  3),
('拥抱',    '🤗', 'romantic', 166,  4),
('戒指',    '💍', 'luxury',   520,  5),
('跑车',    '🏎️', 'luxury',  1314, 6),
('城堡',    '🏰', 'luxury',  5200, 7),
('皇冠',    '👑', 'luxury',  9999, 8),
('香水',    '💐', 'romantic', 199,  9),
('口红',    '💄', 'romantic', 266,  10),
('项链',    '📿', 'luxury',   888,  11),
('包包',    '👜', 'luxury',   1888,12);

-- 默认推送规则
INSERT INTO `push_rule` (`enabled`, `time_slots`, `strategy`, `cooldown_min`) VALUES
(1, '[{"start":"09:00","end":"11:00","maxPer":1},{"start":"12:00","end":"14:00","maxPer":1},{"start":"19:00","end":"22:30","maxPer":2}]', 'active', 30);
