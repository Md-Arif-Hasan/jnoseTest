package no.sdl.client.service.scheduler;

import static org.mockito.Mockito.when;

import java.util.List;
import no.scanpix.sdl.coreutils.DefaultObjectMapper;
import no.sdl.client.service.MediaDiffService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SystemInstructionDeleteHandlerTest {
  public static final int OWNER = 1;
  public static final String MEDIA_ID = "mock_id";

  @Mock MediaDiffService mediaDiffService;

  private SystemInstructionDeleteHandler systemInstructionDeleteHandler;

  @BeforeEach
  public void beforeEach() {
    systemInstructionDeleteHandler =
        new SystemInstructionDeleteHandler(null, mediaDiffService, DefaultObjectMapper.get());
  }

  @Test
  public void testNotEdited() {
    when(mediaDiffService.getMediaDiffs(OWNER, MEDIA_ID)).thenReturn(List.of());
    Assertions.assertFalse(systemInstructionDeleteHandler.isEdited(OWNER, MEDIA_ID));
  }
}
