package no.sdl.client.dto.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.Map;
import java.util.Set;
import lombok.val;
import no.scanpix.sdl.coreutils.DefaultObjectMapper;
import no.scanpix.sdl.coreutils.media.WriteMode;
import no.scanpix.sdl.coreutils.media.model.AbstractJsonColumnData.Fields;
import no.scanpix.sdl.coreutils.media.model.metadata.DetailedMetadataReference;
import no.scanpix.sdl.importer.data.PublishStatus;
import no.sdl.service.client.model.client.patch.PatchRequest.PatchItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PatchItemDeserializerTest {
  private static ObjectMapper objectMapper;

  @BeforeAll
  public static void setup() {
    val module = new SimpleModule();
    module.addDeserializer(PatchItem.class, new PatchItemDeserializer());

    objectMapper = DefaultObjectMapper.get();
    objectMapper.registerModule(module);
  }

  @Test
  void testDeserializeNormalStringField() throws JsonProcessingException {
    val patchItem = new PatchItem();
    patchItem.setField(Fields.description);
    patchItem.setMode(WriteMode.OVERWRITE);
    patchItem.setValue("some string");
    patchItem.setLocale("en");

    var jsonStr = objectMapper.writeValueAsString(patchItem);
    var result = objectMapper.readValue(jsonStr, PatchItem.class);

    assertEquals(patchItem, result);
  }

  @Test
  void testDeserializePublishStatus() throws JsonProcessingException {
    val patchItem = new PatchItem();
    patchItem.setField(Fields.status);
    patchItem.setMode(WriteMode.OVERWRITE);
    patchItem.setValue(PublishStatus.hidden);
    patchItem.setLocale(null);

    var jsonStr = objectMapper.writeValueAsString(patchItem);
    var result = objectMapper.readValue(jsonStr, PatchItem.class);

    assertEquals(patchItem, result);
  }

  @Test
  void testDeserializeDMR() throws JsonProcessingException {
    val patchItem = new PatchItem();
    patchItem.setField(Fields.persons);
    patchItem.setMode(WriteMode.APPEND);
    patchItem.setValue(Set.of(createDmr()));
    patchItem.setLocale(null);

    var jsonStr = objectMapper.writeValueAsString(patchItem);
    var result = objectMapper.readValue(jsonStr, PatchItem.class);

    assertEquals(patchItem, result);
  }

  @Test
  void testPatchItemWithNull() throws JsonProcessingException {
    val json =
        """
    {
      "field": "description",
      "value": "User",
      "mode": "OVERWRITE",
      "locale": null
    }
    """;

    val patchItem = objectMapper.readValue(json, PatchItem.class);
    assertEquals("User", patchItem.getValue());
    assertEquals(WriteMode.OVERWRITE, patchItem.getMode());
    assertNull(patchItem.getLocale());
  }

  private DetailedMetadataReference createDmr() {
    val dmr = new DetailedMetadataReference("");
    dmr.addField("id", 10);
    dmr.addField("typeId", 12);
    dmr.addField("stuff", Map.of("a", 100, "b", "c"));
    return dmr;
  }
}
