package com.demo.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class RandomNumber {
    public static final int MEGA = 45;
    public static final int POWER = 55;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            log.info("Lần {}:", i + 1);
            randomNumber();
            randomWithBound();
        }

    }

    public static List<Integer> getLuckyNumber(int prizeType) {
        List<Integer> luckyNumber = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            int randomNumber = (int) Math.round(Math.random() * prizeType);
            if (luckyNumber.contains(randomNumber) || randomNumber == 0) {
                i--;
                continue;
            }
            luckyNumber.add(randomNumber);
        }
        luckyNumber.sort(Integer::compareTo);
        return luckyNumber;
    }

    public static void randomNumber() {
        log.info("Random number");
        log.info("Lucky number is: {}  (MEGA)", getLuckyNumber(MEGA));
        log.info("Lucky number is: {}  (POWER)", getLuckyNumber(POWER));
    }

    public static void randomWithBound() {
        log.info("Random number with bound");
        log.info("Lucky number is: {}  (POWER)", getLuckyNumberWithBound(POWER));
        log.info("Lucky number is: {}  (MEGA)", getLuckyNumberWithBound(MEGA));
    }

    private static String getLuckyNumberWithBound(int limit) {
        Random ran = new Random();
        String first = String.format("%02d", ran.nextInt(1, 20));
        String second = String.format("%02d", ran.nextInt(1, 30));
        String third = String.format("%02d", ran.nextInt(1, limit));
        String fourth = String.format("%02d", ran.nextInt(1, limit));
        String fifth = String.format("%02d", ran.nextInt(1, limit));
        String sixth = String.format("%02d", ran.nextInt(1, limit));
        return String.format("%s - %s - %s - %s - %s - %s", first, second, third, fourth, fifth, sixth);
    }
}

