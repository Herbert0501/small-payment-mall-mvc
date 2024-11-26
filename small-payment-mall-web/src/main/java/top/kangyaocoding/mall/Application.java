package top.kangyaocoding.mall;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述: 启动类
 *
 * @author K·Herbert
 * @since 2024-11-26 下午9:04
 */

@SpringBootApplication
@Configurable
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}