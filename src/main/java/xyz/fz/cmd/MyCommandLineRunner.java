package xyz.fz.cmd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner{

    /*
    @Autowired
    private SomeService someService;
    */

    @Override
    public void run(String... strings) throws Exception {
        // do what you want to do when spring context has already

        // if want awt or swing then replace
        // SpringApplication.run(Application.class, args);
        // -->
        // new SpringApplicationBuilder(Application.class).headless(false).run(args);
    }
}
