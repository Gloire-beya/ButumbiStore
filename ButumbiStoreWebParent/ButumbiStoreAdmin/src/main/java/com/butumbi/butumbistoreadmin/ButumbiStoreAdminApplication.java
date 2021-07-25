package com.butumbi.butumbistoreadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.butumbi.butumbistorecommon.entity")
public class ButumbiStoreAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ButumbiStoreAdminApplication.class, args);
    }

}
