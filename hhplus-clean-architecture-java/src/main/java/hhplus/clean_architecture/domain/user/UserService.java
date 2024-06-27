package hhplus.clean_architecture.domain.user;

import hhplus.clean_architecture.global.BaseException;
import hhplus.clean_architecture.global.ErrorCode;
import hhplus.clean_architecture.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User find(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
