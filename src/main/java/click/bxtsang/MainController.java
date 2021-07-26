package click.bxtsang;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class MainController {
    @Inject
    UrlRecordRepository urlRecordRepository;

    @Get("/url/{hash}")
    public HttpResponse<String> redirect(String hash) throws URISyntaxException {

        String url = urlRecordRepository.getUrlFromHash(hash);
        URI location = new URI(url);

        return HttpResponse.redirect(location);
    }

//    @Get("/test")
//    public String testMethod() {
//        return urlRecordRepository.getUrlFromHash("test");
//    }
}
