package com.omfgdevelop.jiratelegrambot;

import java.util.Locale;

public class HandlerConstants {

    public static final String GO_TO_MAIN_CHAT = "Перейдите в основной чат бота для создания расширенной задачи. Или отправьте боту сообщение в формате <имя задачи>@<текст задачи> с остальным он сам разберется";
    public static final String UNREGISTERED_GROUP_CHAT = "Этот чат не зарегистрирован в JiraBot";
    public static final String DELETED_GROUP_CHAT = "Этот чат удален";
    public static final String NOT_PERMITTED_GROUP_CHAT ="Этот чат не подтвержден для отправки задач в Jira" ;
    public static final String CURRENT_CHAT_ALREADY_REGISTERED = "Этот чат уже зарегистрирован.";
    public static final String CHAT_HAS_BEEN_REGISTERED = "Чат успешно зарегистрирован";
    public static final String CURRENT_USER_NOT_REGISTERED_IN_JIRA_BOT = "Пользователь не зарегистрирован в боте";
    public static final String USER_IS_BANNED = "Пользователь заблокирован.";
    public static final String CHAT_IS_NOT_REGISTERED = "Этот чат не зарегистрирован";
    public static final String NO_CHAT_ASSIGNED = "Для этого чата не назначен ни один проект";
    public static final String IN_PROGRESS = "Принято, занимаюсь...";
    public static final String FERST_CHOOSE_PROJECT_FOR_CURRENT_TASK = "Сначала выберите проект для этой задачи";
    public static final String START_MAESSGE = "Введите /create_new_task или /start";
    public static final String USER_IS_DELETED = "Пользователь удален. Можно зарегистрировать нового.\\nВведите /start для повторной регистрации.";
    public static final String ACTION_CANCELED = "Действие отменено.";
    public static final String ARE_YOU_SURE_DELETE_USER = "Вы уверены что хотите удалить пользователя %s?\nВведите YES для удаления или любой текст для отмены";
    public static final String USER_NOT_FOUND = "Пользователь с id %s не найден.";
    public static final String ONLY_FOR_GROUP_CHATS = "Только для групповых чатов";
    public static final String NO_TASK_FOUND = "No task to create found";
    public static final String YOU_HAVE_NO_PANDING_TASKS = "Something wrong you have no pending tasks";
    public static final String TASK_ADDED = "Добавлена задача %s\nс описанием:\n%s";
    public static final String USER_BLOCKED_OR_DELETED = "Пользователь заблокирован или удален";
    public static final String NEW_TASK_AND_AUTHOR = "Новая задача\nавтор %s.\nПридумайте название.";
    public static final String NO_SUCH_USER_REGISTER = "Такого пользователя нет.\nЗарегистрируйтесь. %s";
    public static final String YOU_HAVE_PENDING_TASK_ENTER_NAME = "У вас есть незавершенная задача.\nВведите название для нее.";
    public static final String YOU_HAVE_PENDING_TASK_ENTER_TEXT = "У вас есть незаконченная задача. Осталось ввести описание для нее.";
    public static final String ENTER_JIRA_USERNAME = "Пользователь не зарегистрирован.\nВведите имя пользователя от JIRA";
    public static final String USER_BLOCKED_IN_JIRA = "Пользователь %s заблокирован в  jira.";
    public static final String REGISTRATION_SUCCESS = "Регистрация прошла успешно %s.";
    public static final String JIRA_PASSWORD_CHECK_FAILED = "Jira password check failed. Try to enter password again";
    public static final String CANT_FETCH_USER_DATA = "Cant fetch user data. Try to enter password again";
    public static final String LOGIN_ERROR = "Login error. Try to enter password again";
    public static final String USER_BLOCKED = "Пользователь %s заблокирован";
    public static final String ALREDY_EXISTS = "На этот telegramId уже назначен пользователь Jira. Введите пароль от вашего пользователя или введите /delete_user для отмены привязки";
    public static final String USER_ADDED_ENTER_PASSWORD = "Добавлен пользователь %s. Введите пароль. Он не будет сохранен, будет использоваться только для подтверждения входа в jira.";
    public static final String USER_NOT_EXISTS = "Пользователя %s не существует в Jira. Если имя введено не верно, то введите /delete_user и повторите авторизацию";
    public static final String ALL_TASKS_ARE_CANCELED = "Все незаконченные задачи отменены. Можно создать новую";
}
