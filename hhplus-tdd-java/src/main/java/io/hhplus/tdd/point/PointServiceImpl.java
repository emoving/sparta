package io.hhplus.tdd.point;


import io.hhplus.tdd.point.PointHistoryDto.PointHistoryListResponse;
import io.hhplus.tdd.point.UserPointDto.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointHistoryRepository pointHistoryRepository;
    private final UserPointRepository userPointRepository;

    @Override
    public UserPointResponse getPoint(long id) {
        UserPoint userPoint = userPointRepository.selectById(id);

        return UserPointResponse.builder()
                .id(userPoint.id())
                .point(userPoint.point())
                .updateMillis(userPoint.updateMillis())
                .build();
    }

    @Override
    public PointHistoryListResponse getHistory(long id) {
        List<PointHistory> pointHistories = pointHistoryRepository.selectAllByUserId(id);

        return PointHistoryListResponse.builder().pointHistoryList(pointHistories).build();
    }

    @Override
    public synchronized UserPointResponse chargePoint(long id, long amount) {
        UserPoint userPoint = userPointRepository.selectById(id);
        UserPoint chargedUserPoint = userPoint.charge(amount);

        pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        UserPoint savedUserPoint = userPointRepository.insertOrUpdate(id, chargedUserPoint.point());

        return UserPointResponse.builder()
                .id(savedUserPoint.id())
                .point(savedUserPoint.point())
                .updateMillis(savedUserPoint.updateMillis())
                .build();
    }

    @Override
    public synchronized UserPointResponse usePoint(long id, long amount) {
        UserPoint userPoint = userPointRepository.selectById(id);
        UserPoint usedUserPoint = userPoint.use(amount);

        pointHistoryRepository.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        UserPoint savedUserPoint = userPointRepository.insertOrUpdate(id, usedUserPoint.point());

        return UserPointResponse.builder()
                .id(savedUserPoint.id())
                .point(savedUserPoint.point())
                .updateMillis(savedUserPoint.updateMillis())
                .build();
    }
}