package com.hhplus.reservation_concert.domain.user;

import com.hhplus.reservation_concert.domain.user.point.PointHistory;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.user.PointHistoryRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final RedissonClient redissonClient;
    private final UserRepositoryImpl userRepository;
    private final PointHistoryRepositoryImpl pointHistoryRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public PointHistory chargePoint(User user, Integer point) {
        PointHistory pointHistory = user.charge(point);

        return pointHistoryRepository.save(pointHistory);
    }

    public void usePoint(User user, Integer point) {
        PointHistory pointHistory = user.use(point);
        pointHistoryRepository.save(pointHistory);
    }

    public PointHistory usePointWithDistributedLock(Long id, Integer point) {
        User user = getUser(id);
        RLock lock = redissonClient.getLock("userLock: " + user.getId());

        try {
            if (lock.tryLock(5, 2, TimeUnit.SECONDS)) {
                PointHistory pointHistory = user.use(point);
                return pointHistoryRepository.save(pointHistory);
            } else {
                throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }
        } catch (InterruptedException e) {
            throw new CustomException(ErrorCode.LOCK_INTERRUPTED);
        } finally {
                lock.unlock();
        }
    }
}
