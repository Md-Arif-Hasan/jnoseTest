package no.ntb.im.search.api.helpers;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import lombok.val;
import no.ntb.im.model.constants.MediaArchive;
import no.ntb.im.model.constants.MediaFamily;
import no.ntb.im.search.api.helpers.utils.search.SearchQueryBuilder;
import no.ntb.im.search.api.model.BaseSearchRequest;
import org.junit.jupiter.api.Test;
import org.opensearch.client.json.jackson.JacksonJsonProvider;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class SearchQueryBuilderTest {

  @MockBean OpenSearchClient openSearchClient;

  @Test
  void testQueryBuilder() throws IOException {
    val expectedQuery = readString(Path.of("src", "test", "resources", "expected-query.json"));

    var searchRequest =
        new BaseSearchRequest()
            .setMediaFamily(MediaFamily.editorial)
            .setArchive(MediaArchive.sdl)
            .setSearchString("Oslo");

    Query query = SearchQueryBuilder.buildQuery(searchRequest, MediaFamily.editorial);
    // Must be a bool query
    assertTrue(query.isBool());

    StringWriter writer = new StringWriter();
    JsonGenerator jsonGenerator = JacksonJsonProvider.provider().createGenerator(writer);
    query.serialize(jsonGenerator, new JacksonJsonpMapper());
    jsonGenerator.flush();

    // Compare the expected query
    val actualQuery = writer.toString();
    val objectMapper = new ObjectMapper();
    assertEquals(objectMapper.readTree(expectedQuery), objectMapper.readTree(actualQuery));
  }
}
