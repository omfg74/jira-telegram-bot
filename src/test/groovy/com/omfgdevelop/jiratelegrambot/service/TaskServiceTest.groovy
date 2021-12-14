package com.omfgdevelop.jiratelegrambot.service

import com.omfgdevelop.jiratelegrambot.RepositorySpecBase
import com.omfgdevelop.jiratelegrambot.entity.Task
import com.omfgdevelop.jiratelegrambot.repository.TaskQueueRepository
import org.springframework.beans.factory.annotation.Autowired


class TaskServiceTest extends RepositorySpecBase {

    @Autowired
    TaskService taskService

    @Autowired
    TaskQueueRepository repository

    def 'может удалить не завершенные задачи'() {
        given:
        def telegramId = 123456
        List<Task> listPrev = repository.findAllByTelegramId(telegramId)
        def hasUncompleted =false
        for (i in 0..<listPrev.size()) {
            if (listPrev.get(i).getStatus()==4||listPrev.get(i).getStatus()==5||listPrev.get(i).getStatus()==6)
                hasUncompleted=true
        }

        when:
        taskService.dropAllPendingTasks(telegramId)
        List<Task> list = repository.findAllByTelegramId(telegramId)
        then:
        list.size()>0
        for (i in 0..<list.size()) {
           if (list.get(i).getStatus()==4||list.get(i).getStatus()==5||list.get(i).getStatus()==6)
               throw new Exception()
        }
        hasUncompleted

    }

}
