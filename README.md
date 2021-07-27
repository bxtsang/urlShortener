## A Simple URL Shortener
![img.png](img.png)

Built and deployed with AWS resources, using [Micronaut](https://micronaut.io/).

All generated urls follow the pattern *http://bxtsang.click/url/<hash>*

Go to the [deployed frontend](http://bxtsang.click/home/index.html).

## To run locally

1. Clone the repo, from the root folder (in terminal)
2. Add environment variables for `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION` with personal AWS credentials
3. Set environment variable `MICRONAUT_ENVIRONMENTS` to "local" *(change value to "live" when deploying)*
4. Update url in line 59 of `src/main/resources/public/index.html` to `http://localhost:8080/shortener/create`.
5. run `./gradlew run`

In local mode, all generated urls follow the pattern *http://localhost:8080/url/<hash>*

## To test
Some tests have been added for the ShortenerService, including unit and integration tests.

1. run `./gradlew test`
2. open `build/reports/tests/test/index.html` in browser to see full details


### Note:
There is one failing test under `src/test/click.bxtsang.ShortenerIntegrationTest`</br>
This seems to be due to a bug in the Micronaut framework, where the HttpClient provided by the framework is unable to make requests with relative URL paths, and seems to lookup on the wrong host when providing a host in the URI.</br>
On the docs, it should be able to use a relative URL path... will be submitting an issue 😬
