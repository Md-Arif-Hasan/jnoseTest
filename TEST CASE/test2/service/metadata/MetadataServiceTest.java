package no.sdl.client.service.metadata;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import lombok.val;
import no.scanpix.sdl.coreutils.media.model.metadata.VerificationTerm;
import org.junit.jupiter.api.Test;

class MetadataServiceTest {

  @Test
  void testCalculateVerificationTermsWithTitlesNDescriptions() {
    val titles = List.of("a", "b");
    val descriptions = List.of("c");

    val result = MetadataService.calculateVerificationTerms(true, titles, descriptions, null);
    assertEquals(2, result.size());
    verifyAllItemsOfType(result, VerificationTerm.class);
    var term = (VerificationTerm) result.get(0);
    assertEquals("a", term.getTitle());
    assertEquals("c", term.getDescription());

    term = (VerificationTerm) result.get(1);
    assertEquals("b", term.getTitle());
    assertNull(term.getDescription());
  }

  @Test
  void testCalculateVerificationTermsWithVerificationTerms() {
    List<Object> input =
        List.of(
            "a string value",
            new VerificationTerm().setTitle("a").setDescription("b"),
            "another string value");

    val result = MetadataService.calculateVerificationTerms(true, null, null, input);
    assertEquals(3, result.size());
    verifyAllItemsOfType(result, VerificationTerm.class);

    var item = (VerificationTerm) result.get(0);
    assertEquals("a string value", item.getTitle());
    assertNull(item.getDescription());

    item = (VerificationTerm) result.get(1);
    assertEquals("a", item.getTitle());
    assertEquals("b", item.getDescription());

    item = (VerificationTerm) result.get(2);
    assertEquals("another string value", item.getTitle());
    assertNull(item.getDescription());
  }

  @Test
  void testCalculateVerificationTermsAsSimpleStrings() {
    val titles = List.of("a", "b", "c");
    var result = MetadataService.calculateVerificationTerms(false, titles, null, null);
    verifyAllItemsOfType(result, String.class);
    assertEquals(titles, result);

    List<Object> terms = List.of(new VerificationTerm().setTitle("a").setDescription("b"));
    result = MetadataService.calculateVerificationTerms(false, null, null, terms);
    assertEquals(terms, result);
  }

  private void verifyAllItemsOfType(final List<?> result, final Class<?> clazz) {
    for (Object item : result) {
      assertEquals(item.getClass(), clazz);
    }
  }
}
