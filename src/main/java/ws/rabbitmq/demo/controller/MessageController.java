package ws.rabbitmq.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.rabbitmq.demo.service.LogsService;

/**
 * @author WindShadow
 * @version 2021-08-06.
 */

@Slf4j
@RestController
@RequestMapping("/logs")
public class MessageController {

    @Autowired
    public LogsService logsServcie;

    @GetMapping("/info/{context}")
    public String sendInfoLog(@PathVariable("context") String context) {

        log.info("接收[info]日志,context: {}",context);
        logsServcie.sendInfoLog(context);
        return "日志内容：" + context;
    }

    @GetMapping("/warn/{context}")
    public String sendWarnLog(@PathVariable("context") String context) {

        log.info("接收[warn]日志,context: {}",context);
        logsServcie.sendWarnLog(context);
        return "日志内容：" + context;
    }

    @GetMapping("/error/{context}")
    public String sendErrorLog(@PathVariable("context") String context) {

        log.info("接收[error]日志,context: {}",context);
        logsServcie.sendErrorLog(context);
        return "日志内容：" + context;
    }
}
