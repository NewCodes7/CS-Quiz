package newcodes.CSQuiz.domain;

public enum Category {
    Operating_System(1, "운영체제"),
    Network(2, "네트워크"),
    Database(3, "데이터베이스"),
    Data_Structure(4, "자료구조"),
    Algorithm(5, "알고리즘");

    private final String value;
    private final int id;

    Category(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return value;
    }
}
