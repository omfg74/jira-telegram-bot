create table users
(
    id                bigserial
        not null
        constraint users_pk
            primary key,
    telegram_id       int,
    telegram_username text,
    jira_username     text,
    jira_password     text
);

create table tasks_queue
(
    id               bigserial
        not null
        constraint tasks_queue_pk primary key,
    task_type        int,
    telegram_id      int,
    params           text,
    status           int,
    task_title       text,
    task_text        text,
    task_responsible text
);

create table telegram_status
(
    id             int,
    current_offset int
);