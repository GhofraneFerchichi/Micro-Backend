package com.mproduits.client;

import com.mproduits.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${spring.config.user-url}")

public interface UserClient {
    @GetMapping("/user/{id}")
    User getUserById(@PathVariable("userId") Long userId);

}
