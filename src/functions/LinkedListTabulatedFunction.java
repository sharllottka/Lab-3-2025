package functions;

// табулированная функция на двусвязном списке
public class LinkedListTabulatedFunction implements TabulatedFunction {
    private class FunctionNode {
        FunctionPoint point;
        FunctionNode next;
        FunctionNode prev;

        FunctionNode(FunctionPoint p) {
            this.point = new FunctionPoint(p);
        }
    }

    private FunctionNode head; // фиктивная голова списка
    private int pointsCount;

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница >= правой");
        if (values.length < 2)
            throw new IllegalArgumentException("Количество точек < 2");

        head = new FunctionNode(new FunctionPoint());
        head.next = head;
        head.prev = head;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            addNodeToTail(new FunctionPoint(leftX + i * step, values[i]));
        }
    }

    private void addNodeToTail(FunctionPoint p) {
        FunctionNode node = new FunctionNode(p);
        if (pointsCount == 0) {
            head.next = node;
            head.prev = node;
            node.next = head;
            node.prev = head;
        } else {
            FunctionNode last = head.prev;
            last.next = node;
            node.prev = last;
            node.next = head;
            head.prev = node;
        }
        pointsCount++;
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс: " + index);
        FunctionNode node = head.next;
        for (int i = 0; i < index; i++) node = node.next;
        return node;
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException();
        if ((index > 0 && point.getX() <= getPointX(index - 1)) ||
                (index < pointsCount - 1 && point.getX() >= getPointX(index + 1)))
            throw new InappropriateFunctionPointException("x нарушает порядок точек");

        getNodeByIndex(index).point = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        setPoint(index, new FunctionPoint(x, getPointY(index)));
    }

    @Override
    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    @Override
    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }

    @Override
    public void deletePoint(int index) {
        if (pointsCount <= 2)
            throw new IllegalStateException("Нельзя удалить — останется меньше двух точек");

        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        pointsCount--;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode cur = head.next;
        while (cur != head) {
            if (cur.point.getX() == point.getX())
                throw new InappropriateFunctionPointException("Такая точка уже есть");
            if (cur.point.getX() > point.getX())
                break;
            cur = cur.next;
        }
        FunctionNode node = new FunctionNode(point);
        node.next = cur;
        node.prev = cur.prev;
        cur.prev.next = node;
        cur.prev = node;
        pointsCount++;
    }

    @Override
    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    @Override
    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        FunctionNode cur = head.next;
        while (cur.next != head) {
            double x1 = cur.point.getX();
            double x2 = cur.next.point.getX();
            if (x >= x1 && x <= x2) {
                double y1 = cur.point.getY();
                double y2 = cur.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            cur = cur.next;
        }
        return Double.NaN;
    }
}
