package ws.rabbitmq.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {

        int i = 10;
        i = ++i;
        System.out.println(i);
    }
}
