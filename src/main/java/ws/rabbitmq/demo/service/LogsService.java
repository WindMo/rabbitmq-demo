package ws.rabbitmq.demo.service;

/**
 * @author WindShadow
 * @version 2021-08-06.
 */

public interface LogsService {

    /**
     * 发送info级别日志
     * @param context
     */
    void sendInfoLog(String context);


    /**
     * 发送warn级别日志
     * @param context
     */
    void sendWarnLog(String context);


    /**
     * 发送error级别日志
     * @param context
     */
    void sendErrorLog(String context);

}
