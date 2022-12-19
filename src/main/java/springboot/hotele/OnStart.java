package springboot.hotele;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import springboot.hotele.repository.GoscRepo;

@Configuration
public class OnStart {
    @Autowired GoscRepo goscRepo;

   
        
}
