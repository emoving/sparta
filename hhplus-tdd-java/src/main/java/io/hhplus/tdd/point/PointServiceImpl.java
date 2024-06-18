package io.hhplus.tdd.point;


import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointHistoryTable pointHistoryTable;
    private final UserPointTable userPointTable;

    @Override
    public UserPoint getPoint(long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public List<PointHistory> getHistory(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    @Override
    public UserPoint chargePoint(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint chargedUserPoint = userPoint.charge(amount);

        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return userPointTable.insertOrUpdate(id, chargedUserPoint.point());
    }

    @Override
    public UserPoint usePoint(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint usedUserPoint = userPoint.use(amount);

        pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        return userPointTable.insertOrUpdate(id, usedUserPoint.point());
    }
}