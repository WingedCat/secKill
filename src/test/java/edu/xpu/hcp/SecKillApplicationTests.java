package edu.xpu.hcp;

import edu.xpu.hcp.service.IRedisService;
import edu.xpu.hcp.service.impl.UserKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecKillApplicationTests {

    @Autowired
    private IRedisService redisService;

    @Test
    public void test() {
        boolean s = redisService.set(UserKey.getById,"name", "feafea");
        System.out.println(s);
    }

}
