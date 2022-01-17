# Jira telegram bot backend

Simple telegram bot which can halp you fast create issues in jira using telegram api in private or public chats. Also it can manage public chat permissions and filter jira projects to acccess.

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
`/delete_user` - delete user which assigned to current telegram id

#### Notes 
For correct user authorization check it needs to be a user lookup permission in Jira for user group.


##### Debug run script
`#!/bin/bash
java -jar -Djira.base.url=http://<jira_url>:<port> \
-Dapp.webhook.address=https://<backend_url> \
 -Djira.admin.username=<jira_admin_user>\
-Djira.admin.password=<jira_admin_password>\
 -Dapp.telegrambot.token=<bot_token> \
 -Dspring.profiles.include=jira_update,jira_issue,webhook_check\
 -Dapp.reply.url=http://<backend_url>:<port>/browse/ \
 -Dapp.admin.username=admin \
 -Dapp.admin.password=admin \
 -Dapp.security.enabled=true \
 -Dapp.bot.address=@your_bot_name \
 jira-telegram-bot-0.0.1-SNAPSHOT.jar`
