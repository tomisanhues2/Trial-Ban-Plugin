create table if not exists active_bans
(
    ban_id       int auto_increment
        primary key,
    ban_type     varchar(20)  not null,
    player_uuid  varchar(45)  not null,
    banner_uuid  varchar(45)  null,
    ban_reason   varchar(256) null,
    ban_date     bigint       not null,
    ban_duration varchar(20)  not null,
    unban_date   bigint       null
);

create table if not exists banned_players
(
    player_id   int auto_increment
        primary key,
    player_uuid varchar(45) not null
);

create table if not exists player_history
(
    history_id   int auto_increment
        primary key,
    player_uuid  varchar(45)  not null,
    staff_uuid   varchar(45)  not null,
    ban_reason   varchar(256) not null,
    ban_date     bigint       not null,
    ban_duration varchar(20)  not null,
    unban_date   bigint       null,
    ban_status   varchar(11)  not null
);



