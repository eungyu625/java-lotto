package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;

import static lotto.Grade.*;

public class Application {

    private static ErrorMessage error;

    public static void main(String[] args) {
        // TODO: 프로그램 구현
        playLottoGame();
    }

    public static void playLottoGame() {
        try {
            printCashInputBox();
            int cash = getUserCash(inputUserCash());
            List<Lotto> lottoList = new ArrayList<>();
            issueLottoNumbers(cash / 1000, lottoList);
            showLotto(cash, lottoList);
            printWinningNumbersInputBox();
            Lotto winningNumberList = new Lotto(getWinningNumbers(inputWinningNumbers()));
            printBonusNumberInputBox();
            int bonusNumber = getBonusNumber(inputBonusNumber(), winningNumberList.getNumbers());
            showStats(lottoList, winningNumberList.getNumbers(), bonusNumber, cash);
        } catch (IllegalArgumentException e) {
            System.out.println(error.getMessage());
        }
    }

    public static void showLotto(int cash, List<Lotto> lottoList) {

        printLottoQuantity(cash / 1000);
        for (Lotto lotto : lottoList) {
            printLottoNumbers(lotto.getNumbers());
        }
    }

    public static void showStats(List<Lotto> lottoList, List<Integer> winningNumberList, int bonusNumber, int cash) {

        printStatsPhrase();
        printGradeStats(lottoList, winningNumberList, bonusNumber, cash);
    }

    public static void printLottoQuantity(int quantity) {

        System.out.println();
        System.out.println(quantity + "개를 구매했습니다.");
    }

    public static void printLottoNumbers(List<Integer> lottoNumberList) {

        System.out.print("[");

        for (int i = 0; i < lottoNumberList.size() - 1; i++) {
            System.out.print(lottoNumberList.get(i) + ", ");
        }

        System.out.println(lottoNumberList.get(lottoNumberList.size() - 1) + "]");
    }

    public static void printWinningNumbersInputBox() {

        System.out.println();
        System.out.println("당첨 번호를 입력해 주세요.");
    }

    public static void printBonusNumberInputBox() {

        System.out.println();
        System.out.println("보너스 번호를 입력해 주세요.");
    }

    public static void printCashInputBox() {

        System.out.println("구입 금액을 입력해 주세요.");
    }

    public static void printStatsPhrase() {

        System.out.println();
        System.out.println("당첨 통계");
        System.out.println("---");
    }

    public static void printGradeStats(List<Lotto> lottoList, List<Integer> winningNumberList, int bonusNumber, int cash) {
        int first = calculateFirst(lottoList, winningNumberList);
        int second = calculateSecond(lottoList, winningNumberList, bonusNumber);
        int third = calculateThird(lottoList, winningNumberList, bonusNumber);
        int forth = calculateForth(lottoList, winningNumberList);
        int fifth = calculateFifth(lottoList, winningNumberList);
        double surplusRate = calculateSurplus(first, second, third, forth, fifth, cash);
        System.out.println("3개 일치 (5,000원) - " + fifth + "개");
        System.out.println("4개 일치 (50,000원) - " + forth + "개");
        System.out.println("5개 일치 (1,500,000원) - " + third + "개");
        System.out.println("5개 일치, 보너스 볼 일치 (30,000,000원) - " + second + "개");
        System.out.println("6개 일치 (2,000,000,000원) - " + first + "개");
        System.out.println("총 수익률은 " + surplusRate + "%입니다.");
    }

    public static void validateCashIsInteger(String input) {

        for (int i = 0; i < input.length(); i++) {

            if (input.charAt(i) < 48 || input.charAt(i) > 57) {

                throw new IllegalArgumentException();
            }
        }
    }

    public static void validateCashIsDividedThousand(String input) {

        if (input.length() < 4) {
            throw new IllegalArgumentException();
        }

        for (int i = input.length() - 3; i < input.length(); i++) {

            if (input.charAt(i) != 48) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static Integer changeCashStringToInteger(String input) {
        int inputCash = 0;

        for (int i = 0; i < input.length(); i++) {

            int number = input.length() - i;
            inputCash += Math.pow(10, number - 1) * (input.charAt(i) - 48);
        }

        return inputCash;

    }

    public static String inputUserCash() {

        return Console.readLine();
    }

    public static Integer getUserCash(String input) {

        validateCashIsInteger(input);
        validateCashIsDividedThousand(input);

        return changeCashStringToInteger(input);
    }

    public static String inputWinningNumbers() {

        return Console.readLine();
    }

    public static void validateNumbersBetweenComma(char character1, char character2) {

        if ((character1 < 48 || character1 > 57) || (character2 < 48 || character2 > 57)) {

            throw new IllegalArgumentException();
        }

    }

    public static void validateWinningNumbersForm(String input) {

        for (int i = 0; i < input.length(); i++) {

            if (input.charAt(i) != ',' && (input.charAt(i) < 48 || input.charAt(i) > 57)) {
                throw new IllegalArgumentException();
            }
            if (input.charAt(i) == ',') {
                validateNumbersBetweenComma(input.charAt(i - 1), input.charAt(i + 1));
            }
        }

    }

    public static List<Integer> getWinningNumbers(String input) {
        validateWinningNumbersForm(input);
        List<Integer> winningNumberList = new ArrayList<>();
        int number = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ',') {
                winningNumberList.add(number);
                number = 0;
                continue;
            }
            number = number * 10 + input.charAt(i) - 48;
        }
        winningNumberList.add(number);
        return winningNumberList;
    }

