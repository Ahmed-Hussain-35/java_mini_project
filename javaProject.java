import java.util.Scanner;

class JavaProject {
    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }

    static boolean isValidDate(int day, int month, int year) {
        if (year <= 0 || month < 1 || month > 12 || day < 1) return false;
        switch (month) {
            case 1, 3, 5, 7, 8, 10, 12:
                return day <= 31;
            case 4, 6, 9, 11:
                return day <= 30;
            case 2:
                return day <= (isLeapYear(year) ? 29 : 28);
            default:
                return false;
        }
    }

    static int[] parseDate(String[] dateParts) {
        try {
            return new int[] {
                Integer.parseInt(dateParts[0]),
                Integer.parseInt(dateParts[1]),
                Integer.parseInt(dateParts[2])
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static int[] calculateAge(int day, int month, int year, int refDay, int refMonth, int refYear) {
        int[] age = new int[3];
        age[0] = refDay - day;
        age[1] = refMonth - month;
        age[2] = refYear - year;
        if (age[0] < 0) {
            age[0] += daysInMonth(refMonth - 1, refYear);
            age[1]--;
        }
        if (age[1] < 0) {
            age[1] += 12;
            age[2]--;
        }
        return age;
    }

    static int[] calculateDOB(int day, int month, int year, int refDay, int refMonth, int refYear) {
        int[] dob = new int[3];
        dob[0] = refDay - day;
        dob[1] = refMonth - month;
        dob[2] = refYear - year;
        if (dob[0] < 1) {
            dob[0] += daysInMonth(refMonth - 1, refYear);
            dob[1]--;
        }
        if (dob[1] < 1) {
            dob[1] += 12;
            dob[2]--;
        }
        return dob;
    }

    static int daysInMonth(int month, int year) {
        return switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            case 4, 6, 9, 11 -> 30;
            case 2 -> isLeapYear(year) ? 29 : 28;
            default -> 0;
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter date of birth or Age (Ex: AGE=0054-03-23, DOB=19-05-2005): ");
        String input = sc.nextLine();

        System.out.print("\nEnter reference date: ");
        String refDate = sc.nextLine();

        System.out.print("\nChoose Dob format:\n1.YYYY-MM-DD\n2.DD-MM-YYYY\n3.MM-DD-YYYY\nEnter choice: ");
        String format = sc.nextLine();

        System.out.print("\nInput delimiter character: ");
        String dlc = sc.nextLine();

        if (!input.contains("=") || refDate.isEmpty() || dlc.isEmpty()) {
            System.out.println("\nInvalid input format. Please follow instructions.");
            sc.close();
            return;
        }

        String[] refParts = refDate.split(dlc);
        int[] refDateArr = parseDate(refParts);

        if (refDateArr == null || !isValidDate(refDateArr[0], refDateArr[1], refDateArr[2])) {
            System.out.println("\nInvalid reference date.");
            sc.close();
            return;
        }

        if (input.startsWith("DOB=")) {
            String dob = input.split("=")[1];
            String[] dobParts = dob.split(dlc);

            int[] dobArr;
            switch (format) {
                case "1":
                    dobArr = parseDate(new String[] {dobParts[2], dobParts[1], dobParts[0]});
                    break;
                case "2":
                    dobArr = parseDate(new String[] {dobParts[0], dobParts[1], dobParts[2]});
                    break;
                case "3":
                    dobArr = parseDate(new String[] {dobParts[1], dobParts[0], dobParts[2]});
                    break;
                default:
                    System.out.println("\nInvalid format choice.");
                    sc.close();
                    return;
            }

            if (dobArr == null || !isValidDate(dobArr[0], dobArr[1], dobArr[2])) {
                System.out.println("\nInvalid date of birth.");
                sc.close();
                return;
            }

            int[] age = calculateAge(dobArr[0], dobArr[1], dobArr[2], refDateArr[0], refDateArr[1], refDateArr[2]);
            if (age[2] < 0) {
                System.out.println("\nReference year is earlier than DOB.");
                sc.close();
                return;
            }
            System.out.printf("Age is %d days, %d months, %d years\n", age[0], age[1], age[2]);
        } else if (input.startsWith("AGE=")) {
            String ageStr = input.split("=")[1];
            String[] ageParts = ageStr.split(dlc);

            int[] ageArr = parseDate(ageParts);
            if (ageArr == null || ageArr[0] <= 0 || ageArr[1] <= 0 || ageArr[2] <= 0) {
                System.out.println("\nInvalid age format.");
                sc.close();
                return;
            }

            int[] dob = calculateDOB(ageArr[0], ageArr[1], ageArr[2], refDateArr[0], refDateArr[1], refDateArr[2]);
            System.out.printf("Date of Birth is %d%s%d%s%d\n", dob[0], dlc, dob[1], dlc, dob[2]);
        } else {
            System.out.println("\nInvalid input type. Use 'DOB=' or 'AGE='.");
        }

        sc.close();
    }
}
