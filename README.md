# 동시성 문제와 극복

## 좌석 예약
![image](https://github.com/user-attachments/assets/f0044839-8105-461d-9a14-031ab7ca7f7d)

![image](https://github.com/user-attachments/assets/b3a9028b-aa93-46ac-ab18-db8b003e893c)

![image](https://github.com/user-attachments/assets/46ba9818-41e2-4a12-9556-cc5e6f620b9f)

## 포인트 사용
![image](https://github.com/user-attachments/assets/a689144b-8ae4-4af1-bf1c-a628f883dd51)

![image](https://github.com/user-attachments/assets/a137b9b0-e031-4919-813f-a94e5a4c29b3)

![image](https://github.com/user-attachments/assets/3efd83d7-ea3f-4f14-82a5-25062d5a0d6c)

## 성능 비교
낙관적 락 > 비관적 락 > 분산 락(레디스) 순으로 성능 측정이 되었다.

동시성 테스트 경우 낙관적 락은 충돌로 인해 되돌리는 작업이 필요할텐데, 비관적 락보다 성능이 좋게 나온 이유를 잘 모르겠습니다..

충돌이 일어 난 경우 다시 되돌리는 작업이 필요한걸로 알고 있습니다..

## 사용
현재 서비스에서는 단일 DB를 쓸 것으로 예상 되어 분산 락은 필요 없을 것 같다.

포인트 사용의 경우 동시성 발생이 작아 버전 관리를 따로 할 필요 없이 비관적 락을 사용해도 될 것 같습니다.

좌석 예약의 경우 사용자의 수를 고려하여 락을 적용하면 되겠지만, 현재 서비스에서는 비관적 락을 사용해도 될 것으로 보입니다.