    public static String inputBonusNumber() {

        return Console.readLine();
    }

    public static void validateBonusNumberIsInteger(String input) {

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) < 48 || input.charAt(i) > 57) {

                throw new IllegalArgumentException();
            }
        }
    }

    public static void validateBonusNumberIsInRange(String input) {
        int bonusNumber = 0;

        for (int i = 0; i < input.length(); i++) {
            bonusNumber = bonusNumber * 10 + input.charAt(i) - 48;
        }

        if (bonusNumber < 1 || bonusNumber > 45) {

            throw new IllegalArgumentException();
        }
    }

    public static void validateBonusNumberIsDuplicate(String input, List<Integer> winningNumberList) {
        int bonusNumber = 0;

        for (int i = 0; i < input.length(); i++) {
            bonusNumber = bonusNumber * 10 + input.charAt(i) - 48;
        }
        if (winningNumberList.contains(bonusNumber)) {
            throw new IllegalArgumentException();
        }
    }

    public static Integer getBonusNumber(String input, List<Integer> winningNumberList) {

        validateBonusNumberIsInteger(input);
        validateBonusNumberIsInRange(input);
        validateBonusNumberIsDuplicate(input, winningNumberList);

        int bonusNumber = 0;

        for (int i = 0; i < input.length(); i++) {
            bonusNumber = bonusNumber * 10 + input.charAt(i) - 48;
        }

        return bonusNumber;
    }

    public static Double calculateSurplus(int first, int second, int third, int forth, int fifth, int cash) {

        int surplusSum = first * FIRST.getValue() + second * SECOND.getValue()
                + third * THIRD.getValue() + forth * FORTH.getValue() + fifth * FIFTH.getValue();

        double surplusRate = 100.0 * (double) surplusSum / (double) cash;

        return Math.round(surplusRate * 10) / 10.0;
    }

    public static Integer compareLottoNumbersAndWinningNumbers(Lotto lotto, List<Integer> winningNumberList, int equalNum) {

        int number = 0;

        for (int winningNumber : winningNumberList) {

            if (lotto.checkWinningNumberIsInLottoNumbers(winningNumber)) {

                number += 1;
            }
        }

        if (number == equalNum) return 1;

        return 0;
    }

    public static Integer calculateFirst(List<Lotto> lottoList, List<Integer> winningNumberList) {

        int number = 0;

        for (Lotto lotto : lottoList) {

            number += compareLottoNumbersAndWinningNumbers(lotto, winningNumberList, 6);
        }

        return number;
    }

    public static Integer calculateSecond(List<Lotto> lottoList, List<Integer> winningNumberList, int bonusNumber) {

        int number = 0;

        for (Lotto lotto : lottoList) {

            if (!lotto.checkWinningNumberIsInLottoNumbers(bonusNumber)) {

                continue;
            }
            number += compareLottoNumbersAndWinningNumbers(lotto, winningNumberList, 5);
        }
        return number;
    }

    public static Integer calculateThird(List<Lotto> lottoList, List<Integer> winningNumberList, int bonusNumber) {

        int number = 0;

        for (Lotto lotto : lottoList) {

            if (lotto.checkWinningNumberIsInLottoNumbers(bonusNumber)) {

                continue;
            }
            number += compareLottoNumbersAndWinningNumbers(lotto, winningNumberList, 5);
        }
        return number;
    }

    public static Integer calculateForth(List<Lotto> lottoList, List<Integer> winningNumberList) {

        int number = 0;

        for (Lotto lotto : lottoList) {

            number += compareLottoNumbersAndWinningNumbers(lotto, winningNumberList, 4);
        }

        return number;
    }

    public static Integer calculateFifth(List<Lotto> lottoList, List<Integer> winningNumberList) {

        int number = 0;

        for (Lotto lotto : lottoList) {

            number += compareLottoNumbersAndWinningNumbers(lotto, winningNumberList, 3);
        }

        return number;
    }

    public static void issueLottoNumbers(int buyNumber, List<Lotto> lottoList) {

        for (int i = 0; i < buyNumber; i++) {
            lottoList.add(new Lotto(Randoms.pickUniqueNumbersInRange(1, 45, 6)));
        }
    }
}
