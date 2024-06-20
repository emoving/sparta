package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint charge(long amount) {
        if (amount <= 0 ) throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");

        return new UserPoint(this.id, this.point + amount, System.currentTimeMillis());
    }

    public UserPoint use(long amount) {
        if (amount > this.point) throw new IllegalArgumentException("잔액이 부족합니다.");

        return new UserPoint(this.id, this.point - amount, System.currentTimeMillis());
    }
}
