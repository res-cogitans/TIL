package org.programmers.kdtspringorder.domain;

import java.util.UUID;

public record OrderItem(UUID productId,
                        long productPrice,
                        long quantity) {}