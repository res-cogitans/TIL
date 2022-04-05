package org.programmers.kdtspringorder.service;

import lombok.RequiredArgsConstructor;
import org.programmers.kdtspringorder.repository.VoucherRepository;
import org.programmers.kdtspringorder.voucher.Voucher;

import java.text.MessageFormat;
import java.util.UUID;

@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public Voucher getVoucher(UUID voucherId) {
        return voucherRepository.findById(voucherId).orElseThrow(
                () -> new RuntimeException(
                        MessageFormat.format("Cannot find a voucher for {0}", voucherId)));
    }

    public void useVoucher() {
    }
}
