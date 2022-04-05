package org.programmers.kdtspringorder.repository;

import org.programmers.kdtspringorder.voucher.Voucher;

import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {
    public Optional<Voucher> findById(UUID voucherId);
}
