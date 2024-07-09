package no.ntb.im.search.api.service.fetcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import no.ntb.im.model.IndexData;
import no.ntb.im.search.api.config.PreviewUrlConfig;
import no.ntb.im.search.api.helpers.OSRequestBuilder;
import no.ntb.im.search.api.helpers.utils.search.OSResponseParser;
import no.ntb.im.search.api.model.BaseSearchRequest;
import no.ntb.im.search.api.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.ShardStatistics;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.HitsMetadata;
import org.opensearch.client.opensearch.core.search.TotalHits;
import org.opensearch.client.opensearch.core.search.TotalHitsRelation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OSMediaFetcherTest {

  @MockBean OSRequestBuilder osRequestBuilder;
  @MockBean private OpenSearchClient openSearchClient;
  @MockBean private PreviewUrlConfig previewUrlConfig;

  private OSMediaFetcher osMediaFetcher;
  private SearchResponse<IndexData> fakeResponse;

  @BeforeEach
  void setUp() {
    osMediaFetcher = new OSMediaFetcher(osRequestBuilder, openSearchClient, previewUrlConfig);
    fakeResponse =
        new SearchResponse.Builder<IndexData>()
            .took(5)
            .timedOut(false)
            .shards(new ShardStatistics.Builder().failed(0).successful(1).total(10).build())
            .hits(
                new HitsMetadata.Builder<IndexData>()
                    .hits(Collections.emptyList())
                    .total(new TotalHits.Builder().value(0).relation(TotalHitsRelation.Eq).build())
                    .build())
            .build();
  }

  @Test
  public void testSearchSuccess() throws Exception {
    BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
    baseSearchRequest.setOffset(10);
    baseSearchRequest.setSearchSize(40);

    SearchRequest searchRequest = new SearchRequest.Builder().build();
    SearchResult searchResult = new SearchResult();

    when(osRequestBuilder.buildSearchRequest(baseSearchRequest)).thenReturn(searchRequest);
    when(openSearchClient.search(searchRequest, IndexData.class)).thenReturn(fakeResponse);

    try (var osResponseParser = Mockito.mockStatic(OSResponseParser.class)) {
      osResponseParser
          .when(() -> OSResponseParser.parse(fakeResponse, previewUrlConfig))
          .thenReturn(searchResult);

      Optional<SearchResult> result = osMediaFetcher.search(baseSearchRequest);

      assertTrue(result.isPresent());
      assertEquals(10, result.get().getOffset());
      assertEquals(40, result.get().getLimit());

      verify(osRequestBuilder).buildSearchRequest(baseSearchRequest);
      verify(openSearchClient).search(searchRequest, IndexData.class);
      osResponseParser.verify(() -> OSResponseParser.parse(fakeResponse, previewUrlConfig));
    }
  }
}
