package com.mproduits.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "mproduits", url = "localhost:900")
public interface MicroserviceProduitsProxy {

}
