package no.ntb.im.search.api.service;

import static org.junit.jupiter.api.Assertions.*;

import no.ntb.im.model.constants.MediaArchive;
import no.ntb.im.model.constants.MediaFamily;
import no.ntb.im.search.api.errors.exceptions.UnprocessableEntityException;
import no.ntb.im.search.api.model.BaseSearchRequest;
import no.ntb.im.search.api.service.fetcher.MediaFetcherResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class SearchServiceTest {

  private MediaSearchService searchService;
  @MockBean MediaFetcherResolver mediaFetcherResolver;

  @BeforeEach
  void setUp() {
    searchService = new MediaSearchService(mediaFetcherResolver);
  }

  @Test
  void testUnsupportedArchiveInSearchRequest() {
    var searchRequest = new BaseSearchRequest();
    searchRequest.setMediaFamily(MediaFamily.editorial).setArchive(MediaArchive.shutterStock);
    assertThrowsExactly(
        UnprocessableEntityException.class, () -> searchService.performSearch(searchRequest));
  }

  @Test
  void testSupportedArchiveInSearchRequest() {
    var searchRequest = new BaseSearchRequest();
    searchRequest.setMediaFamily(MediaFamily.editorial).setArchive(MediaArchive.sdl);
    assertDoesNotThrow(() -> searchService.performSearch(searchRequest));
  }
}
