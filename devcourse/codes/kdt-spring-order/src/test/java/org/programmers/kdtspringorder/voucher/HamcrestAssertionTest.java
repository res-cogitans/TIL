package org.programmers.kdtspringorder.voucher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class HamcrestAssertionTest {

    @Test
    @DisplayName("여러 hamcrest matcher 테스트")
    void hamcrestTest() {
        int actual = 1 + 2;
        assertThat(actual, equalTo(3));
        assertThat(actual, is(3));
        assertThat(actual, anyOf(is(2), is(4)));

        //assertNotEquals junit5 jupiter 기능
        assertThat(1+1, not(equalTo(1)));
    }

    @Test
    @DisplayName("컬렉션에 대한 matcher 테스트")
    void hamcrestListMatcherTest() {
        var prices = List.of(2, 3, 4);
        assertThat(prices, hasSize(3));
        assertThat(prices, everyItem(greaterThan(1)));
        assertThat(prices, containsInAnyOrder(3, 2, 4));
        assertThat(prices, hasItem(greaterThan(2)));
    }
}
