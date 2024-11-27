package top.kangyaocoding.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import top.kangyaocoding.mall.weixin.IWeixinApiService;

/**
 * 描述: Retrofit2 配置
 *
 * @author K·Herbert
 * @since 2024-11-28 上午12:09
 */
@Configuration
public class Retrofit2Config {
    public static final String BASE_URL = "https://api.weixin.qq.com/";

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create()).build();
    }

    @Bean
    public IWeixinApiService weixinApiService(Retrofit retrofit) {
        return retrofit.create(IWeixinApiService.class);
    }

}
