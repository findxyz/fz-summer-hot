import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.fz.Application;
import xyz.fz.service.async.AsyncService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2017/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class AsyncTest {

    @Autowired
    private AsyncService asyncService;

    @Test
    public void asyncTest() throws InterruptedException, IOException, TimeoutException, ExecutionException {
        List<Future<String>> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            Future<String> future = asyncService.longTimeWork();
            list.add(future);
        }
        for (Future<String> future : list) {
            System.out.println(future.get());
        }
        System.in.read();
    }
}
