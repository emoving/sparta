package com.hhplus.reservation_concert.domain.token;

import java.util.Map;
import java.util.Set;

public interface TokenRepository {

    void saveWaitingToken(String token);

    Set<String> getTopWaitingTokens(int count);

    void removeTopWaitingTokens(int count);

    void saveActiveToken(String token);

    Long getActiveTokenCount();

    Map<Object, Object> getActiveTokens();

    boolean hasActiveToken(String token);

    void removeActiveToken(String token);
}
