package no.sdl.client.service.perf;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.redisson.api.RedissonClient;

class UpdateRateLimiterTest {

  @Test
  void testCalculateDesiredRate() {
    val mockRedisson = Mockito.mock(RedissonClient.class);
    val updateRateLimiter =
        new UpdateRateLimiter(mockRedisson, List.of(5000, 500, 2500, 1000, 1000, 2000));

    assertEquals(500, updateRateLimiter.calculateDesiredRate(5000));
    assertEquals(1_000, updateRateLimiter.calculateDesiredRate(4000));
    assertEquals(1_000, updateRateLimiter.calculateDesiredRate(3000));
    assertEquals(2_000, updateRateLimiter.calculateDesiredRate(2000));
    assertEquals(2_000, updateRateLimiter.calculateDesiredRate(1000));
    assertEquals(UpdateRateLimiter.UNLIMITED_RATE, updateRateLimiter.calculateDesiredRate(100));
  }
}
