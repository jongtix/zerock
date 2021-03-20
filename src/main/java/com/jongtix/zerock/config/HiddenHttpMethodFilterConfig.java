package com.jongtix.zerock.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Arrays;

//@Configuration
public class HiddenHttpMethodFilterConfig { //html form tag에서 PATH, DELETE 등의 method 사용이 불가한 문제 해결을 위한 filter 적용
                                            //application.yml에서 spring.mvc.hiddenmethod.filter.enabled=true 옵션으로 해결 가능

//    @Bean
//    public FilterRegistrationBean hiddenHttpMethodFilter() {
//        FilterRegistrationBean filterRegBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
//        filterRegBean.setUrlPatterns(Arrays.asList("/*"));
//        return filterRegBean;
//    }

}
