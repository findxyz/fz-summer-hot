package xyz.fz.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2015-5-29.
 */
@ControllerAdvice
public class ExceptionAdviceController {

    private static final Logger logger = Logger.getLogger(ExceptionAdviceController.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map<String, Object> processException(Exception e) throws IOException {

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "error【" + e.getMessage() + "】");
        return result;
    }
}
