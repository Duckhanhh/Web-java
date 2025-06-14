package org.bpm.abcbook.config;

import org.bpm.abcbook.filter.NoCacheFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class NoCacheFilterConfig {
    @Bean
    public FilterRegistrationBean<NoCacheFilter> noCacheFilter() {
        FilterRegistrationBean<NoCacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new NoCacheFilter());
        registrationBean.addUrlPatterns("/*"); // Áp dụng cho tất cả các URL
        return registrationBean;
    }
}
