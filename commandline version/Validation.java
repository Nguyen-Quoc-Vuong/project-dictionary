import java.util.Scanner;

public class Validation {
    private static Scanner sc = new Scanner(System.in);

    public static int getInt(String mess, int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        while (true) {
            try {
                System.out.print(mess);
                int n = Integer.parseInt(sc.nextLine());
                if (min <= n && n <= max) return n;
                System.err.println("Vui lòng nhập 1 số từ " + min + " -> " + max);
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập định dạng số");
            }
        }
    }

    public static String getString(String mess) {
        while (true) {
            System.out.print(mess);
            String s = sc.nextLine();
            if (!s.isEmpty()) return s;
            System.err.println("< Dữ liệu không được để trống ! >");
        }
    }

    public static boolean getYN(String mess) {
        while (true) {
            System.out.print(mess);
            String s = sc.nextLine();
            if (s.equalsIgnoreCase("y")) {
                return true;
            } else if (s.equalsIgnoreCase("n")) return false;

            System.err.println("Nhập Y hoặc N !!");
        }
    }
}
