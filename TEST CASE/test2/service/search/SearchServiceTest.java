package no.sdl.client.service.search;

import static no.sdl.client.service.search.SearchService.hasStatusFilter;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
import lombok.val;
import no.sdl.client.models.SearchConfiguration;
import no.sdl.client.models.SearchFilter;
import org.junit.jupiter.api.Test;

class SearchServiceTest {

  @Test
  void testStatusFilterWithQuery() {
    val searchConfig = new SearchConfiguration();
    var filter = new SearchFilter().setName("test");
    searchConfig.setFilters(Map.of(1, filter));

    filter.setQuery(null);
    assertFalse(hasStatusFilter(searchConfig, Set.of(1)));

    filter.setQuery("status:unknown");
    assertFalse(hasStatusFilter(searchConfig, Set.of(1)));

    filter.setQuery("status:deleted");
    assertTrue(hasStatusFilter(searchConfig, Set.of(1)));

    filter.setQuery("keywords:NONE");
    assertFalse(hasStatusFilter(searchConfig, Set.of(1)));

    filter.setQuery("status:deleted AND keywords:OK");
    assertTrue(hasStatusFilter(searchConfig, Set.of(1)));

    filter.setQuery("keywords:OK AND status:visible");
    assertTrue(hasStatusFilter(searchConfig, Set.of(1)));

    filter.setQuery("keywords:OK OR status:hidden AND headline:NOTHING");
    assertTrue(hasStatusFilter(searchConfig, Set.of(1)));

    filter.setQuery("keywords:OK OR status:* AND headline:NOTHING");
    assertFalse(hasStatusFilter(searchConfig, Set.of(1)));
  }
}
