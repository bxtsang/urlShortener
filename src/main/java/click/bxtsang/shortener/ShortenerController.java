package click.bxtsang.shortener;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;
import java.util.Map;

@Controller("/shortener")
public class ShortenerController {

    @Inject
    ShortenerService shortenerService;

    @Post("/create")
    public String shortenUrl(Map<String, String> body) {
        // can do some error handling here

        return shortenerService.shortenUrl(body.get("url"));
    }
}
