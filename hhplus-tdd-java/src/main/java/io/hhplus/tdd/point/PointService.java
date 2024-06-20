package io.hhplus.tdd.point;

import io.hhplus.tdd.point.PointHistoryDto.PointHistoryListResponse;
import io.hhplus.tdd.point.UserPointDto.UserPointResponse;

public interface PointService {

    UserPointResponse getPoint(long id);

    PointHistoryListResponse getHistory(long id);

    UserPointResponse chargePoint(long id, long amount);

    UserPointResponse usePoint(long id, long amount);
}