package ru.ibs.gasu.provider;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by evgeniy on 28.11.18.
 */
@Controller
public class DispatcherProvider {

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

}
