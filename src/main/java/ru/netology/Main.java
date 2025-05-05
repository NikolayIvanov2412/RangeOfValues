package ru.netology;

import java.util.*;
import java.lang.Thread;

public class Main {

    public static void main(String[] args) throws Exception {
        String[] texts = new String[25]; // Массив строк
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000); // Генерация строк
        }

        List<Thread> threads = new ArrayList<>(); // Список для хранения потоков

        long startTs = System.currentTimeMillis(); // Время старта

        // Цикл для запуска каждого текста в отдельном потоке
        for (final String text : texts) {
            Thread t = new Thread(() -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                synchronized (System.out) {
                    System.out.println(text.substring(0, 100) + " -> " + maxSize);
                }
            });
            threads.add(t); // Сохраняем поток в списке
            t.start(); // Запускаем поток
        }

        // Ждем завершения всех потоков
        for (Thread thread : threads) {
            thread.join(); // Блокируем основной поток, пока все потоки не закончат работу
        }

        long endTs = System.currentTimeMillis(); // Время окончания

        System.out.println("Time: " + (endTs - startTs) + " ms"); // Печать затраченного времени
    }

    // Функция генерации случайной строки
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}