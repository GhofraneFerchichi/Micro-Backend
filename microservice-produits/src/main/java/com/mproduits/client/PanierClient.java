package com.mproduits.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mpanier", url = "${application.config.mpanier-url}")
public interface PanierClient {

    @PostMapping("/add/{panierId}/{productId}")
    public void addProductToPanier(@PathVariable int panierId, @PathVariable int productId);
}
