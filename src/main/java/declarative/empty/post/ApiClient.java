package declarative.empty.post;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

@Client("${api.url}")
public interface ApiClient {
    @Post
    Single<String> getTime();

    @Post
    Single<String> getTime(@Body String tz);
}
