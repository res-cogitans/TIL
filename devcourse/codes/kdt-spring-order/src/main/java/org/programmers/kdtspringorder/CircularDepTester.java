package org.programmers.kdtspringorder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class CircularDepTester {

    public static void main(String[] args) {
        var ac = new AnnotationConfigApplicationContext(CircularConfig.class);
    }

//    @Configuration
    class CircularConfig {
        @Bean
        public ComponentA componentA(ComponentB componentB) {
            return new ComponentA(componentB);
        }
        @Bean
        public ComponentB componentB(ComponentC componentC) {
            return new ComponentB(componentC);
        }
        @Bean
        public ComponentC componentC(ComponentA componentA) {
            return new ComponentC(componentA);
        }
    }
    @RequiredArgsConstructor
    class ComponentA {
        private final ComponentB componentB;
    }
    @RequiredArgsConstructor
    class ComponentB {
        private final ComponentC componentC;

    }
    @RequiredArgsConstructor
    class ComponentC {
        private final ComponentA componentA;
    }
}
