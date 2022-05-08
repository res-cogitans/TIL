package org.programmers.kdtspringjdbc.voucher;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class PercentDiscountVoucher implements Voucher {

    private final UUID voucherId;
    private final long percent;

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    public long discount(long beforeDiscount) {
        return beforeDiscount * (percent / 100);

    }
}
