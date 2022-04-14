package org.programmers.kdtspringorder.voucher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FixedAmountVoucherTest {

    @Test
    void getVoucherId() {
    }

    @Test
    @DisplayName("주어진 금액만큼 할인은 해야한다.")
    void discount() {
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        assertEquals(900, fixedAmountVoucher.discount(1000));
    }

    @Test
    @DisplayName("할인된 금액은 음수가 될 수 없다.")
    void discountedToNegative() {
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 2000);
        assertEquals(0, fixedAmountVoucher.discount(1000));
    }

    @Test
    @DisplayName("유효한 할인 금액 바우처만 생성할 수 있다.")
    void discountByNegativeVoucher() {
        assertAll("FixAmountVoucher creation",
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), 900000))
                );

    }
}