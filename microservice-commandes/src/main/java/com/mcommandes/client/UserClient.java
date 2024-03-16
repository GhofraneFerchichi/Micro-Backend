package com.mcommandes.client;

import com.mcommandes.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${spring.config.user-url}")

public interface UserClient {
    @GetMapping("/user")
    User getUserById(@RequestParam("userId") Long userId);
}
