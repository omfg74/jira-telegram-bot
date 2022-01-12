# Jira telegram bot backend

### Docker db start

```bash db-recreate.sh```

### liquibase migrations 

```bash updateLocalDb.sh```

#### ENV variables need for minimal start

`jira.admin.username` (JIRA_ADMIN_USERNAME) - jira user which has permissions to fetch projects
 
`jira.admin.password` (JIRA_ADMIN_PASSWORD)- its password
 
`spring.profiles.active` (SPRING_PROFILES_ACTIVE) - profile names which need to be enabled default(jira_update,jira_issue)
 
`app.telegrambot.token` (APP_TELEGRAMBOT_TOKEN) - token of your bot in telegram
 
`app.webhook.address` (APP_WEBHOOK_ADDRESS) - url of your bot backend


##### Main functions
`/create_new_task` - starts scenario of task creation
`/cancel_current_task`- stops scenario of task creation
`/delete_user` - delete user which assigned tu current telegram id

#### Notes 
For correct user authorization check it needs to be a user lookup permission in Jira for user group.
