create table jira_project
        (
        id text not null
        constraint jira_project_pk
        primary key,
        expand text,
        self text,
        key text,
        name text,
        project_key text
        );
