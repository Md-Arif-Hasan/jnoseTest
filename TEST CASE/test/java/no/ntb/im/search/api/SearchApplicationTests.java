package no.ntb.im.search.api;

import org.junit.jupiter.api.Test;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SearchApplicationTests {

  @MockBean
  OpenSearchClient openSearchClient;

  @Test
  void contextLoads() {}
}