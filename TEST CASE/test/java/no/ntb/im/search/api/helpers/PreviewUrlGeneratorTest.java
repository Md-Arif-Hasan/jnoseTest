package no.ntb.im.search.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import no.ntb.im.search.api.config.PreviewUrlConfig;
import no.ntb.im.search.api.helpers.utils.preview.PreviewUrlGenerator;
import no.ntb.im.search.api.model.constants.FileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PreviewUrlGeneratorTest {
  private static final String BASE_URL = "https://preview.com/";
  private PreviewUrlConfig.EncryptionKey key;

  @BeforeEach
  public void setUp() {
    key = new PreviewUrlConfig.EncryptionKey();
    key.setKey(PreviewUrlConfig.EncKeyName.no);
    key.setValue("yM1sKsdM9WtRVhRwbMKlVgjjklk");
  }

  @Test
  public void test_image_preview_urls() {
    String sampleUrl1 = BASE_URL + "v2/no/_vG7fZwIWg5lSRDPhWZagA/XzP0a-ZW1ZY";
    String sampleUrl2 = BASE_URL + "v2/no/Zi80E2EMsbSe2Idyogs34A/pR4ysQZjpAQ";
    String sampleUrl3 = BASE_URL + "v2/no/K8mFJn9iHoe6KG7Bd4VICw/tlZwLagEkkc";

    assertEquals(
        sampleUrl1,
        PreviewUrlGenerator.url(BASE_URL, key, "XzP0a-ZW1ZY", FileType.PREVIEW_256, false));
    assertEquals(
        sampleUrl2,
        PreviewUrlGenerator.url(BASE_URL, key, "pR4ysQZjpAQ", FileType.PREVIEW_512, false));
    assertEquals(
        sampleUrl3,
        PreviewUrlGenerator.url(BASE_URL, key, "tlZwLagEkkc", FileType.PREVIEW_1024, false));
  }

  @Test
  public void test_urls_with_watermark() {
    assertEquals(
        BASE_URL + "v2/no/B5-R1aD7n46_r_m-RxfMgw/tlZwLagEkkc",
        PreviewUrlGenerator.url(BASE_URL, key, "tlZwLagEkkc", FileType.PREVIEW_1024, true));
    assertEquals(
        BASE_URL + "v2/no/F7w7wXyHf0hggSoBQjCBmw/tlZwLagEkkc",
        PreviewUrlGenerator.url(BASE_URL, key, "tlZwLagEkkc", FileType.VIDEO_PREVIEW_512, true));
  }

  @Test
  public void test_video_preview_urls() {
    String sampleUrl1 = BASE_URL + "v2/no/rKAwZOwSuTD6ODD3HUvBfg/pR4ysQZjpAQ";
    String sampleUrl2 = BASE_URL + "v2/no/BVOALBcYPiO8AgZ6Xon8nQ/XzP0a-ZW1ZY";
    assertEquals(
        sampleUrl1,
        PreviewUrlGenerator.url(BASE_URL, key, "pR4ysQZjpAQ", FileType.VIDEO_PREVIEW_512, false));
    assertEquals(
        sampleUrl2,
        PreviewUrlGenerator.url(BASE_URL, key, "XzP0a-ZW1ZY", FileType.VIDEO_PREVIEW_256, false));
  }
}
