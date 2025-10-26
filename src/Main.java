import functions.*;

public class Main {
    public static void main(String[] args) {
        double[] vals = {0, 1, 4, 9, 16};

        // можно переключать реализацию: Array или LinkedList
        TabulatedFunction func = new ArrayTabulatedFunction(0, 4, vals);
        //TabulatedFunction func = new LinkedListTabulatedFunction(0, 4, vals);

        System.out.println("Точки исходной функции:");
        print(func);

        System.out.println("f(2.5) = " + func.getFunctionValue(2.5));
        System.out.println();

        // попытка установить x, который нарушает порядок
        try {
            func.setPointX(1, 10);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Поймано исключение при setPointX: " + e.getMessage());
        }

        // добавление новой точки
        try {
            func.addPoint(new FunctionPoint(2.2, 5));
            System.out.println("После добавления (2.2,5):");
            print(func);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Не удалось добавить точку: " + e.getMessage());
        }


        try {
            func.deletePoint(0);
            func.deletePoint(0);
            System.out.println("После двух удалений:");
            print(func);
        } catch (IllegalStateException e) {
            System.out.println("Ошибка состояния при удалении: " + e.getMessage());
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Неправильный индекс при удалении: " + e.getMessage());
        }
    }

    private static void print(TabulatedFunction f) {
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println(i + ": " + f.getPoint(i));
        }
        System.out.println();
    }
}


