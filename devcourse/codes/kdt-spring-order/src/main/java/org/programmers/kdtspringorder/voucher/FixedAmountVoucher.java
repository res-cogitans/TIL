package org.programmers.kdtspringorder.voucher;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class FixedAmountVoucher implements Voucher {

    private final UUID voucherId;
    private final long amount;

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    public long discount(long beforeDiscount) {
        return beforeDiscount - amount;
    }
}
