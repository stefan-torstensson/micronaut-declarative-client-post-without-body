package declarative.empty.post;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.*;

import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class ApiClientTest {
    private static ApiClient clientApi;

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @BeforeClass
    public static void setUpClass() {
        var props = new HashMap<String, Object>();
        props.put("api.url", wireMockRule.baseUrl());
        var server = ApplicationContext.run(EmbeddedServer.class, props);
        clientApi = server.getApplicationContext().getBean(ApiClient.class);
    }

    @Before
    public void setUp() throws Exception {
        stubFor(post(urlEqualTo("/")).willReturn(okJson("response")));
    }

    @After
    public void tearDown() throws Exception {
        wireMockRule.resetAll();
    }

    @Test
    public void getTime_withPostData_shouldSetContentHeaders() {
        var response = clientApi.getTime("foo").blockingGet();

        verify(postRequestedFor(urlEqualTo("/"))
                .withHeader(HttpHeaders.CONTENT_LENGTH, equalTo("3"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo( MediaType.APPLICATION_JSON)));
    }

    @Test
    public void getTime_withoutPostData_shouldSetContentHeaders() {
        var response = clientApi.getTime().blockingGet();

        verify(postRequestedFor(urlEqualTo("/"))
                .withHeader(HttpHeaders.CONTENT_LENGTH, equalTo("0"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo( MediaType.APPLICATION_JSON)));
    }

}
