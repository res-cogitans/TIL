package org.programmers.kdtspringjdbc.repository.voucher;

import org.programmers.kdtspringjdbc.voucher.Voucher;

import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {
    Optional<Voucher> findById(UUID voucherId);
    Voucher insert(Voucher voucher);
}
