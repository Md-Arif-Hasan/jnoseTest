package no.sdl.client.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.util.Date;
import no.scanpix.sdl.coreutils.media.DateUtil;
import org.junit.jupiter.api.Test;

class JsonColumnDataConverterTest {

  @Test
  void parseVerificationDate() {
    assertNull(DateUtil.convertToDate(null));
    assertNull(DateUtil.convertToDate(""));
    assertNull(DateUtil.convertToDate("  \n"));

    assertNotNull(DateUtil.convertToDate(Date.from(Instant.now())));
    assertNotNull(DateUtil.convertToDate("Thu Jun 27 19:42:00 BDT 2019"));
    assertNotNull(DateUtil.convertToDate("2011-12-03T10:15:30+01:00"));
    assertNotNull(DateUtil.convertToDate("2011-12-03T10:15:30Z"));
    assertNotNull(DateUtil.convertToDate("Tue, 3 Jun 2008 11:05:30 GMT"));
  }
}
